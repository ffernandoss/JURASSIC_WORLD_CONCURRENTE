// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/MasterScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.SseController;
import org.example.jurassic_world_concurrente.visitante.DistribuidorVisitantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.time.Duration;

@Component
public class MasterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MasterScheduler.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private DinosaurioEstadoService dinosaurioEstadoService;

    @Autowired
    private DistribuidorVisitantes distribuidorVisitantes;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SseController sseController; // Inyectar SseController

    private Disposable disposable;
    private int ticsTotales = 0;

    public void iniciarSimulacion() {
        // Eliminar el archivo de informe al inicio de la simulación
        File file = new File("informacion_simulacion.txt");
        if (file.exists()) {
            file.delete();
        }

        Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticsTotales++;
                    logger.info("---------------------------Año de la simulación: {}---------------------------", ticsTotales);

                    // Envejecer dinosaurios y verificar muertes
                    dinosaurioService.envejecerDinosaurios();

                    // Verificar salida de dinosaurios enfermos de la enfermería
                    dinosaurioService.verificarSalidaEnfermeria(ticsTotales);

                    // Incubar huevos y verificar eclosión
                    huevoService.incubarHuevos();

                    // Enviar mensaje a la cola para verificar dinosaurios
                    rabbitTemplate.convertAndSend("verificarDinosauriosQueue", "Verificar");

                    // Mostrar lista de dinosaurios
                    logger.info("Lista de dinosaurios: {}", dinosaurioService.getDinosaurios());

                    // Evento cada 10 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 7 == 0) {
                        dinosaurioService.generarEventoMuerteAleatoria();
                    }

                    // Evento de reproducción cada 5 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 10 == 0) {
                        huevoService.crearHuevoAleatorio();
                        logger.info("Evento de reproducción: se ha creado un nuevo huevo.");
                    }

                    // Guardar información en notas cada 2 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 1 == 0) {
                        guardarInformacionEnNotas();
                    }
                    // Publicar estados actualizados
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("verificarDinosauriosQueue", "Verificar");

                    // Imprimir estado actual de dinosaurios
                    imprimirEstadoActual();
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    private void guardarInformacionEnNotas() {
    Flux.zip(
            distribuidorVisitantes.getIslaFlux("IslaCarnivoros").obtenerFlujoVisitantes().collectList(),
            distribuidorVisitantes.getIslaFlux("IslaHerbivoros").obtenerFlujoVisitantes().collectList(),
            distribuidorVisitantes.getIslaFlux("IslaVoladores").obtenerFlujoVisitantes().collectList()
    ).doOnNext(tuple -> {
        StringBuilder info = new StringBuilder();
        info.append("Tic: ").append(ticsTotales).append("\n");
        info.append("Lista de dinosaurios: ").append(dinosaurioService.getDinosaurios()).append("\n");
        info.append("Lista de dinosaurios enfermos: ").append(dinosaurioService.getDinosauriosEnfermos()).append("\n");
        info.append("Lista de huevos: ").append(huevoService.getHuevos()).append("\n");
        info.append("Visitantes en IslaCarnivoros: ").append(tuple.getT1()).append("\n");
        info.append("Visitantes en IslaHerbivoros: ").append(tuple.getT2()).append("\n");
        info.append("Visitantes en IslaVoladores: ").append(tuple.getT3()).append("\n");
        info.append("--------------------------------------------------\n");

        String infoCarnivoros = "Visitantes en IslaCarnivoros: " + tuple.getT1() + "\nDinosaurios en IslaCarnivoros: " + distribuidorVisitantes.getIslaFlux("IslaCarnivoros").getTotalDinosaurios();
        String infoHerbivoros = "Visitantes en IslaHerbivoros: " + tuple.getT2() + "\nDinosaurios en IslaHerbivoros: " + distribuidorVisitantes.getIslaFlux("IslaHerbivoros").getTotalDinosaurios();
        String infoVoladores = "Visitantes en IslaVoladores: " + tuple.getT3() + "\nDinosaurios en IslaVoladores: " + distribuidorVisitantes.getIslaFlux("IslaVoladores").getTotalDinosaurios();
        String infoEnfermeria = "Dinosaurios enfermos: " + dinosaurioService.getDinosauriosEnfermos();

        sseController.sendEvent(info.toString());
        sseController.sendEventToIsla("IslaCarnivoros", infoCarnivoros);
        sseController.sendEventToIsla("IslaHerbivoros", infoHerbivoros);
        sseController.sendEventToIsla("IslaVoladores", infoVoladores);
        sseController.sendEventToEnfermeria(infoEnfermeria);
    }).subscribe();
}

    private void imprimirEstadoActual() {
        // Lista de huevos
        logger.info("Huevos en incubación: ");
        huevoService.getHuevos().forEach(huevo -> logger.info("- {}", huevo));

        dinosaurioEstadoService.imprimirDinosauriosEnfermos();
    }
}
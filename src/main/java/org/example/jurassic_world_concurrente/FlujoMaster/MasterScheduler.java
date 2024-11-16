// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/MasterScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
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
import java.io.FileWriter;
import java.io.IOException;
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
            try (FileWriter writer = new FileWriter("informacion_simulacion.txt", true)) {
                writer.write("Tic: " + ticsTotales + "\n");
                writer.write("Lista de dinosaurios: " + dinosaurioService.getDinosaurios() + "\n");
                writer.write("Lista de dinosaurios enfermos: " + dinosaurioService.getDinosauriosEnfermos() + "\n");
                writer.write("Lista de huevos: " + huevoService.getHuevos() + "\n");
                writer.write("Visitantes en IslaCarnivoros: " + tuple.getT1() + "\n");
                writer.write("Visitantes en IslaHerbivoros: " + tuple.getT2() + "\n");
                writer.write("Visitantes en IslaVoladores: " + tuple.getT3() + "\n");
                writer.write("--------------------------------------------------\n");
            } catch (IOException e) {
                logger.error("Error al guardar la información en notas: ", e);
            }
        }).subscribe();
    }

    private void imprimirEstadoActual() {
        // Lista de huevos
        logger.info("Huevos en incubación: ");
        huevoService.getHuevos().forEach(huevo -> logger.info("- {}", huevo));

        dinosaurioEstadoService.imprimirDinosauriosEnfermos();
    }
}
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

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
    private RabbitTemplate rabbitTemplate;
    private Disposable disposable;

    private int ticsTotales = 0;

    public void iniciarSimulacion() {
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
                    // Publicar estados actualizados
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("verificarDinosauriosQueue", "Verificar");

                    // Imprimir estado actual de dinosaurios
                    imprimirEstadoActual();
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    private void imprimirEstadoActual() {

        // Lista de huevos
        logger.info("Huevos en incubación: ");
        huevoService.getHuevos().forEach(huevo -> logger.info("- {}", huevo));

        dinosaurioEstadoService.imprimirDinosauriosEnfermos();
    }
}
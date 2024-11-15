// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/EnfermeriaScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

@Component
public class EnfermeriaScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EnfermeriaScheduler.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private DinosaurioEstadoService dinosaurioEstadoService;

    private Disposable disposable;

    public void iniciarEnfermeria() {
        disposable = Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {

                    // Enviar dinosaurios enfermos a la cola de enfermería
                    List<Dinosaurio> dinosauriosEnfermos = new ArrayList<>(dinosaurioService.getDinosauriosEnfermos());
                    dinosauriosEnfermos.forEach(dinosaurio -> {
                        if (dinosaurio.isEstaEnfermo()) {
                            logger.info("Dinosaurio enfermo: " + dinosaurio.getNombre());
                            rabbitTemplate.convertAndSend("enfermeriaQueue", dinosaurio);
                            dinosaurioService.suscribirDinosaurioEnfermo(dinosaurio);
                        } else {
                            dinosaurioService.desuscribirDinosaurioEnfermo(dinosaurio);
                            dinosaurioService.suscribirDinosaurio(dinosaurio);
                        }
                    });

                    // Actualizar estados de dinosaurios
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");

                    // Mostrar array de dinosaurios enfermos
                    logger.info("Mostrando lista de dinosaurios enfermos:");
                    dinosauriosEnfermos.stream()
                            .filter(Dinosaurio::isEstaEnfermo)
                            .forEach(dinosaurio -> logger.info(dinosaurio.toString()));

                    // Log the updated states of the sick dinosaurs
                    logger.info("Actualización dinosaurios enfermos:");
                    logger.info("Estados de dinosaurios enfermos actualizados: {}", dinosaurioEstadoService.getEstados());
                })
                .filter(tic -> dinosaurioService.getDinosaurios().stream().noneMatch(Dinosaurio::isEstaEnfermo)) // Filtrar dinosaurios curados
                .doOnNext(tic -> {
                    List<Dinosaurio> dinosaurios = dinosaurioService.getDinosaurios();
                    dinosaurios.stream()
                            .filter(Dinosaurio::isEstaEnfermo)
                            .forEach(dinosaurio -> logger.info("Dinosaurio en el flujo: {}", dinosaurio));
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    public void detenerEnfermeria() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/EnfermeriaScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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

    public void iniciarEnfermeria() {
        Flux.interval(Duration.ofSeconds(2))
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
                    dinosaurioEstadoService.imprimirDinosauriosEnfermos();

                    // Log the updated states of the sick dinosaurs
                    logger.info("Actualización dinosaurios enfermos:");
                    logger.info("Estados de dinosaurios enfermos actualizados: {}", dinosaurioEstadoService.getEstados());
                })
                .subscribe();
    }
}
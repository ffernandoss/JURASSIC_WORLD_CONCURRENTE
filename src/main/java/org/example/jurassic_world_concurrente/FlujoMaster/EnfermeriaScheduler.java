package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

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
        Flux.interval(Duration.ofSeconds(1))
                .doOnNext(tic -> {

                    // Enviar dinosaurios enfermos a la cola de enfermería
                    List<Dinosaurio> dinosauriosEnfermos = dinosaurioService.getDinosauriosEnfermos();
                    dinosauriosEnfermos.forEach(dinosaurio -> {
                        if (dinosaurio.isEstaEnfermo()) {
                            logger.info("PUTOS DINOSAURIOS: " + dinosaurio.getNombre());
                            rabbitTemplate.convertAndSend("enfermeriaQueue", dinosaurio);
                        }
                    });

                    // Mostrar array de dinosaurios enfermos
                    logger.info("Mostrando lista de dinosaurios enfermos:");
                    dinosaurioEstadoService.imprimirDinosauriosEnfermos();
                    logger.info("Fin de la lista de dinosaurios enfermos.");
                })
                .subscribe();
    }
}
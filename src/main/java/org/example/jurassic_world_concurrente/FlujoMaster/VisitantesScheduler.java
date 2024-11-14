// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/VisitantesScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Visitante.VisitanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class VisitantesScheduler {

    private static final Logger logger = LoggerFactory.getLogger(VisitantesScheduler.class);

    @Autowired
    private VisitanteService visitanteService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private int ticsTotales = 0;

    public void iniciarSimulacion() {
        Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticsTotales++;
                    logger.info("---------------------------Año de la simulación: {}---------------------------", ticsTotales);

                    // Obtener y mostrar lista de visitantes
                    logger.info("Lista de visitantes: {}", visitanteService.obtenerTodosLosVisitantes());

                    // Enviar mensaje a la cola de visitantes
                    rabbitTemplate.convertAndSend("visitantesQueue", "Actualizar visitantes");

                    // Evento cada 10 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 10 == 0) {
                        // Lógica para evento cada 10 tics
                        logger.info("Evento especial cada 10 tics.");
                    }

                    // Evento cada 5 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 5 == 0) {
                        // Lógica para evento cada 5 tics
                        logger.info("Evento especial cada 5 tics.");
                    }
                })
                .subscribe();
    }
}
// src/main/java/org/example/jurassic_world_concurrente/rabbit/VisitorNotificationListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.visitante.DistribuidorVisitantes;
import org.example.jurassic_world_concurrente.visitante.Visitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitorNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(VisitorNotificationListener.class);

    @Autowired
    private DistribuidorVisitantes distribuidorVisitantes;

    @RabbitListener(queues = "visitorNotificationQueue")
    public void handleVisitorNotification(String islaNombre) {

        logger.info("Isla {} ha alcanzado el máximo de visitantes.", islaNombre);
        List<String> islas = new ArrayList<>(List.of("IslaCarnivoros", "IslaHerbivoros", "IslaVoladores"));
        islas.remove(islaNombre);

        for (String isla : islas) {
            if (distribuidorVisitantes.getIslaFlux(isla).getTotalVisitantes() < 3) {
                Visitante visitante = distribuidorVisitantes.getIslaFlux(islaNombre).removeVisitante();
                if (visitante != null) {
                    distribuidorVisitantes.getIslaFlux(isla).agregarVisitante(visitante).subscribe();
                    logger.info("Visitante movido de {} a {}", islaNombre, isla);
                }
                break;
            }
        }
    }
}
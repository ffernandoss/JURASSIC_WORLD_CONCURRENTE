// src/main/java/org/example/jurassic_world_concurrente/rabbit/VisitantesListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class VisitantesListener {

    private static final Logger logger = LoggerFactory.getLogger(VisitantesListener.class);

    @RabbitListener(queues = "visitantesQueue")
    public void handleVisitantesQueue(String message) {
        logger.info("Mensaje recibido de la cola de visitantes: {}", message);
        // Lógica para manejar la actualización de visitantes
    }
}
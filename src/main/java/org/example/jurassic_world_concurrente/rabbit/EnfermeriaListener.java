// src/main/java/org/example/jurassic_world_concurrente/rabbit/EnfermeriaListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnfermeriaListener {

    private static final Logger logger = LoggerFactory.getLogger(EnfermeriaListener.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @RabbitListener(queues = "enfermeriaQueue")
    public void handleEnfermeriaMessage(Dinosaurio dinosaurio) {
        logger.info("Mensaje recibido en la cola de enfermería: " + dinosaurio);
        if (dinosaurio == null) {
            logger.warn("Dinosaurio nulo recibido en la cola de enfermería.");
            return;
        }
        try {
            if (dinosaurio.isEstaEnfermo()) {
                dinosaurioService.suscribirDinosaurioEnfermo(dinosaurio);
                logger.info("Dinosaurio enfermo añadido: " + dinosaurio.getNombre());
            } else {
                dinosaurioService.desuscribirDinosaurioEnfermo(dinosaurio);
                dinosaurioService.suscribirDinosaurio(dinosaurio);
                logger.info("Dinosaurio recuperado: " + dinosaurio.getNombre());
            }
            logger.info("Estado del dinosaurio actualizado: {}", dinosaurio);
        } catch (Exception e) {
            logger.error("Error al procesar el mensaje de la cola de enfermería", e);
        }
    }
}
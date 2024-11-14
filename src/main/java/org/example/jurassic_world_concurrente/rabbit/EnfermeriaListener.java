package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnfermeriaListener {

    private static final Logger logger = LoggerFactory.getLogger(EnfermeriaListener.class);
    private List<Dinosaurio> dinosauriosEnfermos = new ArrayList<>();

    @RabbitListener(queues = "enfermeriaQueue")
    public void handleEnfermeriaMessage(@Payload Dinosaurio dinosaurio) {
        logger.info("Mensaje recibido en la cola de enfermería: " + dinosaurio);
        if (dinosaurio == null) {
            logger.error("El dinosaurio recibido es nulo");
            return;
        }
        try {
            if (dinosaurio.isEstaEnfermo()) {
                synchronized (dinosauriosEnfermos) {
                    dinosauriosEnfermos.add(dinosaurio);
                }
                logger.info("Dinosaurio enfermo añadido: " + dinosaurio.getNombre());
                logger.info("Lista actual de dinosaurios enfermos: " + dinosauriosEnfermos);
            }
        } catch (Exception e) {
            logger.error("Error al procesar el mensaje de la cola de enfermería", e);
        }
    }

    public List<Dinosaurio> getDinosauriosEnfermos() {
        synchronized (dinosauriosEnfermos) {
            return new ArrayList<>(dinosauriosEnfermos);
        }
    }
}
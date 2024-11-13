package org.example.jurassic_world_concurrente.Eventos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EventoListener {
    private static final Logger logger = LoggerFactory.getLogger(EventoListener.class);

    @Autowired
    private Evento evento;

    @RabbitListener(queues = "eventoQueue")
    public void handleEvento(String message) {
        if ("mataryreproducir".equals(message)) {
            List<Dinosaurio> dinosaurios = evento.getMundoGeneral().getDinosaurios();
            if (!dinosaurios.isEmpty()) {
                Random random = new Random();
                Dinosaurio dinosaurio = dinosaurios.get(random.nextInt(dinosaurios.size()));
                evento.matarDinosaurio(dinosaurio);
                evento.reproducirDinosaurio(); // Call without arguments
            } else {
                logger.warn("No hay dinosaurios en el Mundo General para matar y reproducir.");
            }
        } else {
            logger.warn("Acción desconocida: {}", message);
        }
    }
}
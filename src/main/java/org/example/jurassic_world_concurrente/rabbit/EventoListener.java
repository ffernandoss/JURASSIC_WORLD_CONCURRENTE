package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Evento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoListener {
    private static final Logger logger = LoggerFactory.getLogger(EventoListener.class);

    @Autowired
    private Evento evento;

    @RabbitListener(queues = "eventoQueue")
    public void handleEvento(String message) {
        if ("mataryreproducir".equals(message)) {
            evento.matarYReproducir();
        } else {
            logger.warn("Acción desconocida: {}", message);
        }
    }
}
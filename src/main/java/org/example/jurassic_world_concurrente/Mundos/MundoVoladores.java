package org.example.jurassic_world_concurrente.Mundos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MundoVoladores implements Mundo {
    private static final Logger logger = LoggerFactory.getLogger(MundoVoladores.class);
    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private final int id = 3;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
        logger.info("Dinosaurio {} añadido al Mundo Voladores", dinosaurio.getNombre());
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " añadido al Mundo Voladores");
    }

    @Override
    public void removeDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.remove(dinosaurio);
        logger.info("Dinosaurio {} removido del Mundo Voladores", dinosaurio.getNombre());
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " removido del Mundo Voladores");
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<Dinosaurio> getDinosaurios() {
        return dinosaurios;
    }
}
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
public class MundoCarnivoros implements Mundo {
    private static final Logger logger = LoggerFactory.getLogger(MundoCarnivoros.class);
    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private final int id = 1;
    private int contadorCarnivoros = 0;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
        contadorCarnivoros++;
        logger.info("Dinosaurio {} añadido al Mundo Carnivoros. Total carnivoros: {}", dinosaurio.getNombre(), contadorCarnivoros);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " añadido al Mundo Carnivoros");
    }

    @Override
    public void removeDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.remove(dinosaurio);
        if (contadorCarnivoros > 0) {
            contadorCarnivoros--;
        }
        logger.info("Dinosaurio {} removido del Mundo Carnivoros. Total carnivoros: {}", dinosaurio.getNombre(), contadorCarnivoros);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " removido del Mundo Carnivoros");
    }

    public int getContadorCarnivoros() {
        return contadorCarnivoros;
    }

    @Override
    public int getId() {
        return id;
    }
}
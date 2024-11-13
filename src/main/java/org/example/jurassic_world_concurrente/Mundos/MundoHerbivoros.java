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
public class MundoHerbivoros implements Mundo {
    private static final Logger logger = LoggerFactory.getLogger(MundoHerbivoros.class);
    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private final int id = 2;
    private int contadorHerbivoros = 0;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
        contadorHerbivoros++;
        logger.info("Dinosaurio {} añadido al Mundo Herbivoros", dinosaurio.getNombre());
        logger.info("Total herbivoros: {}", contadorHerbivoros);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " añadido al Mundo Herbivoros");
    }

    @Override
    public void removeDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.remove(dinosaurio);
        contadorHerbivoros--;
        logger.info("Dinosaurio {} removido del Mundo Herbivoros", dinosaurio.getNombre());
        logger.info("Total herbivoros: {}", contadorHerbivoros);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " removido del Mundo Herbivoros");
    }

    public int getContadorHerbivoros() {
        return contadorHerbivoros;
    }

    @Override
    public int getId() {
        return id;
    }
}
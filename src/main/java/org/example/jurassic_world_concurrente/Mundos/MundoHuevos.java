package org.example.jurassic_world_concurrente.Mundos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MundoHuevos implements Mundo {
    private static final Logger logger = LoggerFactory.getLogger(MundoHuevos.class);
    private List<Huevo> huevos = new ArrayList<>();
    private final int id = 4;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void addHuevo(Huevo huevo) {
        huevos.add(huevo);
        logger.info("Huevo de tipo {} añadido al Mundo Huevos", huevo.getTipo());
        rabbitTemplate.convertAndSend("worldChangeQueue", "Huevo de tipo " + huevo.getTipo() + " añadido al Mundo Huevos");
    }

    public void removeHuevo(Huevo huevo) {
        huevos.remove(huevo);
        logger.info("Huevo de tipo {} removido del Mundo Huevos", huevo.getTipo());
        rabbitTemplate.convertAndSend("worldChangeQueue", "Huevo de tipo " + huevo.getTipo() + " removido del Mundo Huevos");
    }

    @Override
    public void addDinosaurio(Dinosaurio dinosaurio) {
        // Not applicable for MundoHuevos
    }

    @Override
    public void removeDinosaurio(Dinosaurio dinosaurio) {
        // Not applicable for MundoHuevos
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<Dinosaurio> getDinosaurios() {
        return Collections.emptyList();
    }
}
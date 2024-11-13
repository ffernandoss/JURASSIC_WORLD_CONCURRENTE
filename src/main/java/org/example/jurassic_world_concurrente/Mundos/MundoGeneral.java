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
public class MundoGeneral {
    private static final Logger logger = LoggerFactory.getLogger(MundoGeneral.class);
    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private List<String> nombresDinosaurios = new ArrayList<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void addDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
        nombresDinosaurios.add(dinosaurio.getNombre());
        logger.info("Dinosaurio {} añadido al Mundo General", dinosaurio.getNombre());
        logger.info("Lista de dinosaurios en Mundo General: {}", nombresDinosaurios);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " añadido al Mundo General");
    }

    public void removeDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.remove(dinosaurio);
        nombresDinosaurios.remove(dinosaurio.getNombre());
        logger.info("Dinosaurio {} removido del Mundo General", dinosaurio.getNombre());
        logger.info("Lista de dinosaurios en Mundo General: {}", nombresDinosaurios);
        rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " removido del Mundo General");
    }

    public List<Dinosaurio> getDinosaurios() {
        return dinosaurios;
    }

    public List<String> getNombresDinosaurios() {
        return nombresDinosaurios;
    }
}
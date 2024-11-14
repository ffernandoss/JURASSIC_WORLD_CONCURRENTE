
// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioService.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DinosaurioService {
    private static final Logger logger = LoggerFactory.getLogger(DinosaurioService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private List<Dinosaurio> dinosaurios = new ArrayList<>();

    public void envejecerDinosaurios() {
        List<Dinosaurio> dinosauriosParaEliminar = new ArrayList<>();
        dinosaurios.forEach(dinosaurio -> {
            if (dinosaurio.getEdad() < dinosaurio.getMaxEdad()) {
                dinosaurio.envejecer();
                logger.info("Dinosaurio {} tiene ahora {} años.", dinosaurio.getNombre(), dinosaurio.getEdad());
            } else {
                dinosauriosParaEliminar.add(dinosaurio);
            }
        });
        dinosauriosParaEliminar.forEach(this::matarDinosaurio);
    }

    public void matarDinosaurio(Dinosaurio dinosaurio) {
        logger.info("Dinosaurio {} ha muerto de viejo.", dinosaurio.getNombre());
        dinosaurios.remove(dinosaurio);
        rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
    }

    public void generarEventoMuerteAleatoria() {
        if (!dinosaurios.isEmpty()) {
            Dinosaurio randomDino = dinosaurios.get(new Random().nextInt(dinosaurios.size()));
            matarDinosaurio(randomDino);
            logger.info("Evento de muerte aleatoria: Dinosaurio {} ha sido eliminado.", randomDino.getNombre());
        }
    }

    public void agregarDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
    }

    public List<Dinosaurio> getDinosaurios() {
        return dinosaurios;
    }
}
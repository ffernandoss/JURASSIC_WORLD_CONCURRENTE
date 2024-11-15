// src/main/java/org/example/jurassic_world_concurrente/Islas/IslaCarnivoro.java
package org.example.jurassic_world_concurrente.Islas;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IslaCarnivoro {
    private static final Logger logger = LoggerFactory.getLogger(IslaCarnivoro.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    public void mostrarInformacion() {
        String info = dinosaurioService.getDinosaurios().stream()
                .filter(dino -> "Carnivoro".equalsIgnoreCase(dino.getTipo()))
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Información de la Isla Carnívoro: [{}]", info);
    }
}
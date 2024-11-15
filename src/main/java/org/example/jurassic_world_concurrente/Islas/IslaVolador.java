// src/main/java/org/example/jurassic_world_concurrente/Islas/IslaVolador.java
package org.example.jurassic_world_concurrente.Islas;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IslaVolador {
    private static final Logger logger = LoggerFactory.getLogger(IslaVolador.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    public void mostrarInformacion() {
        logger.info("Información de la Isla Volador:");
        dinosaurioService.getDinosaurios().stream()
                .filter(dino -> "Volador".equalsIgnoreCase(dino.getTipo()))
                .forEach(dino -> logger.info(dino.toString()));
    }
}
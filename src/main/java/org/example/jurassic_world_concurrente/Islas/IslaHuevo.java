// src/main/java/org/example/jurassic_world_concurrente/Islas/IslaHuevo.java
package org.example.jurassic_world_concurrente.Islas;

import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IslaHuevo {
    private static final Logger logger = LoggerFactory.getLogger(IslaHuevo.class);

    @Autowired
    private HuevoService huevoService;

    public void mostrarInformacion() {
        String info = huevoService.getHuevos().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Información de la Isla Huevo:\nHuevos [{}]", info);
    }
}
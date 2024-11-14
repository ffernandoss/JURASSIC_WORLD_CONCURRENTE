// src/main/java/org/example/jurassic_world_concurrente/rabbit/DinosaurioEstadoListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DinosaurioEstadoListener {

    private static final Logger logger = LoggerFactory.getLogger(DinosaurioEstadoListener.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private DinosaurioEstadoService dinosaurioEstadoService;

    @RabbitListener(queues = "actualizarDinosaurioEstadoQueue")
    public void handleActualizarDinosaurioEstado(String message) {
        dinosaurioEstadoService.actualizarEstados(dinosaurioService.getDinosaurios());
        logger.info("Estados de dinosaurios actualizados: {}", dinosaurioEstadoService.getEstados());
    }
}
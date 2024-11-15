package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.time.Duration;

public class VisitanteGenerator {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteGenerator.class);

    public Flux<Visitante> generarVisitantesContinuos() {
        return Flux.interval(Duration.ofSeconds(3)) // Cada tick es 3 segundos
                .map(tic -> {
                    String nombre = "Visitante_" + tic;
                    Visitante visitante = new Visitante(nombre);
                    logger.info("Nuevo visitante generado: {}", visitante);
                    return visitante;
                });
    }
}
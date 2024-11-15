package org.example.jurassic_world_concurrente.visitante;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Random;
import java.util.stream.IntStream;

public class VisitanteGenerator {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteGenerator.class);
    private final Random random = new Random();

    public Flux<Visitante> generarVisitantesContinuos() {
        return Flux.interval(Duration.ofSeconds(3)) // Cada tick es 3 segundos
                .flatMap(tic -> {
                    int numVisitantes = random.nextInt(4) + 1;
                    return Flux.fromStream(IntStream.range(0, numVisitantes)
                            .mapToObj(i -> {
                                String nombre = "Visitante_" + tic + "_" + i;
                                Visitante visitante = new Visitante(nombre);
                                logger.info("Nuevo visitante generado: {}", visitante);
                                return visitante;
                            }));
                });
    }
}
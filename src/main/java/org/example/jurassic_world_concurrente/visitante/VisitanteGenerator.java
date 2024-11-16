package org.example.jurassic_world_concurrente.visitante;

import org.example.jurassic_world_concurrente.Isla.IslaFlux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.Random;
import java.util.stream.IntStream;

public class VisitanteGenerator {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteGenerator.class);
    private final Random random = new Random();
    private final DistribuidorVisitantes distribuidorVisitantes;

    public VisitanteGenerator(DistribuidorVisitantes distribuidorVisitantes) {
        this.distribuidorVisitantes = distribuidorVisitantes;
    }

    public Flux<Visitante> generarVisitantesContinuos() {
        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(tic -> {
                    logger.info("--------------------------- Generación de visitantes {} ---------------------------", tic);
                    if (islasLlenas()) {
                        logger.info("Todas las islas están llenas. Pausando generación de visitantes por 2 segundos.");
                        return Mono.delay(Duration.ofSeconds(4)).thenMany(Flux.empty());
                    } else {
                        int numVisitantes = random.nextInt(5) + 1;
                        logger.info("Generando {} visitantes en el tick {}", numVisitantes, tic);
                        return Flux.fromStream(IntStream.range(0, numVisitantes)
                                .mapToObj(i -> {
                                    String nombre = "Visitante_" + tic + "_" + i;
                                    Visitante visitante = new Visitante(nombre);
                                    logger.info("Nuevo visitante generado: {}", visitante);
                                    return visitante;
                                }));
                    }
                });
    }

    private boolean islasLlenas() {
        return distribuidorVisitantes.getIslaFlux("IslaCarnivoros").getTotalVisitantes() >= 4 &&
               distribuidorVisitantes.getIslaFlux("IslaHerbivoros").getTotalVisitantes() >= 4 &&
               distribuidorVisitantes.getIslaFlux("IslaVoladores").getTotalVisitantes() >= 4;
    }
}
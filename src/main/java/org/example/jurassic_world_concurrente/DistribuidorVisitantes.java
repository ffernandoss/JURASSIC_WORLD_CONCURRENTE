package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class DistribuidorVisitantes {
    private static final Logger logger = LoggerFactory.getLogger(DistribuidorVisitantes.class);
    private static final List<String> islas = Arrays.asList("IslaCarnivoros", "IslaHerbivoros", "IslaVoladores");
    private static final Random random = new Random();
    private static final AtomicInteger totalVisitantesCarnivoros = new AtomicInteger(0);
    private static final AtomicInteger totalVisitantesHerbivoros = new AtomicInteger(0);
    private static final AtomicInteger totalVisitantesVoladores = new AtomicInteger(0);

    public static Mono<Void> moverAIsla(Visitante visitante) {
        String islaAsignada = islas.get(random.nextInt(islas.size()));
        logger.info("Visitante {} asignado a {}", visitante.getNombre(), islaAsignada);

        int total;
        switch (islaAsignada) {
            case "IslaCarnivoros":
                total = totalVisitantesCarnivoros.incrementAndGet();
                logger.info("Número total de visitantes en IslaCarnivoros: {}", total);
                break;
            case "IslaHerbivoros":
                total = totalVisitantesHerbivoros.incrementAndGet();
                logger.info("Número total de visitantes en IslaHerbivoros: {}", total);
                break;
            case "IslaVoladores":
                total = totalVisitantesVoladores.incrementAndGet();
                logger.info("Número total de visitantes en IslaVoladores: {}", total);
                break;
        }
        // Aquí puedes agregar lógica para manejar el visitante en la isla correspondiente
        return Mono.empty();
    }
}
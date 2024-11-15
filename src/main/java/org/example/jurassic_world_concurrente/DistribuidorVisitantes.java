package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DistribuidorVisitantes {
    private static final Logger logger = LoggerFactory.getLogger(DistribuidorVisitantes.class);
    private static final List<String> islas = List.of("IslaCarnivoros", "IslaHerbivoros", "IslaVoladores");
    private static final Random random = new Random();
    private static final Map<String, IslaFlux> islaFluxMap = new HashMap<>();

    static {
        islas.forEach(isla -> islaFluxMap.put(isla, new IslaFlux(isla)));
    }

    public static Mono<Void> moverAIsla(Visitante visitante) {
        String islaAsignada = islas.get(random.nextInt(islas.size()));
        logger.info("Visitante {} asignado a {}", visitante.getNombre(), islaAsignada);
        return islaFluxMap.get(islaAsignada).agregarVisitante(visitante);
    }
}
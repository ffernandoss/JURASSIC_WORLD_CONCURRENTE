// src/main/java/org/example/jurassic_world_concurrente/DistribuidorVisitantes.java
package org.example.jurassic_world_concurrente.visitante;

import org.example.jurassic_world_concurrente.Isla.IslaFlux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DistribuidorVisitantes {
    private static final Logger logger = LoggerFactory.getLogger(DistribuidorVisitantes.class);
    private static final List<String> islas = List.of("IslaCarnivoros", "IslaHerbivoros", "IslaVoladores");
    private static final Random random = new Random();
    private static final Map<String, IslaFlux> islaFluxMap = new HashMap<>();
    private final RabbitTemplate rabbitTemplate;

    public DistribuidorVisitantes(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        islas.forEach(isla -> islaFluxMap.put(isla, new IslaFlux(isla, rabbitTemplate)));
    }

    public Mono<Void> moverAIsla(Visitante visitante) {
        String islaAsignada = islas.get(random.nextInt(islas.size()));
        logger.info("Visitante {} asignado a {}", visitante.getNombre(), islaAsignada);
        return islaFluxMap.get(islaAsignada).agregarVisitante(visitante);
    }

    public IslaFlux getIslaFlux(String islaNombre) {
        return islaFluxMap.get(islaNombre);
    }

    public Flux<Visitante> obtenerFlujosIslas() {
        return Flux.merge(
                islaFluxMap.get("IslaCarnivoros").obtenerFlujoVisitantes(),
                islaFluxMap.get("IslaHerbivoros").obtenerFlujoVisitantes(),
                islaFluxMap.get("IslaVoladores").obtenerFlujoVisitantes()
        );
    }
}
package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IslaFlux {
    private static final Logger logger = LoggerFactory.getLogger(IslaFlux.class);
    private final String nombreIsla;
    private final AtomicInteger totalVisitantes = new AtomicInteger(0);
    private final List<Visitante> visitantes = new ArrayList<>();

    public IslaFlux(String nombreIsla) {
        this.nombreIsla = nombreIsla;
    }

    public Mono<Void> agregarVisitante(Visitante visitante) {
        return Mono.just(visitante)
                .doOnNext(v -> {
                    visitantes.add(v);
                    int total = totalVisitantes.incrementAndGet();
                    logger.info("Visitante {} ha entrado a {}. Total visitantes: {}", v.getNombre(), nombreIsla, total);
                })
                .delayElement(Duration.ofSeconds(10))
                .doOnNext(v -> {
                    visitantes.remove(v);
                    int total = totalVisitantes.decrementAndGet();
                    logger.info("Visitante {} ha salido de {}. Total visitantes: {}", v.getNombre(), nombreIsla, total);
                })
                .then();
    }
}
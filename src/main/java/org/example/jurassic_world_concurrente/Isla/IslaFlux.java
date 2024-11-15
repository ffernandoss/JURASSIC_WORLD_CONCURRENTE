package org.example.jurassic_world_concurrente.Isla;

import org.example.jurassic_world_concurrente.visitante.Visitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Flux;
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
    private static final int MAX_VISITANTES = 4;
    private final RabbitTemplate rabbitTemplate;

    public IslaFlux(String nombreIsla, RabbitTemplate rabbitTemplate) {
        this.nombreIsla = nombreIsla;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Void> agregarVisitante(Visitante visitante) {
        return Mono.just(visitante)
                .filter(v -> totalVisitantes.get() < MAX_VISITANTES)
                .doOnNext(v -> {
                    synchronized (this) {
                        visitantes.add(v);
                        int total = totalVisitantes.incrementAndGet();
                        logger.info("Visitante {} ha entrado a {}. Total visitantes: {}", v.getNombre(), nombreIsla, total);
                        if (total == MAX_VISITANTES) {
                            rabbitTemplate.convertAndSend("visitorNotificationQueue", nombreIsla);
                        }
                    }
                })
                .delayElement(Duration.ofSeconds(5))
                .doOnNext(v -> {
                    synchronized (this) {
                        visitantes.remove(v);
                        int total = totalVisitantes.decrementAndGet();
                        if (total < 0) {
                            totalVisitantes.set(0);
                            total = 0;
                        }
                        logger.info("Visitante {} ha salido de {}. Total visitantes: {}", v.getNombre(), nombreIsla, total);
                    }
                })
                .then();
    }

    public synchronized int getTotalVisitantes() {
        return totalVisitantes.get();
    }

    public synchronized Visitante removeVisitante() {
        if (!visitantes.isEmpty()) {
            Visitante visitante = visitantes.remove(0);
            int total = totalVisitantes.decrementAndGet();
            if (total < 0) {
                totalVisitantes.set(0);
            }
            return visitante;
        }
        return null;
    }

    public Flux<Visitante> obtenerFlujoVisitantes() {
        return Flux.fromIterable(visitantes);
    }
}
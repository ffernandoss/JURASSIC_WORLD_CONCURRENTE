package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ContadorAnios {
    private static final Logger logger = LoggerFactory.getLogger(ContadorAnios.class);
    private final AtomicInteger anios = new AtomicInteger(0);

    public Flux<Integer> iniciarContador() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
                    int anioActual = anios.incrementAndGet();
                    logger.info("Han transcurrido {} años en la simulación.", anioActual);
                    return anioActual;
                });
    }
}
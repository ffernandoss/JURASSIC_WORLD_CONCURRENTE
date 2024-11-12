package org.example.jurassic_world_concurrente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ContadorAnios {
    private static final Logger logger = LoggerFactory.getLogger(ContadorAnios.class);
    private final AtomicInteger anios = new AtomicInteger(0);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Flux<Integer> iniciarContador() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
                    int anioActual = anios.incrementAndGet();
                    logger.info("Han transcurrido {} años en la simulación.", anioActual);
                    if (anioActual > 0 && anioActual % 5 == 0) {
                        rabbitTemplate.convertAndSend("eventoQueue", "mataryreproducir");
                    }
                    return anioActual;
                });
    }
}
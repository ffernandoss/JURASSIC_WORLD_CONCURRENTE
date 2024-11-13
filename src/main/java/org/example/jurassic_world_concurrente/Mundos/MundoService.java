package org.example.jurassic_world_concurrente.Mundos;

import org.example.jurassic_world_concurrente.Visitantes.Visitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Service
public class MundoService {
    private static final Logger logger = LoggerFactory.getLogger(MundoService.class);
    private final Sinks.Many<Visitante> mundoCarnivorosSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Visitante> mundoHerbivorosSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Visitante> mundoVoladoresSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Visitante> flujoPrincipalSink = Sinks.many().multicast().onBackpressureBuffer();

    @Autowired
    private MundoCarnivoros mundoCarnivoros;

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoVoladores mundoVoladores;

    @RabbitListener(queues = "mundoCarnivorosQueue")
    public void handleMundoCarnivorosVisitante(Visitante visitante) {
        logger.info("Visitante {} dirigido a Mundo Carnivoros", visitante.getNombre());
        mundoCarnivoros.addVisitante(visitante);
        mundoCarnivorosSink.tryEmitNext(visitante);
    }

    @RabbitListener(queues = "mundoHerbivorosQueue")
    public void handleMundoHerbivorosVisitante(Visitante visitante) {
        logger.info("Visitante {} dirigido a Mundo Herbivoros", visitante.getNombre());
        mundoHerbivoros.addVisitante(visitante);
        mundoHerbivorosSink.tryEmitNext(visitante);
    }

    @RabbitListener(queues = "mundoVoladoresQueue")
    public void handleMundoVoladoresVisitante(Visitante visitante) {
        logger.info("Visitante {} dirigido a Mundo Voladores", visitante.getNombre());
        mundoVoladores.addVisitante(visitante);
        mundoVoladoresSink.tryEmitNext(visitante);
    }

    public Flux<Visitante> flujoMundoCarnivoros() {
        return mundoCarnivorosSink.asFlux()
                .delayElements(Duration.ofSeconds(1))
                .flatMap(visitante -> {
                    visitante.setTiempoEnParque(visitante.getTiempoEnParque() + 1);
                    if (visitante.getTiempoEnParque() >= 2) {
                        mundoCarnivoros.removeVisitante(visitante);
                        flujoPrincipalSink.tryEmitNext(visitante);
                        logger.info("Visitante {} ha abandonado Mundo Carnivoros y vuelve al flujo principal", visitante.getNombre());
                    }
                    return Flux.just(visitante);
                });
    }

    public Flux<Visitante> flujoMundoHerbivoros() {
        return mundoHerbivorosSink.asFlux()
                .delayElements(Duration.ofSeconds(1))
                .flatMap(visitante -> {
                    visitante.setTiempoEnParque(visitante.getTiempoEnParque() + 1);
                    if (visitante.getTiempoEnParque() >= 2) {
                        mundoHerbivoros.removeVisitante(visitante);
                        flujoPrincipalSink.tryEmitNext(visitante);
                        logger.info("Visitante {} ha abandonado Mundo Herbivoros y vuelve al flujo principal", visitante.getNombre());
                    }
                    return Flux.just(visitante);
                });
    }

    public Flux<Visitante> flujoMundoVoladores() {
        return mundoVoladoresSink.asFlux()
                .delayElements(Duration.ofSeconds(1))
                .flatMap(visitante -> {
                    visitante.setTiempoEnParque(visitante.getTiempoEnParque() + 1);
                    if (visitante.getTiempoEnParque() >= 2) {
                        mundoVoladores.removeVisitante(visitante);
                        flujoPrincipalSink.tryEmitNext(visitante);
                        logger.info("Visitante {} ha abandonado Mundo Voladores y vuelve al flujo principal", visitante.getNombre());
                    }
                    return Flux.just(visitante);
                });
    }

    public Flux<Visitante> flujoPrincipal() {
        return flujoPrincipalSink.asFlux();
    }
}
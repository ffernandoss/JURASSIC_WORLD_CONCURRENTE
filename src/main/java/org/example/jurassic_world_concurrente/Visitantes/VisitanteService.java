package org.example.jurassic_world_concurrente.Visitantes;

import org.example.jurassic_world_concurrente.Mundos.MundoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VisitanteService {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteService.class);
    private final AtomicLong idCounter = new AtomicLong();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MundoService mundoService; // Inject MundoService

    public Flux<Visitante> flujoPrincipalVisitantes() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
                    Visitante visitante = new Visitante();
                    visitante.setId(idCounter.incrementAndGet());
                    visitante.setNombre("Visitante_" + visitante.getId());
                    visitante.setUbicacion("Entrada");
                    visitante.setTiempoEnParque(0); // Inicializar tiempo en parque
                    logger.info("Nuevo visitante: {}", visitante.getNombre());
                    return visitante;
                })
                .flatMap(visitante -> {
                    String routingKey;
                    int randomWorld = (int) (Math.random() * 3);
                    switch (randomWorld) {
                        case 0:
                            routingKey = "mundoCarnivoros";
                            mundoService.handleMundoCarnivorosVisitante(visitante);
                            break;
                        case 1:
                            routingKey = "mundoHerbivoros";
                            mundoService.handleMundoHerbivorosVisitante(visitante);
                            break;
                        case 2:
                        default:
                            routingKey = "mundoVoladores";
                            mundoService.handleMundoVoladoresVisitante(visitante);
                            break;
                    }
                    logger.info("Visitante {} enviado a {}", visitante.getNombre(), routingKey);
                    return Flux.just(visitante); // Return the visitor to ensure the correct type
                })
                .mergeWith(mundoService.flujoMundoCarnivoros())
                .mergeWith(mundoService.flujoMundoHerbivoros())
                .mergeWith(mundoService.flujoMundoVoladores())
                .mergeWith(mundoService.flujoPrincipal());
    }
}
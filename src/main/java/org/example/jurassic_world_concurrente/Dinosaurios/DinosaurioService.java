package org.example.jurassic_world_concurrente.Dinosaurios;

import org.example.jurassic_world_concurrente.Mundos.MundoCarnivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoHerbivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoVoladores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class DinosaurioService {
    private static final Logger logger = LoggerFactory.getLogger(DinosaurioService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MundoCarnivoros mundoCarnivoros;

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoVoladores mundoVoladores;

    private ConcurrentMap<String, SynchronousSink<Dinosaurio>> sinkMap = new ConcurrentHashMap<>();

    public Flux<Dinosaurio> gestionarVidaCarnivoros(List<Dinosaurio> carnivoros) {
        return gestionarVidaDinosauriosPorTipo(carnivoros, "Carnivoro");
    }

    public Flux<Dinosaurio> gestionarVidaHerbivoros(List<Dinosaurio> herbivoros) {
        return gestionarVidaDinosauriosPorTipo(herbivoros, "Herbivoro");
    }

    public Flux<Dinosaurio> gestionarVidaVoladores(List<Dinosaurio> voladores) {
        return gestionarVidaDinosauriosPorTipo(voladores, "Volador");
    }

    private Flux<Dinosaurio> gestionarVidaDinosauriosPorTipo(List<Dinosaurio> dinosaurios, String tipo) {
        return Flux.fromIterable(dinosaurios)
                .filter(dinosaurio -> dinosaurio.getTipo().equalsIgnoreCase(tipo))
                .flatMap(this::gestionarVidaDinosaurio);
    }

    public Flux<Dinosaurio> gestionarVidaDinosaurio(Dinosaurio dinosaurio) {
        return Flux.<Dinosaurio>generate(sink -> {
            sinkMap.put(dinosaurio.getNombre(), sink);
            if (dinosaurio.getEdad() < dinosaurio.getMaxEdad()) {
                dinosaurio.envejecer();
                logger.info("{} tiene {} años.", dinosaurio.getNombre(), dinosaurio.getEdad());
                sink.next(dinosaurio);
            } else {
                dinosaurio.morir();
                rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
                switch (dinosaurio.getTipo().toLowerCase()) {
                    case "carnivoro":
                        mundoCarnivoros.removeDinosaurio(dinosaurio);
                        logger.info("Total carnivoros: {}", mundoCarnivoros.getContadorCarnivoros());
                        break;
                    case "herbivoro":
                        mundoHerbivoros.removeDinosaurio(dinosaurio);
                        logger.info("Total herbivoros: {}", mundoHerbivoros.getContadorHerbivoros());
                        break;
                    case "volador":
                        mundoVoladores.removeDinosaurio(dinosaurio);
                        logger.info("Total voladores: {}", mundoVoladores.getContadorVoladores());
                        break;
                }
                unsubscribeDinosaur(dinosaurio);
                sink.complete();
            }
        }).delayElements(Duration.ofSeconds(1));
    }

    public void unsubscribeDinosaur(Dinosaurio dinosaurio) {
        SynchronousSink<Dinosaurio> sink = sinkMap.get(dinosaurio.getNombre());
        if (sink != null) {
            sink.complete();
            sinkMap.remove(dinosaurio.getNombre());
            logger.info("Dinosaurio {} ha sido desuscrito del flujo.", dinosaurio.getNombre());
        }
    }
}
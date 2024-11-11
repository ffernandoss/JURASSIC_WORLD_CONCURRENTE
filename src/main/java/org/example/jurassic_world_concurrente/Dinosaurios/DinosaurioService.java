package org.example.jurassic_world_concurrente.Dinosaurios;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class DinosaurioService {

    public Flux<Dinosaurio> gestionarVidaDinosaurios(List<Dinosaurio> dinosaurios) {
        return Flux.fromIterable(dinosaurios)
                .flatMap(this::gestionarVidaDinosaurio);
    }

    private Flux<Dinosaurio> gestionarVidaDinosaurio(Dinosaurio dinosaurio) {
        return Flux.<Dinosaurio>generate(sink -> {
                    if (dinosaurio.getEdad() < dinosaurio.getMaxEdad()) {
                        dinosaurio.envejecer();
                        sink.next(dinosaurio);
                    } else {
                        dinosaurio.morir();
                        sink.complete();
                    }
                })
                .delayElements(Duration.ofSeconds(1));
    }
}
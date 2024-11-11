package org.example.jurassic_world_concurrente.Dinosaurios;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class DinosaurioService {

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
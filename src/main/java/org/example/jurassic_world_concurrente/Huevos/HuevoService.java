package org.example.jurassic_world_concurrente.Huevos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class HuevoService {

    @Autowired
    private DinosaurioService dinosaurioService;

    public Flux<Huevo> gestionarIncubacionHuevos(List<Huevo> huevos) {
        return Flux.fromIterable(huevos)
                .flatMap(this::gestionarIncubacionHuevo);
    }

    private Flux<Huevo> gestionarIncubacionHuevo(Huevo huevo) {
        return Flux.<Huevo>generate(sink -> {
                    if (!"Eclosionado".equals(huevo.getEstado())) {
                        huevo.incubar();
                        sink.next(huevo);
                    } else {
                        Dinosaurio dinosaurio = huevo.transformarADinosaurio();
                        dinosaurioService.gestionarVidaDinosaurios(List.of(dinosaurio))
                                .subscribe(d -> System.out.println(d.getNombre() + " tiene " + d.getEdad() + " años."));
                        sink.complete();
                    }
                })
                .delayElements(Duration.ofSeconds(1));
    }
}
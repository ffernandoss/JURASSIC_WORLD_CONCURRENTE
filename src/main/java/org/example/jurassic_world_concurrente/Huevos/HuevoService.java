package org.example.jurassic_world_concurrente.Huevos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
public class HuevoService {
    private static final Logger logger = LoggerFactory.getLogger(HuevoService.class);

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
                        switch (dinosaurio.getTipo().toLowerCase()) {
                            case "carnivoro":
                                dinosaurioService.gestionarVidaCarnivoros(List.of(dinosaurio))
                                        .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                                break;
                            case "herbivoro":
                                dinosaurioService.gestionarVidaHerbivoros(List.of(dinosaurio))
                                        .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                                break;
                            case "volador":
                                dinosaurioService.gestionarVidaVoladores(List.of(dinosaurio))
                                        .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                                break;
                        }
                        sink.complete();
                    }
                })
                .delayElements(Duration.ofSeconds(1));
    }
}
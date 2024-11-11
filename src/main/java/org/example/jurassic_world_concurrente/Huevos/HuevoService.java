package org.example.jurassic_world_concurrente.Huevos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Mundos.MundoCarnivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoHerbivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoVoladores;
import org.example.jurassic_world_concurrente.Mundos.MundoHuevos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private MundoCarnivoros mundoCarnivoros;

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoVoladores mundoVoladores;

    @Autowired
    private MundoHuevos mundoHuevos;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
                        mundoCarnivoros.addDinosaurio(dinosaurio);
                        dinosaurioService.gestionarVidaCarnivoros(List.of(dinosaurio))
                                .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                        break;
                    case "herbivoro":
                        mundoHerbivoros.addDinosaurio(dinosaurio);
                        dinosaurioService.gestionarVidaHerbivoros(List.of(dinosaurio))
                                .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                        break;
                    case "volador":
                        mundoVoladores.addDinosaurio(dinosaurio);
                        dinosaurioService.gestionarVidaVoladores(List.of(dinosaurio))
                                .subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
                        break;
                }
                mundoHuevos.removeHuevo(huevo);
                logger.info("Dinosaurio {} ha sido movido a su mundo correspondiente: {}", dinosaurio.getNombre(), dinosaurio.getTipo());
                rabbitTemplate.convertAndSend("worldChangeQueue", "Dinosaurio " + dinosaurio.getNombre() + " movido a su mundo correspondiente");
                sink.complete();
            }
        }).delayElements(Duration.ofSeconds(1));
    }
}
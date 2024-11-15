// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioService.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DinosaurioService {
    private static final Logger logger = LoggerFactory.getLogger(DinosaurioService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private List<Dinosaurio> dinosauriosEnfermos = new ArrayList<>();
    private List<Disposable> disposables = new ArrayList<>();

    public void envejecerDinosaurios() {
        List<Dinosaurio> dinosauriosParaEliminar = new ArrayList<>();
        dinosaurios.forEach(dinosaurio -> {
            if (dinosaurio.getEdad() < dinosaurio.getMaxEdad()) {
                dinosaurio.envejecer();
                logger.info("Dinosaurio {} tiene ahora {} años.", dinosaurio.getNombre(), dinosaurio.getEdad());
            } else {
                dinosauriosParaEliminar.add(dinosaurio);
            }
        });
        dinosauriosParaEliminar.forEach(this::matarDinosaurio);
    }

    public void matarDinosaurio(Dinosaurio dinosaurio) {
        logger.info("Dinosaurio {} ha muerto de viejo.", dinosaurio.getNombre());
        dinosaurios.remove(dinosaurio);
        rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
    }

    public void generarEventoMuerteAleatoria() {
        if (!dinosaurios.isEmpty()) {
            Dinosaurio randomDino = dinosaurios.get(new Random().nextInt(dinosaurios.size()));
            logger.info("Evento de muerte aleatoria: Dinosaurio {} fue asesinado.", randomDino.getNombre());
            dinosaurios.remove(randomDino);
        }
    }

    public void agregarDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
    }

    public void suscribirDinosaurio(Dinosaurio dinosaurio) {
        if (!dinosaurios.contains(dinosaurio)) {
            dinosaurios.add(dinosaurio);
            Disposable disposable = Flux.just(dinosaurio)
                    .doOnSubscribe(subscription -> logger.info("!!!!!Dinosaurio suscrito a dinosaurios: " + dinosaurio.getNombre()))
                    .subscribe();
            disposables.add(disposable);
        }
    }

    public void desuscribirDinosaurio(Dinosaurio dinosaurio) {
        if (dinosaurios.contains(dinosaurio)) {
            dinosaurios.remove(dinosaurio);
            disposables.forEach(Disposable::dispose);
            disposables.clear();
            logger.info("!!!!Dinosaurio desuscrito de dinosaurios: " + dinosaurio.getNombre());
        }
    }

    public List<Dinosaurio> getDinosaurios() {
        return dinosaurios;
    }

    public boolean existeDinosaurioDeTipo(String tipo) {
        return dinosaurios.stream().anyMatch(dino -> dino.getTipo().equalsIgnoreCase(tipo));
    }

    public void suscribirDinosaurioEnfermo(Dinosaurio dinosaurio) {
        if (!dinosauriosEnfermos.contains(dinosaurio)) {
            dinosauriosEnfermos.add(dinosaurio);
            Disposable disposable = Flux.just(dinosaurio)
                    .doOnSubscribe(subscription -> logger.info("HAN PEGADO UN TIRO A " + dinosaurio.getNombre()))
                    .subscribe();
            disposables.add(disposable);
        }
    }

    public void desuscribirDinosaurioEnfermo(Dinosaurio dinosaurio) {
        if (dinosauriosEnfermos.contains(dinosaurio)) {
            dinosauriosEnfermos.remove(dinosaurio);
            disposables.forEach(Disposable::dispose);
            disposables.clear();
            logger.info("UNA DOCTORA CULONA HA CURADO A " + dinosaurio.getNombre());
            suscribirDinosaurio(dinosaurio); // Añadir el dinosaurio a la lista de dinosaurios
        }
    }

    public List<Dinosaurio> getDinosauriosEnfermos() {
        return dinosauriosEnfermos;
    }
}
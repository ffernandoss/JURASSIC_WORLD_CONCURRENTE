package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.*;
import org.example.jurassic_world_concurrente.Huevos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@SpringBootApplication
public class JurassicWorldConcurrenteApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JurassicWorldConcurrenteApplication.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private FabricaDinosaurios fabricaDinosaurios;

    @Autowired
    private FabricaHuevos fabricaHuevos;

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create two dinosaurs of each type using the factory
        Dinosaurio carnivoro1 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio carnivoro2 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio herbivoro1 = fabricaDinosaurios.crearDinosaurio("Herbivoro");
        Dinosaurio herbivoro2 = fabricaDinosaurios.crearDinosaurio("Herbivoro");
        Dinosaurio volador1 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio volador2 = fabricaDinosaurios.crearDinosaurio("Volador");


        // Manage lifecycle of each type of dinosaur concurrently
        Flux<Dinosaurio> carnivoroFlux = dinosaurioService.gestionarVidaCarnivoros(Arrays.asList(carnivoro1, carnivoro2));
        Flux<Dinosaurio> herbivoroFlux = dinosaurioService.gestionarVidaHerbivoros(Arrays.asList(herbivoro1, herbivoro2));
        Flux<Dinosaurio> voladorFlux = dinosaurioService.gestionarVidaVoladores(Arrays.asList(volador1, volador2));

        carnivoroFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
        herbivoroFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
        voladorFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
    }
}
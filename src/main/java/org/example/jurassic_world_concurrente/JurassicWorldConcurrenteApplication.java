package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.*;
import org.example.jurassic_world_concurrente.Eventos.ContadorAnios;
import org.example.jurassic_world_concurrente.Huevos.*;
import org.example.jurassic_world_concurrente.Mundos.MundoCarnivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoHerbivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoVoladores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@SpringBootApplication
public class
JurassicWorldConcurrenteApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(JurassicWorldConcurrenteApplication.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private FabricaDinosaurios fabricaDinosaurios;

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private ContadorAnios contadorAnios;

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoCarnivoros mundoCarnivoros;

    @Autowired
    private MundoVoladores mundoVoladores;


    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create two dinosaurs of each type using the factory
        Dinosaurio h1 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio h2 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio h3 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio h5 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio h4 = fabricaDinosaurios.crearDinosaurio("Herbivoro");

        // Add herbivore dinosaurs to MundoHerbivoros
        mundoVoladores.addDinosaurio(h1);
        mundoVoladores.addDinosaurio(h2);
        mundoCarnivoros.addDinosaurio(h3);
        mundoHerbivoros.addDinosaurio(h4);


        // Manage lifecycle of each type of dinosaur concurrently
        Flux<Dinosaurio> voladorFlux = dinosaurioService.gestionarVidaVoladores(Arrays.asList(h1, h2));
        Flux<Dinosaurio> carnivoroFlux = dinosaurioService.gestionarVidaCarnivoros(Arrays.asList(h3,h5));
        Flux<Dinosaurio> herbivoroFlux = dinosaurioService.gestionarVidaHerbivoros(Arrays.asList(h4));

        voladorFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
        carnivoroFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));
        herbivoroFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));

        // Start the year counter
        contadorAnios.iniciarContador().subscribe();
    }
}
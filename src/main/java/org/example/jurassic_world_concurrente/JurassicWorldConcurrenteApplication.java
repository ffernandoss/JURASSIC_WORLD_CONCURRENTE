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

    @Autowired
    private ContadorAnios contadorAnios;

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create two dinosaurs of each type using the factory

        Dinosaurio volador1 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio volador2 = fabricaDinosaurios.crearDinosaurio("Volador");

        // Manage lifecycle of each type of dinosaur concurrently

        Flux<Dinosaurio> voladorFlux = dinosaurioService.gestionarVidaVoladores(Arrays.asList(volador1, volador2));

        voladorFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));

        // Start the year counter
        contadorAnios.iniciarContador().subscribe();
    }
}
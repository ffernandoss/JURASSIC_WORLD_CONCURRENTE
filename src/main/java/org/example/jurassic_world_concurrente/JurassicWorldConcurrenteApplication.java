package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.*;
import org.example.jurassic_world_concurrente.Huevos.*;
import org.example.jurassic_world_concurrente.Mundos.MundoHerbivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoService;
import org.example.jurassic_world_concurrente.Visitantes.VisitanteService;
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

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoService mundoService;

    @Autowired
    private VisitanteService visitanteService;

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create two dinosaurs of each type using the factory
        Dinosaurio h1 = fabricaDinosaurios.crearDinosaurio("Herbivoro");
        Dinosaurio h2 = fabricaDinosaurios.crearDinosaurio("Herbivoro");
        Dinosaurio h3 = fabricaDinosaurios.crearDinosaurio("Herbivoro");

        // Add herbivore dinosaurs to MundoHerbivoros
        mundoHerbivoros.addDinosaurio(h1);
        mundoHerbivoros.addDinosaurio(h2);
        mundoHerbivoros.addDinosaurio(h3);

        // Manage lifecycle of each type of dinosaur concurrently
        Flux<Dinosaurio> herbivoroFlux = dinosaurioService.gestionarVidaHerbivoros(Arrays.asList(h1, h2, h3));

        herbivoroFlux.subscribe(d -> logger.info("{} tiene {} años.", d.getNombre(), d.getEdad()));

        // Start the year counter
        contadorAnios.iniciarContador().subscribe();

        // Subscribe to visitor flows for each world
        visitanteService.flujoPrincipalVisitantes().subscribe();

        mundoService.flujoMundoCarnivoros().subscribe(visitante -> logger.info("Procesando visitante en Mundo Carnivoros: {}", visitante.getNombre()));
        mundoService.flujoMundoHerbivoros().subscribe(visitante -> logger.info("Procesando visitante en Mundo Herbivoros: {}", visitante.getNombre()));
        mundoService.flujoMundoVoladores().subscribe(visitante -> logger.info("Procesando visitante en Mundo Voladores: {}", visitante.getNombre()));
    }
}
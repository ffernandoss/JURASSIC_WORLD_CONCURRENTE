package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.*;
import org.example.jurassic_world_concurrente.Huevos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class JurassicWorldConcurrenteApplication implements CommandLineRunner {

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

        // Print details of the created dinosaurs
        System.out.println("Dinosaurio creado: " + carnivoro1.getNombre() + ", tipo: " + carnivoro1.getTipo());
        System.out.println("Dinosaurio creado: " + carnivoro2.getNombre() + ", tipo: " + carnivoro2.getTipo());
        System.out.println("Dinosaurio creado: " + herbivoro1.getNombre() + ", tipo: " + herbivoro1.getTipo());
        System.out.println("Dinosaurio creado: " + herbivoro2.getNombre() + ", tipo: " + herbivoro2.getTipo());
        System.out.println("Dinosaurio creado: " + volador1.getNombre() + ", tipo: " + volador1.getTipo());
        System.out.println("Dinosaurio creado: " + volador2.getNombre() + ", tipo: " + volador2.getTipo());

        // Manage lifecycle of each type of dinosaur concurrently
        Flux<Dinosaurio> carnivoroFlux = dinosaurioService.gestionarVidaCarnivoros(Arrays.asList(carnivoro1, carnivoro2));
        Flux<Dinosaurio> herbivoroFlux = dinosaurioService.gestionarVidaHerbivoros(Arrays.asList(herbivoro1, herbivoro2));
        Flux<Dinosaurio> voladorFlux = dinosaurioService.gestionarVidaVoladores(Arrays.asList(volador1, volador2));

        carnivoroFlux.subscribe(d -> System.out.println(d.getNombre() + " tiene " + d.getEdad() + " años."));
        herbivoroFlux.subscribe(d -> System.out.println(d.getNombre() + " tiene " + d.getEdad() + " años."));
        voladorFlux.subscribe(d -> System.out.println(d.getNombre() + " tiene " + d.getEdad() + " años."));
    }
}
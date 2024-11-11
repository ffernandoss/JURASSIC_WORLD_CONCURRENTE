package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.*;
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

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Dinosaurio carnivoro1 = new Carnivoro();
        carnivoro1.setNombre("T-Rex 1");
        carnivoro1.setEdad(0);
        carnivoro1.setEstaEnfermo(false);

        Dinosaurio carnivoro2 = new Carnivoro();
        carnivoro2.setNombre("T-Rex 2");
        carnivoro2.setEdad(0);
        carnivoro2.setEstaEnfermo(false);

        Dinosaurio herbivoro1 = new Herbivoro();
        herbivoro1.setNombre("Triceratops 1");
        herbivoro1.setEdad(0);
        herbivoro1.setEstaEnfermo(false);

        Dinosaurio herbivoro2 = new Herbivoro();
        herbivoro2.setNombre("Triceratops 2");
        herbivoro2.setEdad(0);
        herbivoro2.setEstaEnfermo(false);

        Dinosaurio volador1 = new Volador();
        volador1.setNombre("Pterodactyl 1");
        volador1.setEdad(0);
        volador1.setEstaEnfermo(false);

        Dinosaurio volador2 = new Volador();
        volador2.setNombre("Pterodactyl 2");
        volador2.setEdad(0);
        volador2.setEstaEnfermo(false);

        List<Dinosaurio> dinosaurios = Arrays.asList(carnivoro1, carnivoro2, herbivoro1, herbivoro2, volador1, volador2);

        Flux<Dinosaurio> dinosaurioFlux = dinosaurioService.gestionarVidaDinosaurios(dinosaurios);

        dinosaurioFlux.subscribe(dinosaurio -> {
            System.out.println(dinosaurio.getNombre() + " tiene " + dinosaurio.getEdad() + " años.");
        });
    }
}
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

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Huevo huevoCarnivoro = new Huevo("Carnivoro", 2);
        Huevo huevoHerbivoro = new Huevo("Herbivoro", 5);
        Huevo huevoVolador = new Huevo("Volador", 3);

        List<Huevo> huevos = Arrays.asList(huevoCarnivoro, huevoHerbivoro, huevoVolador);

        Flux<Huevo> huevoFlux = huevoService.gestionarIncubacionHuevos(huevos);

        huevoFlux.subscribe(huevo -> {
            System.out.println("Huevo de tipo " + huevo.getTipo() + " está " + huevo.getEstado() + " con " + huevo.getTiempoIncubacion() + " días de incubación.");
        });

        // Create two dinosaurs of each type
        Dinosaurio carnivoro1 = new Carnivoro();
        Dinosaurio carnivoro2 = new Carnivoro();
        Dinosaurio herbivoro1 = new Herbivoro();
        Dinosaurio herbivoro2 = new Herbivoro();
        Dinosaurio volador1 = new Volador();
        Dinosaurio volador2 = new Volador();

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
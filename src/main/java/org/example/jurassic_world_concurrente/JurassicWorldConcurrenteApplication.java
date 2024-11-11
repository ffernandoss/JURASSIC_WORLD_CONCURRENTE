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
        Huevo huevoCarnivoro = new Huevo("Carnivoro", 10);
        Huevo huevoHerbivoro = new Huevo("Herbivoro", 15);

        List<Huevo> huevos = Arrays.asList(huevoCarnivoro, huevoHerbivoro);

        Flux<Huevo> huevoFlux = huevoService.gestionarIncubacionHuevos(huevos);

        huevoFlux.subscribe(huevo -> {
            System.out.println("Huevo de tipo " + huevo.getTipo() + " está " + huevo.getEstado() + " con " + huevo.getTiempoIncubacion() + " días de incubación.");
        });
    }
}
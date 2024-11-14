package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.FlujoMaster.MasterScheduler;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Dinosaurios.FabricaDinosaurios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    private MasterScheduler masterScheduler;

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Crear dinosaurios de cada tipo usando la fábrica y agregar al DinosaurioService
        /*Dinosaurio carnivoro1 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio carnivoro2 = fabricaDinosaurios.crearDinosaurio("Carnivoro");
        Dinosaurio herbivoro1 = fabricaDinosaurios.crearDinosaurio("Herbivoro");
        Dinosaurio herbivoro2 = fabricaDinosaurios.crearDinosaurio("Herbivoro");*/
        Dinosaurio volador1 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio volador2 = fabricaDinosaurios.crearDinosaurio("Volador");

        // Agregar dinosaurios al servicio para ser gestionados en el flujo maestro
        Arrays.asList(volador1, volador2)
                .forEach(dinosaurioService::agregarDinosaurio);

        logger.info("Dinosaurios iniciales creados y agregados al servicio de gestión.");

        // Iniciar el flujo maestro que controla el tiempo de simulación
        masterScheduler.iniciarSimulacion();
    }
}

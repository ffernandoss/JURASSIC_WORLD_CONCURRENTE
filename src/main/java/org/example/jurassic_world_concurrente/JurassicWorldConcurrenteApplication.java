// src/main/java/org/example/jurassic_world_concurrente/JurassicWorldConcurrenteApplication.java
package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.FlujoMaster.MasterScheduler;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Dinosaurios.FabricaDinosaurios;
import org.example.jurassic_world_concurrente.visitante.DistribuidorVisitantes;
import org.example.jurassic_world_concurrente.visitante.Visitante;
import org.example.jurassic_world_concurrente.visitante.VisitanteGenerator;
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
    private MasterScheduler masterScheduler;

    @Autowired
    private DistribuidorVisitantes distribuidorVisitantes;

    public static void main(String[] args) {
        SpringApplication.run(JurassicWorldConcurrenteApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Crear dinosaurios de cada tipo usando la fábrica y agregar al DinosaurioService
        Dinosaurio volador1 = fabricaDinosaurios.crearDinosaurio("Volador");
        Dinosaurio volador2 = fabricaDinosaurios.crearDinosaurio("Volador");

        // Agregar dinosaurios al servicio para ser gestionados en el flujo maestro
        Arrays.asList(volador1, volador2)
                .forEach(dinosaurioService::agregarDinosaurio);

        logger.info("Dinosaurios iniciales creados y agregados al servicio de gestión.");

        // Iniciar el flujo maestro que controla el tiempo de simulación
        masterScheduler.iniciarSimulacion();

        // Iniciar la generación de visitantes
        VisitanteGenerator visitanteGenerator = new VisitanteGenerator();
        Flux<Visitante> visitantesFlux = visitanteGenerator.generarVisitantesContinuos();

        // Combinar flujos de visitantes y flujos de islas
        Flux<Visitante> flujoCombinado = Flux.merge(
                visitantesFlux,
                distribuidorVisitantes.obtenerFlujosIslas()
        );

        flujoCombinado
                .flatMap(distribuidorVisitantes::moverAIsla) // Distribuye visitantes
                .subscribe();

        // Keep the application running to observe the visitor generation
        try {
            Thread.sleep(10000); // Run for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
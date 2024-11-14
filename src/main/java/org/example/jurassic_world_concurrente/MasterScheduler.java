package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class MasterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MasterScheduler.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    private int ticsTotales = 0;

    public void iniciarSimulacion() {
        Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticsTotales++;
                    logger.info("---------------------------Año de la simulación: {}---------------------------", ticsTotales);

                    // Envejecer dinosaurios y verificar muertes
                    dinosaurioService.envejecerDinosaurios();

                    // Incubar huevos y verificar eclosión
                    huevoService.incubarHuevos();

                    // Mostrar lista de dinosaurios
                    logger.info("Lista de dinosaurios: {}", dinosaurioService.getDinosaurios());

                    // Evento cada 5 tics (cada 10 segundos)
                    //if (ticsTotales % 5 == 0) {
                     //   dinosaurioService.generarEventoMuerteAleatoria();
                    //}
                })
                .subscribe();
    }
}
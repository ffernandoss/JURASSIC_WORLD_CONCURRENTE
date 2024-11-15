// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/MasterScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Visitantes.VisitanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

@Component
public class MasterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MasterScheduler.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private VisitanteService visitanteService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private int ticsTotales = 0;
    private Random random = new Random();

    public void iniciarSimulacion() {
        // Flujo principal para generar eventos
        Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticsTotales++;
                    logger.info("---------------------------Año de la simulación: {}---------------------------", ticsTotales);

                    // Envejecer dinosaurios y verificar muertes
                    dinosaurioService.envejecerDinosaurios();

                    // Enviar mensaje a la cola para verificar dinosaurios
                    rabbitTemplate.convertAndSend("verificarDinosauriosQueue", "Verificar");

                    // Enviar mensajes a las colas de RabbitMQ para islas
                    rabbitTemplate.convertAndSend("islaHuevoQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("islaCarnivoroQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("islaHerbivoroQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("islaVoladorQueue", "Actualizar");

                    // Enviar mensaje a la cola de RabbitMQ para visitantes
                    rabbitTemplate.convertAndSend("visitantesQueue", "Actualizar");

                    // Evento cada 10 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 10 == 0) {
                        dinosaurioService.generarEventoMuerteAleatoria();
                    }

                    // Evento de reproducción cada 5 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 5 == 0) {
                        huevoService.crearHuevoAleatorio();
                        logger.info("Evento de reproducción: se ha creado un nuevo huevo.");
                    }

                })
                .subscribe();
    }
}
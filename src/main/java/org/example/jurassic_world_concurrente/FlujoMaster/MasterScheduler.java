// src/main/java/org/example/jurassic_world_concurrente/FlujoMaster/MasterScheduler.java
package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Islas.IslaCarnivoro;
import org.example.jurassic_world_concurrente.Islas.IslaHerbivoro;
import org.example.jurassic_world_concurrente.Islas.IslaVolador;
import org.example.jurassic_world_concurrente.Islas.IslaHuevo;
import org.example.jurassic_world_concurrente.Visitantes.Visitante;
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

    @Autowired
    private IslaCarnivoro islaCarnivoro;

    @Autowired
    private IslaHerbivoro islaHerbivoro;

    @Autowired
    private IslaVolador islaVolador;

    @Autowired
    private IslaHuevo islaHuevo;

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

                    // Evento cada 10 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 10 == 0) {
                        dinosaurioService.generarEventoMuerteAleatoria();
                    }

                    // Evento de reproducción cada 5 tics (excluyendo 0)
                    if (ticsTotales != 0 && ticsTotales % 5 == 0) {
                        huevoService.crearHuevoAleatorio();
                        logger.info("Evento de reproducción: se ha creado un nuevo huevo.");
                    }

                    // Mostrar información de Isla Huevo
                    huevoService.incubarHuevos();
                    islaHuevo.mostrarInformacion();

                    // Mostrar información de Isla Carnívoro
                    islaCarnivoro.mostrarInformacion();

                    // Mostrar información de Isla Herbívoro
                    islaHerbivoro.mostrarInformacion();

                    // Mostrar información de Isla Volador
                    islaVolador.mostrarInformacion();

                    // Incrementar tiempo de visitantes en el parque y eliminar los que han estado más de 2 años
                    visitanteService.incrementarTiempoVisitantes();

                    // Agregar un número aleatorio de visitantes cada año
                    int nuevosVisitantes = random.nextInt(10) + 1;
                    for (int i = 0; i < nuevosVisitantes; i++) {
                        visitanteService.agregarVisitante(new Visitante("Visitante_" + ticsTotales + "_" + i));
                    }

                    // Mostrar total de visitantes
                    logger.info("Total de visitantes: {}", visitanteService.getTotalVisitantes());

                })
                .subscribe();
    }
}
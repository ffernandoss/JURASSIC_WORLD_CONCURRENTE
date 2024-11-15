package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

@Component
public class MasterScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MasterScheduler.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private DinosaurioEstadoService dinosaurioEstadoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private int ticsTotales = 0;
    private Disposable disposable;

    public void iniciarSimulacion() {
        disposable = Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticsTotales++;
                    logger.info("---------------------------Año de la simulación: {}---------------------------", ticsTotales);

                    // Envejecer dinosaurios y verificar muertes por edad
                    dinosaurioService.envejecerDinosaurios();

                    // Verificar salida de dinosaurios enfermos de la enfermería
                    dinosaurioService.verificarSalidaEnfermeria(ticsTotales);

                    // Incubar huevos y verificar eclosión
                    huevoService.incubarHuevos();

                    // Evento de reproducción cada 5 tics
                    if (ticsTotales % 5 == 0) {
                        huevoService.crearHuevoAleatorio();
                        logger.info("Evento de reproducción: se ha creado un nuevo huevo.");
                    }

                    // Evento de muerte aleatoria cada 10 tics
                    if (ticsTotales % 10 == 0) {
                        dinosaurioService.generarEventoMuerteAleatoria();
                    }

                    // Publicar estados actualizados
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");
                    rabbitTemplate.convertAndSend("verificarDinosauriosQueue", "Verificar");

                    // Imprimir estado actual de dinosaurios
                    imprimirEstadoActual();

                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    public void detenerSimulacion() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            logger.info("Simulación detenida.");
        }
    }

    private void imprimirEstadoActual() {
        // Lista de dinosaurios
        List<Dinosaurio> dinosaurios = dinosaurioService.getDinosaurios();
        logger.info("Dinosaurios en el parque: ");
        dinosaurios.forEach(dinosaurio -> logger.info("- {}", dinosaurio));

        // Lista de huevos
        logger.info("Huevos en incubación: ");
        huevoService.getHuevos().forEach(huevo -> logger.info("- {}", huevo));

        // Lista de dinosaurios enfermos
        logger.info("Dinosaurios enfermos:");
        dinosaurioEstadoService.imprimirDinosauriosEnfermos();
    }
}

package org.example.jurassic_world_concurrente.FlujoMaster;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioEstadoService;
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
public class EnfermeriaScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EnfermeriaScheduler.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private DinosaurioEstadoService dinosaurioEstadoService;

    private Disposable disposable;
    private int ticActual = 0;

    public void iniciarEnfermeria() {
        disposable = Flux.interval(Duration.ofSeconds(2))
                .doOnNext(tic -> {
                    ticActual++;
                    logger.info("Tic actual: {}", ticActual);

                    // Verificar salida de dinosaurios enfermos de la enfermería
                    dinosaurioService.verificarSalidaEnfermeria(ticActual);

                    // Obtener dinosaurios enfermos y enviarlos a la cola de enfermería
                    List<Dinosaurio> dinosauriosEnfermos = dinosaurioService.getDinosauriosEnfermos();
                    dinosauriosEnfermos.forEach(dinosaurio -> {
                        if (dinosaurio.isEstaEnfermo()) {
                            logger.info("Dinosaurio enfermo detectado: {}", dinosaurio.getNombre());
                            rabbitTemplate.convertAndSend("enfermeriaQueue", dinosaurio);
                        }
                    });

                    // Actualizar estados de dinosaurios
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");

                    // Mostrar estado actual de dinosaurios enfermos
                    logger.info("Dinosaurios enfermos actualmente en la enfermería:");
                    dinosauriosEnfermos.forEach(dinosaurio -> logger.info(dinosaurio.toString()));

                    // Log de estados actualizados
                    logger.info("Actualización de estados:");
                    logger.info("Estados de dinosaurios enfermos: {}", dinosaurioEstadoService.getEstados());
                })
                .subscribeOn(Schedulers.parallel())
                .subscribe();
    }

    public void detenerEnfermeria() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            logger.info("Enfermería detenida.");
        }
    }
}

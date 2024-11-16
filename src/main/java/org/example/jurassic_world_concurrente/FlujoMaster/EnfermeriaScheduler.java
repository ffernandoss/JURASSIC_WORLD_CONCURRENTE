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
        disposable = Flux.interval(Duration.ofSeconds(4))
                .doOnNext(tic -> {
                    ticActual++;
                    logger.info("Tic actual: {}", ticActual);

                    // Obtener dinosaurios enfermos
                    List<Dinosaurio> dinosauriosEnfermos = dinosaurioService.getDinosauriosEnfermos();

                    synchronized (dinosauriosEnfermos) {
                        // Incrementar tics y verificar salida de la enfermería
                        for (Dinosaurio dinosaurio : dinosauriosEnfermos) {
                            dinosaurio.incrementarTicsEnEnfermeria();
                            logger.info("Dinosaurio {} ha estado {} tics en la enfermería.", dinosaurio.getNombre(), dinosaurio.getTicsEnEnfermeria());
                        }

                        // Verificar si algún dinosaurio debe salir
                        dinosaurioService.verificarSalidaEnfermeria(ticActual);
                    }

                    // Enviar actualización a la cola de estados
                    rabbitTemplate.convertAndSend("actualizarDinosaurioEstadoQueue", "Actualizar");
                    logger.info("Se envió una actualización de estado a la cola.");

                    // Mostrar estado actual de los dinosaurios enfermos
                    synchronized (dinosauriosEnfermos) {
                        if (dinosauriosEnfermos.isEmpty()) {
                            logger.info("No hay dinosaurios enfermos en la enfermería.");
                        } else {
                            logger.info("Dinosaurios enfermos actualmente en la enfermería:");
                            dinosauriosEnfermos.forEach(dinosaurio -> {
                                logger.info("{} ({} tics)", dinosaurio.getNombre(), dinosaurio.getTicsEnEnfermeria());
                            });
                        }
                    }

                    // Log de estados actualizados
                    logger.info("Estados de dinosaurios enfermos actualizados: {}", dinosaurioEstadoService.getEstados());
                })
                .onErrorContinue((error, obj) -> logger.error("Error en el flujo de enfermería: ", error))
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

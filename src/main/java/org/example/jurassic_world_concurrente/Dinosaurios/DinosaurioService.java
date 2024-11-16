// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioService.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class DinosaurioService {
    private static final Logger logger = LoggerFactory.getLogger(DinosaurioService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final List<Dinosaurio> dinosaurios = Collections.synchronizedList(new ArrayList<>());
    private final List<Dinosaurio> dinosauriosEnfermos = Collections.synchronizedList(new ArrayList<>());
    private final List<Disposable> disposables = new ArrayList<>();
    private static final int DURACION_ENFERMERIA = 3; // Tiempo en tics que el dinosaurio permanece enfermo

    public synchronized void envejecerDinosaurios() {
        List<Dinosaurio> dinosauriosParaEliminar = new ArrayList<>();
        synchronized (dinosaurios) {
            dinosaurios.forEach(dinosaurio -> {
                if (dinosaurio.getEdad() < dinosaurio.getMaxEdad()) {
                    dinosaurio.envejecer();
                    logger.info("Dinosaurio {} tiene ahora {} años.", dinosaurio.getNombre(), dinosaurio.getEdad());
                } else {
                    dinosauriosParaEliminar.add(dinosaurio);
                }
            });
        }
        dinosauriosParaEliminar.forEach(this::matarDinosaurio);
    }

    public synchronized void matarDinosaurio(Dinosaurio dinosaurio) {
        logger.info("Dinosaurio {} ha muerto de viejo.", dinosaurio.getNombre());
        dinosaurios.remove(dinosaurio);
        rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
    }

    public synchronized void generarEventoMuerteAleatoria() {
        if (!dinosaurios.isEmpty()) {
            Dinosaurio randomDino = dinosaurios.get(new Random().nextInt(dinosaurios.size()));
            logger.info("Evento de muerte aleatoria: Dinosaurio {} fue asesinado.", randomDino.getNombre());
            dinosaurios.remove(randomDino);
        }
    }

    public synchronized void agregarDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
    }

    public synchronized void suscribirDinosaurio(Dinosaurio dinosaurio) {
        if (!dinosaurios.contains(dinosaurio)) {
            dinosaurios.add(dinosaurio);

        }
    }

    public synchronized void desuscribirDinosaurio(Dinosaurio dinosaurio) {
        if (dinosaurios.contains(dinosaurio)) {
            dinosaurios.remove(dinosaurio);

        }
    }

    public synchronized List<Dinosaurio> getDinosaurios() {
        return new ArrayList<>(dinosaurios);
    }

    public synchronized boolean existeDinosaurioDeTipo(String tipo) {
        return dinosaurios.stream().anyMatch(dino -> dino.getTipo().equalsIgnoreCase(tipo));
    }

    public synchronized void suscribirDinosaurioEnfermo(Dinosaurio dinosaurio, int ticActual) {
        if (dinosaurio.getMaxEdad() - dinosaurio.getEdad() > 3) { // Verificar si tiene más de 3 días de vida restantes
            if (!dinosauriosEnfermos.contains(dinosaurio)) {
                dinosauriosEnfermos.add(dinosaurio);
                dinosaurio.setTicsEnEnfermeria(0); // Reiniciar contador de tics

            }
        }
    }

    public synchronized void desuscribirDinosaurioEnfermo(Dinosaurio dinosaurio) {
        if (dinosauriosEnfermos.contains(dinosaurio)) {
            dinosauriosEnfermos.remove(dinosaurio);
            dinosaurio.resetTicsEnEnfermeria();
            dinosaurio.setEdad(dinosaurio.getEdad() + 3); // Incrementar la edad en 3 años
            logger.info("Dinosaurio {} ha sido curado, tiene ahora {} años y salió de la enfermería.", dinosaurio.getNombre(), dinosaurio.getEdad());
            suscribirDinosaurio(dinosaurio); // Regresar a la lista principal
        }
    }

    public void verificarSalidaEnfermeria(int ticsTotales) {
        List<Dinosaurio> dinosauriosParaSalir = new ArrayList<>();
        synchronized (dinosauriosEnfermos) {
            for (Dinosaurio dinosaurio : dinosauriosEnfermos) {
                if (dinosaurio.getTicsEnEnfermeria() >= DURACION_ENFERMERIA) {
                    dinosaurio.setEstaEnfermo(false);
                    dinosaurio.resetTicsEnEnfermeria();
                    dinosauriosParaSalir.add(dinosaurio);

                }
            }
        }
        dinosauriosParaSalir.forEach(this::desuscribirDinosaurioEnfermo);
    }

    public synchronized List<Dinosaurio> getDinosauriosEnfermos() {
        return new ArrayList<>(dinosauriosEnfermos);
    }
}
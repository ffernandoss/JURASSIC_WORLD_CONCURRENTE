// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioService.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DinosaurioService {
    private static final Logger logger = LoggerFactory.getLogger(DinosaurioService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private List<Dinosaurio> dinosaurios = new ArrayList<>();
    private List<Dinosaurio> dinosauriosEnfermos = new ArrayList<>();

    public void envejecerDinosaurios() {
        // Lógica para envejecer dinosaurios
    }

    public void matarDinosaurio(Dinosaurio dinosaurio) {
        // Lógica para matar dinosaurio
    }

    public void generarEventoMuerteAleatoria() {
        // Lógica para generar evento de muerte aleatoria
    }

    public void agregarDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.add(dinosaurio);
    }

    public void suscribirDinosaurioEnfermo(Dinosaurio dinosaurio) {
        if (!dinosauriosEnfermos.contains(dinosaurio)) {
            dinosauriosEnfermos.add(dinosaurio);
        }
    }

    public void desuscribirDinosaurioEnfermo(Dinosaurio dinosaurio) {
        dinosauriosEnfermos.remove(dinosaurio);
    }

    public void suscribirDinosaurio(Dinosaurio dinosaurio) {
        if (!dinosaurios.contains(dinosaurio)) {
            dinosaurios.add(dinosaurio);
        }
    }

    public void desuscribirDinosaurio(Dinosaurio dinosaurio) {
        dinosaurios.remove(dinosaurio);
    }

    public List<Dinosaurio> getDinosaurios() {
        return dinosaurios;
    }

    public List<Dinosaurio> getDinosauriosEnfermos() {
        return dinosauriosEnfermos;
    }

    public boolean existeDinosaurioDeTipo(String tipo) {
        return dinosaurios.stream().anyMatch(dino -> dino.getTipo().equalsIgnoreCase(tipo));
    }
}
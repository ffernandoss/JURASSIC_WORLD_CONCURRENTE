// src/main/java/org/example/jurassic_world_concurrente/Dinosaurios/DinosaurioEstadoService.java
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DinosaurioEstadoService {
    private List<DinosaurioEstado> estados = new ArrayList<>();

    @Autowired
    private DinosaurioService dinosaurioService;

    public void actualizarEstados(List<Dinosaurio> dinosaurios) {
        estados.clear();
        List<Dinosaurio> dinosauriosCopy = new ArrayList<>(dinosaurios); // Crear una copia de la lista
        for (Dinosaurio dinosaurio : dinosauriosCopy) {
            DinosaurioEstado estado = new DinosaurioEstado(dinosaurio);
            estado.actualizarEstado();
            estados.add(estado);
            if (estado.isEstaEnfermo()) {
                dinosaurioService.suscribirDinosaurioEnfermo(dinosaurio);
                dinosaurioService.desuscribirDinosaurio(dinosaurio);
            } else {
                dinosaurioService.desuscribirDinosaurioEnfermo(dinosaurio);
                dinosaurioService.suscribirDinosaurio(dinosaurio);
            }
        }
    }

    public List<DinosaurioEstado> getEstados() {
        return estados;
    }

    public List<DinosaurioEstado> getDinosauriosEnfermos() {
        return estados.stream()
                .filter(DinosaurioEstado::isEstaEnfermo)
                .collect(Collectors.toList());
    }

    public void imprimirDinosauriosEnfermos() {
        List<DinosaurioEstado> enfermos = getDinosauriosEnfermos();
        if (enfermos.isEmpty()) {
            System.out.println("No hay dinosaurios enfermos.");
        } else {
            System.out.println("LOS FOKIN DINOSAURIOS:");
            enfermos.forEach(System.out::println);
        }
    }
}
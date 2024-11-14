package org.example.jurassic_world_concurrente.Dinosaurios;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DinosaurioEstadoService {
    private List<DinosaurioEstado> estados = new ArrayList<>();

    public void actualizarEstados(List<Dinosaurio> dinosaurios) {
        estados.clear();
        for (Dinosaurio dinosaurio : dinosaurios) {
            DinosaurioEstado estado = new DinosaurioEstado(dinosaurio);
            estado.actualizarEstado();
            estados.add(estado);
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
package org.example.jurassic_world_concurrente.Dinosaurios;

import org.springframework.stereotype.Component;

@Component
public class FabricaDinosaurios {
    public Dinosaurio crearDinosaurio(String tipo) {
        switch (tipo.toLowerCase()) {
            case "carnivoro":
                return new Carnivoro();
            case "herbivoro":
                return new Herbivoro();
            case "volador":
                return new Volador();
            default:
                throw new IllegalArgumentException("Tipo de dinosaurio no soportado.");
        }
    }
}
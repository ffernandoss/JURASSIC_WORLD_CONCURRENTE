package org.example.jurassic_world_concurrente.Huevos;

import org.springframework.stereotype.Component;

@Component
public class FabricaHuevos {
    public Huevo crearHuevo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "carnivoro":
                return new Huevo("Carnivoro", 3);
            case "herbivoro":
                return new Huevo("Herbivoro", 2);
            case "volador":
                return new Huevo("Volador", 1);
            default:
                throw new IllegalArgumentException("Tipo de huevo no soportado.");
        }
    }
}
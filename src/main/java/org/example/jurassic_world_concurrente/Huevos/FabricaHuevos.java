package org.example.jurassic_world_concurrente.Huevos;

public class FabricaHuevos {
    public Huevo crearHuevo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "carnivoro":
                return new Huevo("Carnivoro", 10);
            case "herbivoro":
                return new Huevo("Herbivoro", 15);
            case "volador":
                return new Huevo("Volador", 8);
            default:
                throw new IllegalArgumentException("Tipo de huevo no soportado.");
        }
    }
}
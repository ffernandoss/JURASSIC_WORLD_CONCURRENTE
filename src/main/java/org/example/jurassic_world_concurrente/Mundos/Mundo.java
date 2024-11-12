package org.example.jurassic_world_concurrente.Mundos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import java.util.List;

public interface Mundo {
    void addDinosaurio(Dinosaurio dinosaurio);
    void removeDinosaurio(Dinosaurio dinosaurio);
    int getId();
    List<Dinosaurio> getDinosaurios(); // Add this method
}
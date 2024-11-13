package org.example.jurassic_world_concurrente.Mundos;

import org.example.jurassic_world_concurrente.Visitantes.Visitante;
import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;

public interface Mundo {
    void addDinosaurio(Dinosaurio dinosaurio);
    void removeDinosaurio(Dinosaurio dinosaurio);
    void addVisitante(Visitante visitante);
    void removeVisitante(Visitante visitante);
    int getVisitanteCount();
    int getId();
}
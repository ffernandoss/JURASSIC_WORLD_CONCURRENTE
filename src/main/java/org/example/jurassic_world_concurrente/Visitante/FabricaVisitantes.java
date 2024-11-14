package org.example.jurassic_world_concurrente.Visitante;

import org.example.jurassic_world_concurrente.DataBase.VisitanteEnti;
import org.springframework.stereotype.Component;

@Component
public class FabricaVisitantes {
    public VisitanteEnti crearVisitante(String nombre, String ubicacion) {
        VisitanteEnti visitante = new VisitanteEnti();
        visitante.setNombre(nombre);
        visitante.setUbicacion(ubicacion);
        return visitante;
    }
}
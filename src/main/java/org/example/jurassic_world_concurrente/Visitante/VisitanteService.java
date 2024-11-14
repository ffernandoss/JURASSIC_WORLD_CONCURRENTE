// src/main/java/org/example/jurassic_world_concurrente/Visitante/VisitanteService.java
package org.example.jurassic_world_concurrente.Visitante;

import org.example.jurassic_world_concurrente.DataBase.VisitanteEnti;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitanteService {

    private List<VisitanteEnti> visitantes = new ArrayList<>();

    public List<VisitanteEnti> obtenerTodosLosVisitantes() {
        return new ArrayList<>(visitantes);
    }

    public void agregarVisitante(VisitanteEnti visitante) {
        visitantes.add(visitante);
    }

    public void eliminarVisitante(Long id) {
        visitantes.removeIf(visitante -> visitante.getId().equals(id));
    }
}
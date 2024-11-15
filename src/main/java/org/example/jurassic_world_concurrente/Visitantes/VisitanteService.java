// src/main/java/org/example/jurassic_world_concurrente/Visitantes/VisitanteService.java
package org.example.jurassic_world_concurrente.Visitantes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VisitanteService {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteService.class);
    private List<Visitante> visitantes = new ArrayList<>();
    private static final String[] ISLAS = {"Carnivoro", "Herbivoro", "Volador"};
    private Random random = new Random();

    public void agregarVisitante(Visitante visitante) {
        visitantes.add(visitante);
        logger.info("Nuevo visitante agregado: Visitante_{} a la isla {}", visitante.getId(), visitante.getIslaAsignada());
    }

    public void incrementarTiempoVisitantes() {
        List<Visitante> visitantesParaEliminar = new ArrayList<>();
        for (Visitante visitante : visitantes) {
            visitante.incrementarTiempoEnParque();
            if (visitante.getTiempoEnParque() >= 2) {
                visitantesParaEliminar.add(visitante);
                logger.info("Visitante {} ha abandonado la isla {} y el parque.", visitante.getId(), visitante.getIslaAsignada());
            }
        }
        visitantes.removeAll(visitantesParaEliminar);
    }

    public List<Visitante> getVisitantes() {
        return visitantes;
    }

    public int getTotalVisitantes() {
        return visitantes.size();
    }

    public String asignarIslaAleatoria() {
        return ISLAS[random.nextInt(ISLAS.length)];
    }
}
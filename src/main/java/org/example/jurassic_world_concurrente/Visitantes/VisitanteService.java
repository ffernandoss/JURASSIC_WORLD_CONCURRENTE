// src/main/java/org/example/jurassic_world_concurrente/Visitantes/VisitanteService.java
package org.example.jurassic_world_concurrente.Visitantes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitanteService {
    private static final Logger logger = LoggerFactory.getLogger(VisitanteService.class);
    private List<Visitante> visitantes = new ArrayList<>();

    public void agregarVisitante(Visitante visitante) {
        visitantes.add(visitante);
        logger.info("Nuevo visitante agregado: {}", visitante);
    }

    public List<Visitante> getVisitantes() {
        return visitantes;
    }

    public int getTotalVisitantes() {
        return visitantes.size();
    }
}
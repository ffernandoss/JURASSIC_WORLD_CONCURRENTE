package org.example.jurassic_world_concurrente.Islas;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Visitantes.VisitanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class IslaHerbivoro {
    private static final Logger logger = LoggerFactory.getLogger(IslaHerbivoro.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private VisitanteService visitanteService;

    public void mostrarInformacion() {
        String infoDinosaurios = dinosaurioService.getDinosaurios().stream()
                .filter(dino -> "Herbivoro".equalsIgnoreCase(dino.getTipo()))
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        String infoVisitantes = visitanteService.getVisitantes().stream()
                .filter(visitante -> visitante != null && "Herbivoro".equalsIgnoreCase(visitante.getIslaAsignada()))
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Información de la Isla Herbívoro:\nDinosaurios [{}]\nVisitantes [{}]", infoDinosaurios, infoVisitantes);
    }
}
// src/main/java/org/example/jurassic_world_concurrente/Huevos/HuevoService.java
package org.example.jurassic_world_concurrente.Huevos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

@Service
public class HuevoService {
    private static final Logger logger = LoggerFactory.getLogger(HuevoService.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    private Queue<Huevo> colaHuevos = new LinkedList<>();
    private static final String[] TIPOS_DINOSAURIOS = {"Carnivoro", "Herbivoro", "Volador"};
    private Random random = new Random();

    public void incubarHuevos() {
        List<Huevo> huevosEclosionados = new LinkedList<>();
        colaHuevos.forEach(huevo -> {
            if (!"Eclosionado".equals(huevo.getEstado())) {
                huevo.incubar();
                logger.info("Huevo de tipo {} está incubando. Tiempo de incubación: {} días", huevo.getTipo(), huevo.getTiempoIncubacion());
            } else {
                Dinosaurio dinosaurio = huevo.transformarADinosaurio();
                dinosaurioService.agregarDinosaurio(dinosaurio);
                logger.info("Huevo de tipo {} ha eclosionado, creando dinosaurio {}", huevo.getTipo(), dinosaurio.getNombre());
                huevosEclosionados.add(huevo);
            }
        });
        colaHuevos.removeAll(huevosEclosionados);
    }

    public Huevo crearHuevo(String tipo) {
        Huevo huevo = new Huevo(tipo, determinarPeriodoIncubacion(tipo));
        colaHuevos.add(huevo);
        logger.info("Generado nuevo huevo de tipo {}", tipo);
        return huevo;
    }

    public Huevo crearHuevoAleatorio() {
        String tipoAleatorio = TIPOS_DINOSAURIOS[random.nextInt(TIPOS_DINOSAURIOS.length)];
        return crearHuevo(tipoAleatorio);
    }

    private int determinarPeriodoIncubacion(String tipo) {
        switch (tipo.toLowerCase()) {
            case "carnivoro": return 3;
            case "herbivoro": return 2;
            case "volador": return 1;
            default: throw new IllegalArgumentException("Tipo de huevo no soportado.");
        }
    }

    public List<Huevo> getHuevos() {
        return new LinkedList<>(colaHuevos);
    }

    public boolean existeHuevoDeTipo(String tipo) {
        return colaHuevos.stream().anyMatch(huevo -> huevo.getTipo().equalsIgnoreCase(tipo));
    }
}
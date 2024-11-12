package org.example.jurassic_world_concurrente;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Mundos.Mundo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class Evento {
    private static final Logger logger = LoggerFactory.getLogger(Evento.class);
    private final Random random = new Random();

    @Autowired
    private List<Mundo> mundos;

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void matar() {
        Mundo mundo = mundos.get(random.nextInt(mundos.size()));
        List<Dinosaurio> dinosaurios = mundo.getDinosaurios();
        if (!dinosaurios.isEmpty()) {
            Dinosaurio dinosaurio = dinosaurios.get(random.nextInt(dinosaurios.size()));
            mundo.removeDinosaurio(dinosaurio);
            dinosaurio.morirAsesinado();
            rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
        }
    }

    public void reproducirse() {
        String[] tipos = {"carnivoro", "herbivoro", "volador"};
        String tipo = tipos[random.nextInt(tipos.length)];
        Huevo huevo = fabricaHuevos.crearHuevo(tipo);
        huevoService.gestionarIncubacionHuevos(List.of(huevo)).subscribe();
        logger.info("Huevo de tipo {} ha sido añadido al flujo.", tipo);
    }
}
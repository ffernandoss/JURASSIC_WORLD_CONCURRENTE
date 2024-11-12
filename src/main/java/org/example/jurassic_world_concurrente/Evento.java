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

    public void matarYReproducir() {
        try {
            matar();
        } catch (Exception e) {
            logger.error("Error al matar dinosaurio: ", e);
        }

        try {
            reproducir();
        } catch (Exception e) {
            logger.error("Error al reproducir dinosaurio: ", e);
        }
    }

private void matar() {
    Dinosaurio dinosaurio = null;
    Mundo mundo = null;

    // Iterate through all mundos to find a dinosaur to kill
    for (Mundo m : mundos) {
        List<Dinosaurio> dinosaurios = m.getDinosaurios();
        if (!dinosaurios.isEmpty()) {
            mundo = m;
            dinosaurio = dinosaurios.get(random.nextInt(dinosaurios.size()));
            break;
        }
    }

    if (dinosaurio != null && mundo != null) {
        mundo.removeDinosaurio(dinosaurio);
        dinosaurio.morirAsesinado();
        logger.info("Dinosaurio {} ha sido asesinado.", dinosaurio.getNombre());
        rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());
    } else {
        logger.warn("No hay dinosaurios disponibles para matar en ningún mundo.");
    }
}

    private void reproducir() {
        String[] tipos = {"carnivoro", "herbivoro", "volador"};
        String tipo = tipos[random.nextInt(tipos.length)];
        Huevo huevo = fabricaHuevos.crearHuevo(tipo);
        huevoService.gestionarIncubacionHuevos(List.of(huevo)).subscribe(
            success -> logger.info("Un nuevo huevo de tipo {} ha sido creado por reproducción.", tipo),
            error -> logger.error("Error al gestionar la incubación del huevo: ", error)
        );
    }
}
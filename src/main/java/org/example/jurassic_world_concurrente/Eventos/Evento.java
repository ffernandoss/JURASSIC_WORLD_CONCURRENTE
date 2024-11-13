package org.example.jurassic_world_concurrente.Eventos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Mundos.MundoGeneral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class Evento {
    private static final Logger logger = LoggerFactory.getLogger(Evento.class);

    @Autowired
    private MundoGeneral mundoGeneral;

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private List<Dinosaurio> dinosaurDeathQueue = new ArrayList<>();

    public void matarYReproducir() {
        List<Dinosaurio> dinosaurios = mundoGeneral.getDinosaurios();
        if (dinosaurios.isEmpty()) {
            logger.warn("No hay dinosaurios en el Mundo General para matar y reproducir.");
            return;
        }

        // Select a random dinosaur
        Random random = new Random();
        Dinosaurio dinosaurio = dinosaurios.get(random.nextInt(dinosaurios.size()));
        String tipoDinosaurio = dinosaurio.getTipo();

        // Log the type of the dinosaur that was killed
        logger.info("Se ha asesinado a un dinosaurio de tipo: {}", tipoDinosaurio);
        logger.info("Dinosaurio {} ha sido asesinado.", dinosaurio.getNombre());

        // Send a message to the dinosaurDeathQueue
        rabbitTemplate.convertAndSend("dinosaurDeathQueue", dinosaurio.getTipo());

        // Add the dinosaur to the dinosaurDeathQueue list
        dinosaurDeathQueue.add(dinosaurio);

        // Remove the dinosaur from MundoGeneral
        mundoGeneral.removeDinosaurio(dinosaurio);

        // Generate a new egg of the same type
        Huevo nuevoHuevo = fabricaHuevos.crearHuevo(tipoDinosaurio);

        // Log the type of the dinosaur that was reproduced
        logger.info("Se ha reproducido un dinosaurio de tipo: {}", tipoDinosaurio);

        // Subscribe to the incubation process of the new egg
        huevoService.gestionarIncubacionHuevos(List.of(nuevoHuevo))
                .subscribe(huevo -> {
                    logger.info("Nuevo huevo de tipo {} está {} con {} días de incubación.", huevo.getTipo(), huevo.getEstado(), huevo.getTiempoIncubacion());
                });
    }
}
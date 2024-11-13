package org.example.jurassic_world_concurrente.Eventos;

import org.example.jurassic_world_concurrente.Dinosaurios.Dinosaurio;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.example.jurassic_world_concurrente.Mundos.MundoGeneral;
import org.example.jurassic_world_concurrente.Mundos.MundoCarnivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoHerbivoros;
import org.example.jurassic_world_concurrente.Mundos.MundoVoladores;
import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class Evento {
    private static final Logger logger = LoggerFactory.getLogger(Evento.class);

    @Autowired
    private MundoGeneral mundoGeneral;

    @Autowired
    private MundoCarnivoros mundoCarnivoros;

    @Autowired
    private MundoHerbivoros mundoHerbivoros;

    @Autowired
    private MundoVoladores mundoVoladores;

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private HuevoService huevoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DinosaurioService dinosaurioService;

    public MundoGeneral getMundoGeneral() {
        return mundoGeneral;
    }

    public void matarYReproducir() {
        List<Dinosaurio> dinosaurios = mundoGeneral.getDinosaurios();
        if (dinosaurios.isEmpty()) {
            logger.warn("No hay dinosaurios en el Mundo General para matar y reproducir.");
            return;
        }

        // Select a random dinosaur
        Random random = new Random();
        Dinosaurio dinosaurio = dinosaurios.get(random.nextInt(dinosaurios.size()));

        // Kill the dinosaur
        matarDinosaurio(dinosaurio);

        // Reproduce a new dinosaur
        reproducirDinosaurio();
    }

public void matarDinosaurio(Dinosaurio dinosaurio) {
    String tipoDinosaurio = dinosaurio.getTipo();

    // Log the type of the dinosaur that was killed
    logger.info("Se ha asesinado a un dinosaurio de tipo: {}", tipoDinosaurio);
    logger.info("Dinosaurio {} ha sido asesinado.", dinosaurio.getNombre());

    // Unsubscribe the dinosaur from its current Flux
    dinosaurioService.unsubscribeDinosaur(dinosaurio);

    // Remove the dinosaur from MundoGeneral
    mundoGeneral.removeDinosaurio(dinosaurio);

    // Follow the same process as when a dinosaur dies
    switch (dinosaurio.getTipo().toLowerCase()) {
        case "carnivoro":
            mundoCarnivoros.removeDinosaurio(dinosaurio);
            logger.info("Total carnivoros: {}", mundoCarnivoros.getContadorCarnivoros());
            break;
        case "herbivoro":
            mundoHerbivoros.removeDinosaurio(dinosaurio);
            logger.info("Total herbivoros: {}", mundoHerbivoros.getContadorHerbivoros());
            break;
        case "volador":
            mundoVoladores.removeDinosaurio(dinosaurio);
            logger.info("Total voladores: {}", mundoVoladores.getContadorVoladores());
            break;
        default:
            logger.warn("Tipo de dinosaurio desconocido: {}", tipoDinosaurio);
            break;
    }
}

    public void reproducirDinosaurio() {
        // List of possible dinosaur types
        List<String> tiposDinosaurios = Arrays.asList("carnivoro", "herbivoro", "volador");

        // Select a random type
        Random random = new Random();
        String tipoDinosaurio = tiposDinosaurios.get(random.nextInt(tiposDinosaurios.size()));

        // Generate a new egg of the selected type
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
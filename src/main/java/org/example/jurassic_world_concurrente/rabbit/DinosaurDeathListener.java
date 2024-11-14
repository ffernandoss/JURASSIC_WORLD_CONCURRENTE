package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DinosaurDeathListener {

    private static final Logger logger = LoggerFactory.getLogger(DinosaurDeathListener.class);

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private HuevoService huevoService;

    @RabbitListener(queues = "dinosaurDeathQueue")
    public void handleDinosaurDeath(String tipo) {
        Huevo nuevoHuevo = fabricaHuevos.crearHuevo(tipo);
        huevoService.crearHuevo(tipo);
        logger.info("Nuevo huevo de tipo {} generado tras muerte de dinosaurio", tipo);
    }
}

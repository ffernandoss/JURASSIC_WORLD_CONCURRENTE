package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Dinosaurios.DinosaurioService;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificarDinosauriosListener {

    private static final Logger logger = LoggerFactory.getLogger(VerificarDinosauriosListener.class);

    @Autowired
    private DinosaurioService dinosaurioService;

    @Autowired
    private HuevoService huevoService;

    @RabbitListener(queues = "verificarDinosauriosQueue")
    public void handleVerificarDinosaurios(String message) {
        String[] tiposDinosaurios = {"Carnivoro", "Herbivoro", "Volador"};
        for (String tipo : tiposDinosaurios) {
            if (!dinosaurioService.existeDinosaurioDeTipo(tipo) && !huevoService.existeHuevoDeTipo(tipo)) {
                huevoService.crearHuevo(tipo);
                logger.info("No hay dinosaurios ni huevos de tipo {}. Se ha creado un nuevo huevo.", tipo);
            }
        }
    }
}
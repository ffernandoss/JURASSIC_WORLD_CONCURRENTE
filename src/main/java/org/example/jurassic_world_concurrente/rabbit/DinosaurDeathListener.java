package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        huevoService.gestionarIncubacionHuevos(List.of(nuevoHuevo))
                .subscribe(huevo -> {
                    logger.info("Nuevo huevo de tipo {} está {} con {} días de incubación.", huevo.getTipo(), huevo.getEstado(), huevo.getTiempoIncubacion());
                });
    }
}
package org.example.jurassic_world_concurrente;
import org.example.jurassic_world_concurrente.Huevos.FabricaHuevos;
import org.example.jurassic_world_concurrente.Huevos.Huevo;
import org.example.jurassic_world_concurrente.Huevos.HuevoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DinosaurDeathListener {

    @Autowired
    private FabricaHuevos fabricaHuevos;

    @Autowired
    private HuevoService huevoService;

    @RabbitListener(queues = "dinosaurDeathQueue")
    public void handleDinosaurDeath(String tipo) {
        Huevo nuevoHuevo = fabricaHuevos.crearHuevo(tipo);
        huevoService.gestionarIncubacionHuevos(List.of(nuevoHuevo))
                .subscribe(huevo -> {
                    System.out.println("Nuevo huevo de tipo " + huevo.getTipo() + " está " + huevo.getEstado() + " con " + huevo.getTiempoIncubacion() + " días de incubación.");
                });
    }
}
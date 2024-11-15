// src/main/java/org/example/jurassic_world_concurrente/rabbit/IslasListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Islas.IslaCarnivoro;
import org.example.jurassic_world_concurrente.Islas.IslaHerbivoro;
import org.example.jurassic_world_concurrente.Islas.IslaVolador;
import org.example.jurassic_world_concurrente.Islas.IslaHuevo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IslasListener {

    private static final Logger logger = LoggerFactory.getLogger(IslasListener.class);

    @Autowired
    private IslaCarnivoro islaCarnivoro;

    @Autowired
    private IslaHerbivoro islaHerbivoro;

    @Autowired
    private IslaVolador islaVolador;

    @Autowired
    private IslaHuevo islaHuevo;

    @RabbitListener(queues = "islaHuevoQueue")
    public void handleIslaHuevo(String message) {
        islaHuevo.mostrarInformacion();
    }

    @RabbitListener(queues = "islaCarnivoroQueue")
    public void handleIslaCarnivoro(String message) {
        islaCarnivoro.mostrarInformacion();
    }

    @RabbitListener(queues = "islaHerbivoroQueue")
    public void handleIslaHerbivoro(String message) {
        islaHerbivoro.mostrarInformacion();
    }

    @RabbitListener(queues = "islaVoladorQueue")
    public void handleIslaVolador(String message) {
        islaVolador.mostrarInformacion();
    }
}
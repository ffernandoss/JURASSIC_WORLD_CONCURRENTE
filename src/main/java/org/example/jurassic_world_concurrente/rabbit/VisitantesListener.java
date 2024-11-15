// src/main/java/org/example/jurassic_world_concurrente/rabbit/VisitantesListener.java
package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Visitantes.VisitanteService;
import org.example.jurassic_world_concurrente.Visitantes.Visitante;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VisitantesListener {

    private static final Logger logger = LoggerFactory.getLogger(VisitantesListener.class);

    @Autowired
    private VisitanteService visitanteService;

    private Random random = new Random();

    @RabbitListener(queues = "visitantesQueue")
    public void handleVisitantes(String message) {
        // Incrementar tiempo de visitantes en el parque y eliminar los que han estado más de 2 años
        visitanteService.incrementarTiempoVisitantes();

        // Agregar un número aleatorio de visitantes cada año
        int nuevosVisitantes = random.nextInt(10) + 1;
        for (int i = 0; i < nuevosVisitantes; i++) {
            String islaAsignada = visitanteService.asignarIslaAleatoria();
            visitanteService.agregarVisitante(new Visitante("Visitante_" + System.currentTimeMillis() + "_" + i, islaAsignada));
        }

        // Mostrar total de visitantes
        logger.info("Total de visitantes: {}", visitanteService.getTotalVisitantes());
    }
}
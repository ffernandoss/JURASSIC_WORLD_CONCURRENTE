// src/main/java/org/example/jurassic_world_concurrente/rabbit/RabbitMQConfig.java
package org.example.jurassic_world_concurrente.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue dinosaurDeathQueue() {
        return new Queue("dinosaurDeathQueue", false);
    }

    @Bean
    public Queue verificarDinosauriosQueue() {
        return new Queue("verificarDinosauriosQueue", false);
    }

    @Bean
    public Queue actualizarDinosaurioEstadoQueue() {
        return new Queue("actualizarDinosaurioEstadoQueue", false);
    }
}
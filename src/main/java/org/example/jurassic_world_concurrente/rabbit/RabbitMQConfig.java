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
    public Queue worldChangeQueue() {
        return new Queue("worldChangeQueue", false);
    }
}
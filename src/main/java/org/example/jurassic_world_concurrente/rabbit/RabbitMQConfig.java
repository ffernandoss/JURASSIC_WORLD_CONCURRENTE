// src/main/java/org/example/jurassic_world_concurrente/rabbit/RabbitMQConfig.java
package org.example.jurassic_world_concurrente.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    @Bean
    public Queue enfermeriaQueue() {
        return new Queue("enfermeriaQueue", false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
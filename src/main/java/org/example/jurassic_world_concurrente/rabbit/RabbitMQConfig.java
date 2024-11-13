package org.example.jurassic_world_concurrente.rabbit;

import org.example.jurassic_world_concurrente.Visitantes.Visitante;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<DirectMessageListenerContainer> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        return new RabbitListenerContainerFactory<DirectMessageListenerContainer>() {
            @Override
            public DirectMessageListenerContainer createListenerContainer(RabbitListenerEndpoint endpoint) {
                DirectMessageListenerContainer container = new DirectMessageListenerContainer();
                container.setConnectionFactory(connectionFactory);
                container.setMessageListener(new MessageListenerAdapter(messageConverter()));
                return container;
            }
        };
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Queue dinosaurDeathQueue() {
        return new Queue("dinosaurDeathQueue", false);
    }

    @Bean
    public Queue worldChangeQueue() {
        return new Queue("worldChangeQueue", false);
    }

    @Bean
    public DirectExchange visitantesExchange() {
        return new DirectExchange("visitantesExchange");
    }

    @Bean
    public Queue mundoCarnivorosQueue() {
        return new Queue("mundoCarnivorosQueue", false);
    }

    @Bean
    public Queue mundoHerbivorosQueue() {
        return new Queue("mundoHerbivorosQueue", false);
    }

    @Bean
    public Queue mundoVoladoresQueue() {
        return new Queue("mundoVoladoresQueue", false);
    }

    @Bean
    public Binding bindingMundoCarnivoros(DirectExchange visitantesExchange, Queue mundoCarnivorosQueue) {
        return BindingBuilder.bind(mundoCarnivorosQueue).to(visitantesExchange).with("mundoCarnivoros");
    }

    @Bean
    public Binding bindingMundoHerbivoros(DirectExchange visitantesExchange, Queue mundoHerbivorosQueue) {
        return BindingBuilder.bind(mundoHerbivorosQueue).to(visitantesExchange).with("mundoHerbivoros");
    }

    @Bean
    public Binding bindingMundoVoladores(DirectExchange visitantesExchange, Queue mundoVoladoresQueue) {
        return BindingBuilder.bind(mundoVoladoresQueue).to(visitantesExchange).with("mundoVoladores");
    }
}
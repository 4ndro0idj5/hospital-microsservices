package com.hospital.notificacao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String CONSULTA_NOTIFICACAO_QUEUE = "notificacao-consulta";

    @Bean
    public Queue notificacaoQueue() {
        return new Queue(CONSULTA_NOTIFICACAO_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

package br.com.southsystem.importer.config.broker;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILE_IMPORT_EXCHANGE = "fileImportExchange";
    public static final String FILE_IMPORT_QUEUE = "fileImportQueue";
    public static final String FILE_IMPORT_BINDING = "fileImportBinding";

    @Bean
    Queue fileImportQueue() {
        return new Queue(FILE_IMPORT_QUEUE, true);
    }

    @Bean
    Exchange fileImportExchange() {
        return ExchangeBuilder.directExchange(FILE_IMPORT_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    Binding fileImportBinding() {
        return BindingBuilder
                .bind(fileImportQueue())
                .to(fileImportExchange())
                .with(FILE_IMPORT_BINDING)
                .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}

package com.daoyintech.entities.activemq;

import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

/**
 * Created by xuzhaolin on 2017/2/16.
 */
@Configuration
public class JMSTemplateConfig {
    @Bean
    public JmsListenerContainerFactory<?> gameFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer containerFactoryConfigurer){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        containerFactoryConfigurer.configure(factory,connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}

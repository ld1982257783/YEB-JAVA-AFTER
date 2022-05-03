package com.xxxx.mail.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailMqQueue {


    @Autowired
    private RabbitTemplate rabbitTemplate;

}

package com.xxxx.server.config.rabbitMq;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.constants.MailConstants;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.servive.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);
    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;


    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调 确认消息是否到达broker
         * data 消息的唯一标识
         * ack 确认结果
         * cause 失败原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                LOGGER.info("{}====消息发送成功",msgId);
                //更新数据库消息状态
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",1).eq("msgId",msgId));
            }else{
                LOGGER.error("{}====消息发送失败",msgId);

            }

        });

        /**
         * 消息失败回调 比如 router不到 queue时
         * msg : 消息主体
         * repCode : 相应码
         * repText : 相应描述
         * exchange : 交换机
         * routingKey : 路由键
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error("{}====消息发送queue失败",msg.getBody());

        });

        return rabbitTemplate;
    }

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME,true,false,false);
    }


    @Bean
    public Exchange stockEventExchange(){
        return new TopicExchange(MailConstants.MAIL_EXCHANGE_NAME,true,false);
    }

    @Bean
    public Binding stockReleaseBinding(){
        return new Binding(MailConstants.MAIL_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                MailConstants.MAIL_EXCHANGE_NAME,
                "stock.mail.#",null);
    }




}

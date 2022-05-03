package com.xxxx.mail.listener;

import com.rabbitmq.client.Channel;
import com.xxxx.server.constants.MailConstants;
import com.xxxx.server.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;


@Component
public class MainReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;


    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        Employee employee = (Employee) message.getPayload();
        //获取消息头部
        MessageHeaders headers = message.getHeaders();
        //获取消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //获取messageId
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if(hashOperations.entries("mail_log").containsKey(msgId)){
                LOGGER.error("消息已经被消费=========={}"+msgId);
                /**
                 * 手动确认消息
                 * tag 消息序号
                 * multiple 是否确认多条
                 */
                channel.basicAck(tag,false);
                return;
            }

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            //设置发件人
            helper.setFrom(mailProperties.getUsername());
            //设置收件人
            helper.setTo(employee.getEmail());
            //设置标题
            helper.setSubject("入职欢迎邮件");
            //设置日期
            helper.setSentDate(new Date());
            //设置邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("jobLevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            //设置文本进去 将html转换为能看懂的
            helper.setText(mail,true);
            //发送邮件
            javaMailSender.send(msg);

            LOGGER.info("邮件发送成功");
            //将消息id 存入redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认消息消费成功
            channel.basicAck(tag,false);
        } catch (Exception e) {
            /**
             * 手动确认消息
             * tag : 消息序号
             * multiple 是否确认多条
             * requeue : 消息没有消费成功 是否需要退回到队列
             */
            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            LOGGER.error("邮件发送失败======》{}"+e.getMessage());
        }
    }

}

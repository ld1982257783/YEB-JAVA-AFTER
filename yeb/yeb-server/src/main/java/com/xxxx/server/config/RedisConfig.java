package com.xxxx.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //String 类型 key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //String 类型 value 序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //hash 类型 key 序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash 类型 value 序列化器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //放入redis工厂进行创建
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}

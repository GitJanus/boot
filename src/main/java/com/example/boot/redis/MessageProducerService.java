package com.example.boot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {
    @Autowired
    private RedisTemplate<String, MessageEntity> redisTemplate;

    @Value("${redis.queue.key}")
    private String queueKey;

    public Long sendMeassage(MessageEntity message) {
        System.out.println("发送了" + message);
        return redisTemplate.opsForList().leftPush(queueKey, message);
    }

}


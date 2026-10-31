package com.miniProject.AeroScale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@SpringBootTest
public class test {

    @Autowired
    private RedisTemplate<String, Object> template;

    @Test
    public void tessting() {

        student studen = new student("shadab", 19, Instant.now());

        template.opsForValue().set("student1", studen);
        student res = (student) template.opsForValue().get("student1");
        System.out.println("yes");
    }
}

package com.nei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("setRedisValue")
    public void setRedisValue(String key, @RequestParam(required = false, defaultValue = "") String value) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
    }

    @GetMapping("getRedisValue")
    public String getRedisValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

}

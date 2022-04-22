package com.example.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;

@Service
public class BucketServiceImpl implements  BucketService {

    private final static String KEY = "bucket";

    public DefaultRedisScript<Boolean> redisScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("RateLimiter.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @PostConstruct
    public void generateBucket() {
        DefaultRedisScript<Boolean> redisScript = redisScript();
        Boolean result = this.redisTemplate.execute(redisScript, KEY, 1, System.currentTimeMillis(), 500, 1000, 10);
    }


    @Resource
    private RedisTemplate<String, Object> redisTemplate;



    @Override
    public void acquire() {

    }
}

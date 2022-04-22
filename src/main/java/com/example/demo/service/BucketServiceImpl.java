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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class BucketServiceImpl implements  BucketService {

    private final static String KEY = "bucket";

    public DefaultRedisScript<Integer> redisScript() {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("bucket.lua")));
        redisScript.setResultType(Integer.class);
        return redisScript;
    }




    @Resource
    private RedisTemplate<String, Object> redisTemplate;



    @Override
    public int acquire() {
        DefaultRedisScript<Integer> redisScript = redisScript();
        List<String> list = new ArrayList<>();
        list.add(KEY);
        Integer result = this.redisTemplate.execute(redisScript, list, 1000);
        return result;
    }
}

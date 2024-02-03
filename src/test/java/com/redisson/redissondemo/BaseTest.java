package com.redisson.redissondemo;

import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author gengya.yuan
 * @since 2023-12-28 10:50
 */
public class BaseTest {

    /**
     * 同步阻塞客户端
     */
    @Autowired
    RedissonClient redisson;
    /**
     * RxJava响应式编程客户端
     */
    @Autowired
    RedissonRxClient redissonRxClient;
    /**
     * Reactor响应式编程客户端
     */
    @Autowired
    RedissonReactiveClient redissonReactiveClient;
    /**
     * 普通的RedisTemplate
     */
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    ReactiveRedisTemplate<Object,Object> reactiveRedisTemplate;

    public void sleep(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

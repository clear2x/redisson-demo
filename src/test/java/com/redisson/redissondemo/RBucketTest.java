package com.redisson.redissondemo;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RBucketReactive;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * key - value
 * @author gengya.yuan
 * @since 2023-12-11 10:28
 */
@SpringBootTest
public class RBucketTest extends BaseTest{

    private static final String value = "hello world  " + DateUtil.formatAsDatetime(new Date());

    @BeforeEach
    void before() {
        RBucket<Object> bucket = this.redisson.getBucket("test.user");
        bucket.set(value, Duration.of(200, ChronoUnit.SECONDS));
        RBucket<Object> expireBucket = this.redisson.getBucket("test.user.expire");
        expireBucket.setIfAbsent("hello world  " + DateUtil.formatAsDatetime(new Date()), Duration.of(20, ChronoUnit.SECONDS));
    }

    @Test
    public void case1Test() {
        RBucket<Object> bucket = this.redisson.getBucket("test.user");
        Object value = bucket.get();
        System.out.println(value);
    }

    @Test
    public void case2Test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RBucketReactive<Object> value = this.redissonReactiveClient.getBucket("test.user");
        value.get().subscribe(v -> {
            System.out.println(v);
            countDownLatch.countDown();
        });
        System.out.println("方法执行完...");
        countDownLatch.await();
    }

    @Test
    public void case3Test() {
        Object value = this.redisTemplate.opsForValue().get("test.user");
        System.out.println(value);
    }

    @Test
    public void case4Test() {
        RBucket<Object> bucket = this.redisson.getBucket("test.user");
        // CAS
        boolean res = bucket.compareAndSet(value, "1111111111");
        Assertions.assertTrue(res);
    }

    @Test
    public void case5Test() {
        RBucket<Object> bucket = this.redisson.getBucket("test.user");
        // CAS
        boolean res = bucket.compareAndSet("hhhhhhhhhh", "1111111111");
        Assertions.assertFalse(res);
    }

}

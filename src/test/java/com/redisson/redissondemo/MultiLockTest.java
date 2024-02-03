package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * 复合锁：由多把锁组成的复合锁
 * @author gengya.yuan
 * @since 2023-12-28 14:21
 */
@SpringBootTest
@Slf4j
public class MultiLockTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RLock lock1 = this.redisson.getLock("lock1");
        RLock lock2 = this.redisson.getLock("lock2");
        RLock lock3 = this.redisson.getLock("lock3");

        log.info("开始....");

        lock1.lock(3, TimeUnit.SECONDS);
        lock2.lock(5, TimeUnit.SECONDS);
        lock3.lock(1, TimeUnit.SECONDS);

        Thread t = new Thread(() -> {
            // 这样获得锁不会有什么问题
            RedissonMultiLock lock = new RedissonMultiLock(this.redisson.getLock("lock1"), this.redisson.getLock("lock2"), this.redisson.getLock("lock3"));
            log.info("尝试获得...");
            lock.lock();
            log.info("获得锁...");
            this.sleep(10);
            lock.unlock();
        });

        t.start();
        t.join();

        log.info("结束....");
    }

    @Test
    public void case2Test() throws InterruptedException {
        RLock lock1 = this.redisson.getLock("case2.lock1");
        RLock lock2 = this.redisson.getLock("case2.lock2");
        RLock lock3 = this.redisson.getLock("case2.lock3");

        log.info("开始....");

        lock1.lock(3, TimeUnit.SECONDS);
        lock2.lock(5, TimeUnit.SECONDS);
        lock3.lock(1, TimeUnit.SECONDS);

        Thread t = new Thread(() -> {
            // 直接这样会报错，使用case1的方式不会报错！！！
            RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
            log.info("尝试获得...");
            lock.lock();
            log.info("获得锁...");
            this.sleep(10);
            lock.unlock();
        });

        t.start();
        t.join();

        log.info("结束....");
    }

}

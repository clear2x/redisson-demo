package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author gengya.yuan
 * @since 2023-12-28 14:16
 */
@SpringBootTest
public class LockTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RLock lock = this.redisson.getLock("lock.test");
        // 5 秒之后自动释放
        lock.lock(5, TimeUnit.SECONDS);
        Thread thread = new Thread(() -> {
            RLock l = this.redisson.getLock("lock.test");
            System.out.println("尝试获取锁");
            l.lock();
            System.out.println("获得锁");
            l.unlock();
        });
        thread.start();
        thread.join();
    }

}

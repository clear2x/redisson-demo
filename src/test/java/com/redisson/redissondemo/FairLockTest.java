package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 公平锁：获取到锁的顺序会和请求获取锁的顺序是一致的
 * @author gengya.yuan
 * @since 2023-12-28 14:02
 */
@SpringBootTest
public class FairLockTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RLock lock = this.redisson.getFairLock("fairLock.test");

        int size = 10;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Thread t = new Thread(() -> {
                lock.lock();
                System.out.println(STR."获取锁的线程是: \{Thread.currentThread().getName()}");
                this.sleep(2);
                lock.unlock();
            });
            t.setName(STR."thread: \{i}");
            threads.add(t);
        }

        for (Thread thread : threads) {
            thread.start();
            TimeUnit.MILLISECONDS.sleep(100);
        }

        for (Thread thread : threads) {
            thread.join();
        }

    }

}

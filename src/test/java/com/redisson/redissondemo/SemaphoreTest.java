package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RSemaphore;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 信号量测试
 * @author gengya.yuan
 * @since 2023-12-28 10:48
 */
@SpringBootTest
public class SemaphoreTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RSemaphore semaphore = this.redisson.getSemaphore("test.semaphore");
        // 设置5个许可
        semaphore.trySetPermits(5);

        // 获取3个许可，剩两个
        semaphore.acquire(3);

        new Thread(() -> {
            this.sleep(2);
            // 释放 2 个许可，可用总数为 4 个
            semaphore.release(2);
        }).start();

        System.out.println("阻塞...");
        // 获得 3 个，可用总数为1个
        semaphore.acquire(3);
        System.out.println("结束...");

        // 1 个
        System.out.println(semaphore.availablePermits());
        // 申请并返回所有的
        System.out.println(semaphore.drainPermits());
        // 可用为 0
        System.out.println(semaphore.availablePermits());

        semaphore.delete();
    }

    @Test
    public void case2Test() throws InterruptedException {
        RSemaphore semaphore = this.redisson.getSemaphore("test.semaphore2");
        // 设置5个许可
        semaphore.trySetPermits(5);
        // 5 个
        System.out.println(semaphore.availablePermits());
        // 申请并返回所有的 5个
        System.out.println(semaphore.drainPermits());
        // 可用为 0
        System.out.println(semaphore.availablePermits());

        semaphore.delete();
    }



}

package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPermitExpirableSemaphore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * 允许过期的信号量
 * @author gengya.yuan
 * @since 2023-12-28 14:38
 */
@SpringBootTest
public class PermitExpirableSemaphoreTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RPermitExpirableSemaphore s = this.redisson.getPermitExpirableSemaphore("PermitExpirableSemaphore.test");
        // 设置 1 许可证
        s.trySetPermits(1);

        /*
            100: 最长等待时间，2: 最长租期
         */
        String permitId = s.tryAcquire(100, 2, TimeUnit.SECONDS);

        Thread t = new Thread(() -> {
            RPermitExpirableSemaphore s1 = PermitExpirableSemaphoreTest.this.redisson.getPermitExpirableSemaphore("PermitExpirableSemaphore.test");
            try {
                String permitId1 = s1.acquire();
                // 只能释放自己的
                s1.release(permitId1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
        t.join();

        // 只能释放自己的
        s.tryRelease(permitId);

    }

}

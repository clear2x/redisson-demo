package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RCountDownLatch;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Redisson门闩
 * @author gengya.yuan
 * @since 2023-12-28 11:15
 */
@SpringBootTest
public class CountDownLatchTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        final RCountDownLatch latch = this.redisson.getCountDownLatch("latch1");
        latch.trySetCount(1);

        executor.execute(() -> {
            this.sleep(5);
            // 等待5秒
            latch.countDown();
        });

        executor.execute(() -> {
            try {
                System.out.println("等待...");
                //latch.await(550, TimeUnit.MILLISECONDS);
                latch.await();
                System.out.println("等待完成...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

    }

}

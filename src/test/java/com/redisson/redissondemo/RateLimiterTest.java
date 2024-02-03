package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 限流
 * @author gengya.yuan
 * @since 2023-12-28 15:55
 */
@SpringBootTest
@Slf4j
public class RateLimiterTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        ThreadFactory factory = Thread.ofVirtual().name("测试线程池").factory();
        try (var executorService = Executors.newThreadPerTaskExecutor(factory) ){
            while (true) {
                for (int i = 0; i < 1000; i++) {
                    executorService.execute(() -> {
                        //log.info(STR."\{Thread.currentThread().getName()} 尝试访问...");
                        if(this.tryAcquirePermit()) {
                            log.info(STR."\{Thread.currentThread().getName() + Thread.currentThread().threadId()} 访问成功...");
                        } else {
                            //log.error(STR."\{Thread.currentThread().getName() + Thread.currentThread().threadId()} 访问失败...");
                        }
                    });
                }
                this.sleep(1);
            }
        }
    }

    /**
     * 以下设置expire后，不能保证完全100%准确的控制 2秒内一定只有一次许可。但是基本大差不差能保证两秒内的流量了
     * @return
     */
    public boolean tryAcquirePermit() {
        RRateLimiter rateLimiter = this.redisson.getRateLimiter("moveAttendee.meetingUid.123321.attendeeUid.432423");
        rateLimiter.expire(Duration.of(5, ChronoUnit.SECONDS));
        rateLimiter.trySetRate(RateType.OVERALL, 1, 5, RateIntervalUnit.SECONDS);
        try {
            return  rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

}

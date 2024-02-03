package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author gengya.yuan
 * @since 2024-01-25 14:34
 */
@SpringBootTest
@Slf4j
public class RScoredSortedSetTest extends BaseTest{

    private RScoredSortedSet<String> scoredSortedSet = null;

    private final int delaySeconds = 10;

    @BeforeEach
    public void before() {
        this.scoredSortedSet = this.redisson.getScoredSortedSet("score.score.test.1");
        new Thread(() -> {
            int mix = 10000;
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                try {
                    log.info("check expire ");
                    this.scoredSortedSet.removeRangeByScore(now.plusDays(-1).toEpochSecond(ZoneOffset.UTC), true, now.toEpochSecond(ZoneOffset.UTC), true);
                    System.out.println(STR."检查时间：\{now.toLocalTime()} \n 过期之后有： \{this.scoredSortedSet.readAll()}");
                    TimeUnit.SECONDS.sleep(this.delaySeconds);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }).start();
    }

    @Test
    public void case1Test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            LocalDateTime now = LocalDateTime.now();
            this.scoredSortedSet.addIfAbsent(now.plusSeconds(this.delaySeconds).toEpochSecond(ZoneOffset.UTC), STR."\{now.plusSeconds(this.delaySeconds).toLocalTime()}");
            System.out.println(STR."当前有： \{this.scoredSortedSet.readAll()}");
            TimeUnit.SECONDS.sleep(1);
        }

        TimeUnit.SECONDS.sleep(60);
    }

}

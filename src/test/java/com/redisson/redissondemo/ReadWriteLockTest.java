package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 读写锁 1. 当线程持有读锁时，其他线程可以获取到读锁  2. 当线程持有写锁时，其他线程不可以获取读锁和写锁
 * @author gengya.yuan
 * @since 2023-12-28 14:46
 */
@SpringBootTest
@Slf4j
public class ReadWriteLockTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        final RReadWriteLock lock = this.redisson.getReadWriteLock("read.write.lock");

        // 写锁
        lock.writeLock().lock();
        log.info("持有写锁...");
        Thread t = new Thread(() -> {
            // 获取 读锁
            RLock r = this.redisson.getReadWriteLock("read.write.lock").readLock();
            // 获取不到读锁，因为外面已经持有了写锁
            log.info("尝试获取读锁...");
            r.lock();
            log.info("获取到读锁...");
            this.sleep(5);
            r.unlock();
        });

        t.start();
        t.join();

        lock.writeLock().unlock();
        log.info("释放写锁...");
        t.join();
    }

    @Test
    public void case2Test() throws InterruptedException {
        final RReadWriteLock lock = this.redisson.getReadWriteLock("case2.read.write.lock");

        // 写锁
        Thread t = new Thread(() -> {
            // 获取 读锁
            RLock r = this.redisson.getReadWriteLock("case2.read.write.lock").readLock();
            // 获取读锁成功
            log.info("尝试获取读锁...");
            r.lock();
            log.info("获取到读锁...");
            this.sleep(5);
            r.unlock();
        });

        t.start();

        this.sleep(1);
        lock.writeLock().lock();
        // 要等读锁释放
        log.info("持有写锁...");

        t.join();

        lock.writeLock().unlock();
        log.info("释放写锁...");
        t.join();
    }

    @Test
    public void case3Test() throws InterruptedException {
        final RReadWriteLock lock = this.redisson.getReadWriteLock("case3.read.write.lock");

        // 写锁
        Thread t = new Thread(() -> {
            // 获取 读锁
            RLock r = this.redisson.getReadWriteLock("case3.read.write.lock").readLock();
            // 获取读锁成功
            log.info("尝试获取读锁...");
            r.lock();
            log.info("获取到读锁...");
            this.sleep(5);
            r.unlock();
        });

        t.start();

        this.sleep(1);
        lock.readLock().lock();
        // 能获取读锁
        log.info("持有读锁...");

        t.join();

        lock.readLock().unlock();
        log.info("释放读锁...");
        t.join();
    }

}

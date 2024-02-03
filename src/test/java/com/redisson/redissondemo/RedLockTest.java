package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;

/**s
 * RedLock: 红锁，当Redis是集群部署时，RedLock是最佳选择
 * @author gengya.yuan
 * @since 2023-12-28 15:06
 */
@SpringBootTest
@Slf4j
public class RedLockTest extends BaseTest{

    /*
        红锁主要解决的问题：当master未同步锁数据给slave节点时发生宕机，此时slave升级为主节点，如果客户端再次尝试获取锁，可以再次获取锁，即：并不安全！
        红锁主要实现了DLM算法，大概的原理是：从N/2+1的节点上获得到了锁，即认为获得到了锁。
        参考链接：https://redis.io/docs/manual/patterns/distributed-locks/

        TODO : 搭建集群环境验证 RedLock 在某个节点宕机的情况
     */

    @Test
    public void case1Test() throws InterruptedException {
        RLock lock1 = this.redisson.getLock("lock1");
        RLock lock2 = this.redisson.getLock("lock2");
        RLock lock3 = this.redisson.getLock("lock3");

        Thread t1 = new Thread(() -> lock3.lock());
        t1.start();
        t1.join();

        Thread t = new Thread(() -> {
            RedissonMultiLock lock = new RedissonRedLock(lock1, lock2, lock3);
            lock.lock();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            lock.unlock();
        });
        t.start();
        t.join(1000);

        lock3.forceUnlock();

        RedissonMultiLock lock = new RedissonRedLock(lock1, lock2, lock3);
        lock.lock();
        lock.unlock();

    }

}

package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RTransferQueue;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author gengya.yuan
 * @since 2023-12-29 13:41
 */
@SpringBootTest
@Slf4j
public class QueueTest extends BaseTest{

    @Test
    public void case1Test() {
        // 无界队列
        RQueue<String> queue = this.redisson.getQueue("delay.queue");
        RDelayedQueue<String> delayedQueue = this.redisson.getDelayedQueue(queue);
        delayedQueue.offer("hello", 20, TimeUnit.SECONDS);
        String poll;
        while (true) {
             poll = queue.poll();
            if(poll != null) {
                break;
            }
            this.sleep(1);
        }
        System.out.println(poll);
    }

    @Test
    public void case2Test() throws InterruptedException {
        // 无界阻塞队列
        RBlockingQueue<String> blockingQueue = this.redisson.getBlockingQueue("delay.block.queue");

        RDelayedQueue<String> delayedQueue = this.redisson.getDelayedQueue(blockingQueue);
        delayedQueue.offer("hello", 20, TimeUnit.SECONDS);
        // 一直阻塞，直到获得
        String value = blockingQueue.take();
        System.out.println(value);
    }

    @Test
    public void case3Test() throws InterruptedException {
        // 转移，搬运的 queue
        RTransferQueue<String> transferQueue = this.redisson.getTransferQueue("delay.transfer.queue");
        new Thread(() -> {
            RTransferQueue<String> producer = this.redisson.getTransferQueue("delay.transfer.queue");
            try {
                this.sleep(10);
                producer.transfer("hello");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        log.info("开始等待...");
        String value = transferQueue.take();
        log.info(STR."value: \{value}");
    }

    @Test
    public void case4Test() throws InterruptedException {
        RTransferQueue<String> transferQueue = this.redisson.getTransferQueue("delay.transfer.queue2");
        // 会一直阻塞，直到响应超时
        transferQueue.transfer("hello");
    }

}

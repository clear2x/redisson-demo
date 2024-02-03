package com.redisson.redissondemo;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

/**
 * List列表测试
 * @author gengya.yuan
 * @since 2023-12-11 17:04
 */
@SpringBootTest
public class RListTest extends BaseTest{

    ExecutorService localExecutorService = new ThreadPoolExecutor(10, 10, 3000, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(STR."测试List-\{thread.threadId()}");
            return thread;
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());

    @Test
    public void case1Test() {
        RList<String> list = this.redisson.getList("test.list");
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            this.localExecutorService.execute(() -> {
                list.add(STR."STR:\{finalI}");
            });
        }
        this.sleep(20);
        System.out.println(list.size());
        list.delete();
    }


}

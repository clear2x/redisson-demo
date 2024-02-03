package com.redisson.redissondemo;

import com.redisson.redissondemo.task.CallableTask;
import com.redisson.redissondemo.task.RunnableTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RExecutorFuture;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.WorkerOptions;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 * @author gengya.yuan
 * @since 2023-12-11 17:07
 */
@SpringBootTest
@Slf4j
public class RScheduledExecutorServiceTest extends BaseTest{

    RScheduledExecutorService executorService = null;

    ExecutorService localExecutorService = new ThreadPoolExecutor(2, 2, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("这是一个测试-" + thread.threadId());
            return thread;
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());

    @BeforeEach
    public void before() {
        this.executorService = this.redisson.getExecutorService("demoExecutor");
        WorkerOptions workerOptions =  WorkerOptions.defaults();
        // 工作线程
        workerOptions.workers(4);
        // 会采用这个线程池执行任务
        workerOptions.executorService(this.localExecutorService);

        /*
            注意： 如果 workers > localExecutorService可执行的最大任务数，则当 localExecutorService 无法消费任务时，会报错。 比如： 5 > maximumPoolSize (2) + arrayBlockingQueue (2)
         */
        this.executorService.registerWorkers(workerOptions);
    }

    @Test
    public void case1Test() throws InterruptedException {
        this.executorService.schedule((Runnable & Serializable) () -> System.out.println("task has been executed!"), 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void case2Test() throws InterruptedException, ExecutionException {
        this.executorService.registerWorkers(WorkerOptions.defaults());
        RExecutorFuture<String> future = this.executorService.submit(new CallableTask());
        String res = future.get();
        System.out.println(res);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void case3Test() throws InterruptedException, ExecutionException {
        for (int i = 0; i < 10; i++) {
            this.executorService.execute(new RunnableTask());
        }
        RExecutorFuture<String> future = this.executorService.submit(new CallableTask());
        System.out.println(future.get());
        System.out.println(this.executorService.countActiveWorkers());
        this.executorService.execute((Runnable & Serializable) () -> System.out.println("测试执行线程池!"));
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void case4Test() throws InterruptedException {
        this.executorService.schedule((Runnable & Serializable) () -> log.info("task has been executed!"), 20, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(1000);
    }

    @Test
    public void case5Test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1000);
    }

    @AfterEach
    public void destroy() {
        this.executorService.shutdown();
        this.executorService.close();
        this.executorService.delete();
    }

}

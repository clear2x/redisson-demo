package com.redisson.redissondemo;

import com.redisson.redissondemo.task.RunnableTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.WorkerOptions;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gengya.yuan
 * @since 2023-12-28 15:52
 */
@SpringBootTest
@Slf4j
public class ExecutorServiceTest extends BaseTest{

    @Test
    public void case1Test() {
        RScheduledExecutorService executorService = this.redisson.getExecutorService("test.executor.service");
        executorService.registerWorkers(WorkerOptions.defaults());
        executorService.execute(new RunnableTask());
        this.sleep(5);
    }

}

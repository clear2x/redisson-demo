package com.redisson.redissondemo.task;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public  class RunnableTask implements Runnable, Serializable {

        @Serial
        private static final long serialVersionUID = 183759193951988948L;
        @RInject
        RedissonClient redisson;

        @Override
        public void run() {
            RMap<String, String> map = this.redisson.getMap("myMap");
            System.out.println(this.getClass().getSimpleName() + Thread.currentThread().getName() + " 执行...");
            map.put("5", "11");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }



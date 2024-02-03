package com.redisson.redissondemo.task;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public  class CallableTask implements Callable<String>, Serializable {

        @Serial
        private static final long serialVersionUID = -3363182319862798223L;
        @RInject
        RedissonClient redisson;

        @Override
        public String call() throws Exception {
            RMap<String, String> map = this.redisson.getMap("myMap");
            map.put("1", "2");
            System.out.println(this.getClass().getSimpleName() + Thread.currentThread().getName() + " 执行...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return map.get("3");
        }

    }

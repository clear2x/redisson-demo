package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RTransaction;
import org.redisson.api.TransactionOptions;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 面向Redis操作的事务管理
 * @author gengya.yuan
 * @since 2023-12-28 15:42
 */
@SpringBootTest
@Slf4j
public class TransactionTest extends BaseTest{

    @Test
    public void case1Test() {
        RBucket<String> b = this.redisson.getBucket("case1.transaction.bucket");
        b.set("123");

        RTransaction transaction = this.redisson.createTransaction(TransactionOptions.defaults());
        // 更新
        RBucket<String> bucket = transaction.getBucket("case1.transaction.bucket");
        bucket.set("234");

        // 插入
        RMap<String, String> map = transaction.getMap("case1.transaction.map");
        map.put("1", "2");

        // 提交事务
        transaction.commit();
    }

    @Test
    public void case2Test() {
        RBucket<String> b = this.redisson.getBucket("case1.transaction.bucket");
        b.set("123");

        RTransaction transaction = this.redisson.createTransaction(TransactionOptions.defaults());
        // 更新
        RBucket<String> bucket = transaction.getBucket("case1.transaction.bucket");
        bucket.set("666");

        // 会抛出异常，事务未提交
        int i = 1/0;

        // 插入
        RMap<String, String> map = transaction.getMap("case1.transaction.map");
        map.put("1", "3");

        // 提交事务
        transaction.commit();
    }

    @Test
    public void case3Test() {
        // 执行 case1 之后 执行case3查看结果：234 2
        // 执行 case2 之后 执行case3查看结果：123 2 ， 因为事务未提交，bucket.set("666"); map.put("1", "3"); 未设置进去

        RBucket<String> b = this.redisson.getBucket("case1.transaction.bucket");
        System.out.println(b.get());
        RMap<String, String> map = this.redisson.getMap("case1.transaction.map");
        System.out.println(map.get("1"));
    }

}

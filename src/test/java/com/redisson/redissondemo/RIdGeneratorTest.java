package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RIdGenerator;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gengya.yuan
 * @since 2023-12-29 11:45
 */
@SpringBootTest
@Slf4j
public class RIdGeneratorTest extends BaseTest{

    @Test
    public void case1Test() {
        RIdGenerator idGenerator = this.redisson.getIdGenerator("id.test");
        // 从12开始，一批一批的获取
        idGenerator.tryInit(12, 1000);
        for (int i = 0; i < 2000; i++) {
            System.out.println(idGenerator.nextId());
        }
    }

}

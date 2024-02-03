package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RStream;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gengya.yuan
 * @since 2024-01-02 14:54
 */
@SpringBootTest
@Slf4j
public class RStreamTest extends BaseTest{

    @Test
    public void case1Test() {
        RStream<String, String> stream = this.redisson.getStream("stream.test");

    }

}

package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RReliableTopic;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gengya.yuan
 * @since 2023-12-29 10:09
 */
@SpringBootTest
@Slf4j
public class TopicTest extends BaseTest{

    @Test
    public void case1Test() throws InterruptedException {
        RTopic topic = this.redisson.getTopic("topic.test");
        topic.addListener(String.class, (channel, msg) -> {
            log.info(STR."case1Test channel: \{channel}, msg: \{msg}");
        });
        this.sleep(1000000);
    }

    @Test
    public void case2Test() {
        RTopic topic = this.redisson.getTopic("topic.demo");
        topic.addListener(String.class, (channel, msg) -> {
            log.info(STR."case2Test channel: \{channel}, msg: \{msg}");
        });
        this.sleep(1000000);
    }

    @Test
    public void case3Test() {
        /*
            topic? subscribes to topic1, topicA ...
            topic?_my subscribes to topic_my, topic123_my, topicTEST_my ...
            topic[ae] subscribes to topica and topice only
         */
        RPatternTopic patternTopic = this.redisson.getPatternTopic("topic*");
        patternTopic.addListener(String.class, (pattern, channel, msg) -> {
            log.info(STR."case3Test pattern: \{pattern} channel: \{channel}, msg: \{msg}");
        });
        this.sleep(1000000);
    }

    @Test
    public void case4Test() {
        RReliableTopic reliableTopic = this.redisson.getReliableTopic("topic.reliable");
        reliableTopic.addListener(String.class, (channel, msg) -> {
            log.info(STR."case4Test channel: \{channel}, msg: \{msg}");
        });
        this.sleep(1000000);
    }

    @Test
    public void case5Test() {
        this.redisson.getTopic("topic.test").publish("这是test信息");
        this.redisson.getTopic("topic.demo").publish("这是demo信息");
        this.redisson.getReliableTopic("topic.reliable").publish("这是reliable信息");
        // 这两个发送出去，case1Test case2Test case3Test
        this.redisson.getReliableTopic("topic.test").publish("这是reliable test信息");
        this.redisson.getReliableTopic("topic.demo").publish("这是reliable demo信息");
    }

    @Test
    public void case6Test() {
        RReliableTopic reliableTopic = this.redisson.getReliableTopic("topic.demo");
        reliableTopic.addListener(String.class, (channel, msg) -> {
            log.info(STR."case6Test channel: \{channel}, msg: \{msg}");
        });
        this.sleep(100000);
    }

    @Test
    public void case7Test() {
        this.redisson.getReliableTopic("topic.ReliableTopic.demo").addListener(String.class,(channel, msg) -> {
            System.out.println(msg);
        });
        this.redisson.getReliableTopic("topic.ReliableTopic.demo").publish("这是reliable demo信息");

    }

    @Test
    public void case8Test() {


    }


}

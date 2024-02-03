package com.redisson.redissondemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSet;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 有序Set
 * @author gengya.yuan
 * @since 2024-01-02 14:37
 */
@SpringBootTest
@Slf4j
public class ScoredSortedSetTest extends BaseTest{

    @Test
    public void case1Test() {
        RScoredSortedSet<String> set = this.redisson.getScoredSortedSet("score.test.1");
        set.add(10, "1");
        set.add(20, "2");
        set.add(30, "3");

        for (var v : set) {
            log.info(STR."迭代： \{v}");
        }

        Map<String, Double> newValues = new HashMap<>();
        newValues.put("4", 40D);
        newValues.put("5", 50D);
        newValues.put("6", 60D);
        int newValuesAmount = set.addAll(newValues);

        Double scoreResult = set.addScore("2", 10);
        set.contains("4");
        set.containsAll(Arrays.asList("3", "4", "5"));

        for (String s : set.readAll()) {
            log.info(STR."迭代2： \{s}");
        }

        String firstValue = set.first();
        String lastValue = set.last();
        log.info(STR."first: \{firstValue} lastValue: \{lastValue}}");

        String polledFirst = set.pollFirst();
        String polledLast = set.pollLast();
        log.info(STR."polledFirst: \{polledFirst} polledLast: \{polledLast}}");

        for (String s : set.readAll()) {
            log.info(STR."迭代3： \{s}");
        }
    }

    @Test
    public void case2Test() {
        RScoredSortedSet<String> set = this.redisson.getScoredSortedSet("score.test.2");
        set.add(0.3, "1");
        set.add(0.1, "2");
        set.add(0.2, "3");

        Map<String, Double> newValues = new HashMap<>();
        newValues.put("4", 0.11);
        newValues.put("5", 0.12);
        newValues.put("6", 0.21);
        set.addAll(newValues);

        for (String s : set.readAll()) {
            log.info(STR."迭代： \{s}");
        }
    }

}

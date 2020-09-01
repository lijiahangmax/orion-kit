package com.orion.test.id;

import com.orion.id.Sequences;
import com.orion.id.SnowFlakes;
import com.orion.id.UUIds;
import com.orion.utils.Threads;

import java.util.concurrent.Executors;

/**
 * id 生成器测试
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/13 18:04
 */
public class IdTests {

    public static void main(String[] args) {
        sequence();
    }

    private static void sequence() {
        Threads.concurrent(100, Executors.newCachedThreadPool(), () -> System.out.println(Sequences.createId()));
    }

    private static void snowflake() {
        Threads.concurrent(100, Executors.newCachedThreadPool(), () -> System.out.println(SnowFlakes.createId()));
    }

    private static void uuid() {
        Threads.concurrent(100, Executors.newCachedThreadPool(), () -> System.out.println(UUIds.random15Long()));
    }

}

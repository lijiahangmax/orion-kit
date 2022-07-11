package com.orion.test.id;

import com.orion.lang.id.ObjectIds;
import com.orion.lang.id.Sequences;
import com.orion.lang.id.SnowFlakes;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Threads;

import java.util.concurrent.Executors;

/**
 * id 生成器测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:04
 */
public class IdTests {

    public static void main(String[] args) {
        sequence();
        Threads.sleep(500);
        System.out.println();
        snowflake();
        Threads.sleep(500);
        System.out.println();
        objectId();
        Threads.sleep(500);
        System.out.println();
        uuid();
    }

    private static void sequence() {
        Threads.concurrent(() -> System.out.println(Sequences.nextId()), 100, Executors.newCachedThreadPool());
    }

    private static void snowflake() {
        Threads.concurrent(() -> System.out.println(SnowFlakes.nextId()), 100, Executors.newCachedThreadPool());
    }

    private static void objectId() {
        Threads.concurrent(() -> System.out.println(ObjectIds.nextId()), 100, Executors.newCachedThreadPool());
    }

    private static void uuid() {
        Threads.concurrent(() -> System.out.println(UUIds.random15Long()), 100, Executors.newCachedThreadPool());
    }

}

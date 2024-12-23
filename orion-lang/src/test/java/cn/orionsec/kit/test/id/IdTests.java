/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.test.id;

import cn.orionsec.kit.lang.id.ObjectIds;
import cn.orionsec.kit.lang.id.Sequences;
import cn.orionsec.kit.lang.id.SnowFlakes;
import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.Threads;

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

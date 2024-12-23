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
package cn.orionsec.kit.test;

import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.random.Randoms;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/20 17:41
 */
public class ThreadTest {

    @Test
    public void testConcurrentRun() {
        Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        }, 3, Threads.CACHE_EXECUTOR);
        System.out.println("end");

        Threads.sleep(10000);
    }

    @Test
    public void testConcurrentRun1() {
        CyclicBarrier c = new CyclicBarrier(3);
        Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        }, Threads.CACHE_EXECUTOR, c);
        System.out.println("end");

        Threads.sleep(10000);
    }

    @Test
    public void testConcurrentRun2() {
        CountDownLatch c = new CountDownLatch(3);
        Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        }, Threads.CACHE_EXECUTOR, c);
        System.out.println("end");

        Threads.sleep(10000);
    }

    @Test
    public void testConcurrentCall() throws ExecutionException, InterruptedException {
        List<Future<Integer>> fs = Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
            return i;
        }, 3, Threads.CACHE_EXECUTOR);
        System.out.println("end");

        for (Future<Integer> f : fs) {
            System.out.println(f.get());
        }
    }

    @Test
    public void testConcurrentCall1() throws ExecutionException, InterruptedException {
        CyclicBarrier c = new CyclicBarrier(3);
        List<Future<Integer>> fs = Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
            return i;
        }, Threads.CACHE_EXECUTOR, c);
        System.out.println("end");

        for (Future<Integer> f : fs) {
            System.out.println(f.get());
        }
    }

    @Test
    public void testConcurrentCall2() throws ExecutionException, InterruptedException {
        CountDownLatch c = new CountDownLatch(3);
        List<Future<Integer>> fs = Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
            return i;
        }, Threads.CACHE_EXECUTOR, c);
        System.out.println("end");

        for (Future<Integer> f : fs) {
            System.out.println(f.get());
        }
    }

    @Test
    public void testConcurrentRunList() {
        Runnable runnable = () -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        };
        Threads.concurrentRunnable(Lists.of(runnable, runnable, runnable), Threads.CACHE_EXECUTOR);
        System.out.println("end");

        Threads.sleep(10000);
    }

    @Test
    public void testConcurrentCallList() throws ExecutionException, InterruptedException {
        Callable<Integer> runnable = () -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
            return i;
        };
        for (Future<Integer> f : Threads.concurrentCallable(Lists.of(runnable, runnable, runnable), Threads.CACHE_EXECUTOR)) {
            System.out.println(f.get());
        }
        System.out.println("end");

        Threads.sleep(10000);
    }

    @Test
    public void blockRun() throws InterruptedException {
        Runnable runnable = () -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        };
        Threads.blockRun(runnable, 3, null);
        System.out.println("end");
    }

    @Test
    public void blockRun1() throws InterruptedException {
        Runnable runnable = () -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        };
        Threads.blockRun(runnable, new CountDownLatch(5), null);
        System.out.println("end");
    }

    @Test
    public void blockRun2() throws InterruptedException {
        Runnable runnable = () -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            System.out.println(Thread.currentThread().getName() + " start " + i + " " + System.currentTimeMillis());
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
        };
        Threads.blockRun(Lists.of(runnable, runnable, runnable), null);
        System.out.println("end");
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.utils.collect;

import com.orion.lang.define.collect.FixedDeque;
import com.orion.lang.define.collect.FixedQueue;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Queue 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/6 11:38
 */
public class Queues extends Collections {

    private Queues() {
    }

    // -------------------- new --------------------

    public static <E> Queue<E> newQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    public static <E> Queue<E> newQueue(Collection<E> c) {
        return new ConcurrentLinkedQueue<>(c);
    }

    public static <E> Deque<E> newDeque() {
        return new ConcurrentLinkedDeque<>();
    }

    public static <E> Deque<E> newDeque(Collection<E> c) {
        return new ConcurrentLinkedDeque<>(c);
    }

    public static <E> Queue<E> newLimitQueue(int size) {
        return new FixedQueue<>(size);
    }

    public static <E> Deque<E> newLimitDeque(int size) {
        return new FixedDeque<>(size);
    }

    // -------------------- function --------------------

    public static <E> Queue<E> def(Queue<E> queue) {
        return queue == null ? new ConcurrentLinkedQueue<>() : queue;
    }

    public static <E> Deque<E> def(Deque<E> queue) {
        return queue == null ? new ConcurrentLinkedDeque<>() : queue;
    }

    public static <E> Queue<E> def(Queue<E> queue, Queue<E> def) {
        return queue == null ? def : queue;
    }

    public static <E> Deque<E> def(Deque<E> queue, Deque<E> def) {
        return queue == null ? def : queue;
    }

    public static <E> Queue<E> def(Queue<E> queue, Supplier<Queue<E>> def) {
        return queue == null ? def.get() : queue;
    }

    public static <E> Deque<E> def(Deque<E> queue, Supplier<Deque<E>> def) {
        return queue == null ? def.get() : queue;
    }

    @SafeVarargs
    public static <E> Queue<E> of(E... values) {
        return new ConcurrentLinkedQueue<>(Arrays.asList(values));
    }

    @SafeVarargs
    public static <E> Deque<E> ofd(E... values) {
        return new ConcurrentLinkedDeque<>(Arrays.asList(values));
    }

    @SafeVarargs
    public static <E, V> Queue<E> of(Function<V, E> mapper, V... values) {
        Valid.notNull(mapper, "convert function is null");
        Queue<E> q = new ConcurrentLinkedQueue<>();
        int length = Arrays1.length(values);
        for (int i = 0; i < length; i++) {
            q.add(mapper.apply(values[i]));
        }
        return q;
    }

    @SafeVarargs
    public static <E, V> Deque<E> ofd(Function<V, E> mapper, V... e) {
        Valid.notNull(mapper, "convert function is null");
        Deque<E> q = new ConcurrentLinkedDeque<>();
        int length = Arrays1.length(e);
        for (int i = 0; i < length; i++) {
            q.add(mapper.apply(e[i]));
        }
        return q;
    }

    public static <E, V> Queue<E> map(Queue<V> queue, Function<V, E> mapper) {
        Valid.notNull(mapper, "convert function is null");
        Queue<E> q = new ConcurrentLinkedQueue<>();
        if (isEmpty(queue)) {
            return q;
        }
        for (V v : queue) {
            q.add(mapper.apply(v));
        }
        return q;
    }

    public static <E, V> Deque<E> map(Deque<V> queue, Function<V, E> f) {
        Valid.notNull(f, "convert function is null");
        Deque<E> q = new ConcurrentLinkedDeque<>();
        if (isEmpty(queue)) {
            return q;
        }
        for (V v : queue) {
            q.add(f.apply(v));
        }
        return q;
    }

    public static <E> Queue<E> as(Iterator<E> iterator) {
        Queue<E> q = new ConcurrentLinkedQueue<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                q.add(iterator.next());
            }
        }
        return q;
    }

    public static <E> Deque<E> asd(Iterator<E> iterator) {
        Deque<E> q = new ConcurrentLinkedDeque<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                q.add(iterator.next());
            }
        }
        return q;
    }

    public static <E> Queue<E> as(Enumeration<E> iterator) {
        Queue<E> q = new ConcurrentLinkedQueue<>();
        if (iterator != null) {
            while (iterator.hasMoreElements()) {
                q.add(iterator.nextElement());
            }
        }
        return q;
    }

    public static <E> Deque<E> asd(Enumeration<E> iterator) {
        Deque<E> q = new ConcurrentLinkedDeque<>();
        if (iterator != null) {
            while (iterator.hasMoreElements()) {
                q.add(iterator.nextElement());
            }
        }
        return q;
    }

}

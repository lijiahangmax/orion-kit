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
package cn.orionsec.kit.lang.utils.hash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.ToIntFunction;

/**
 * 一致性 hash 算法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 17:30
 */
public class ConsistentHash<T> {

    /**
     * 自定义 hash 算法
     */
    private final ToIntFunction<Object> hashFun;

    /**
     * 复制的节点个数
     */
    private final int numberOfReplicas;

    /**
     * 一致性 hash 环
     */
    private final SortedMap<Integer, T> circle = new TreeMap<>();

    /**
     * @param numberOfReplicas 复制的节点个数, 增加每个节点的复制节点有利于负载均衡
     * @param nodes            节点对象
     */
    public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
        this(o -> Hashes.fnvHash(o.toString()), numberOfReplicas, nodes);
    }

    /**
     * @param hashFun          hash算法对象
     * @param numberOfReplicas 复制的节点个数, 增加每个节点的复制节点有利于负载均衡
     * @param nodes            节点对象
     */
    public ConsistentHash(ToIntFunction<Object> hashFun, int numberOfReplicas, Collection<T> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        this.hashFun = hashFun;
        // 初始化节点
        for (T node : nodes) {
            this.add(node);
        }
    }

    /**
     * 增加节点
     * 每增加一个节点, 就会在闭环上增加给定复制节点数
     * <p>
     * 由于hash算法会调用node的toString方法, 故按照toString去重
     * <p>
     * 例如复制节点数是2, 则每调用此方法一次, 增加两个虚拟节点, 这两个节点指向同一Node
     *
     * @param node 节点对象
     */
    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFun.applyAsInt(node.toString() + i), node);
        }
    }

    /**
     * 移除节点的同时移除相应的虚拟节点
     *
     * @param node 节点对象
     */
    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFun.applyAsInt(node.toString() + i));
        }
    }

    /**
     * 获得一个最近的顺时针节点
     *
     * @param key 为给定键取Hash, 取得顺时针方向上最近的一个虚拟节点对应的实际节点
     * @return 节点对象
     */
    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFun.applyAsInt(key);
        if (!circle.containsKey(hash)) {
            // 返回此映射的部分视图, 其键大于等于 hash
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        // 正好命中
        return circle.get(hash);
    }

}

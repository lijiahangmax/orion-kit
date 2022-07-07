package com.orion.lang.define.collect;

import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 权重随机Map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 10:52
 */
public class WeightRandomMap<T> extends TreeMap<Double, T> implements Serializable {

    private static final long serialVersionUID = -978238901200283479L;

    private final Random RANDOM = ThreadLocalRandom.current();

    public static <T> WeightRandomMap<T> create() {
        return new WeightRandomMap<>();
    }

    public WeightRandomMap() {
    }

    public WeightRandomMap(Collection<WeightObject<T>> weights) {
        if (Lists.isNotEmpty(weights)) {
            for (WeightObject<T> w : weights) {
                this.put(w);
            }
        }
    }

    public WeightRandomMap(Map<T, Double> weights) {
        if (Maps.isNotEmpty(weights)) {
            for (Map.Entry<T, Double> e : weights.entrySet()) {
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public T put(Double weight, T value) {
        this.put(new WeightObject<>(value, weight));
        return null;
    }

    /**
     * 增加对象
     *
     * @param o      对象
     * @param weight 权重
     */
    public void put(T o, double weight) {
        this.put(new WeightObject<>(o, weight));
    }

    /**
     * 增加对象权重
     *
     * @param o 权重对象
     */
    public void put(WeightObject<T> o) {
        if (o != null) {
            double weight = o.getWeight();
            if (o.getWeight() > 0) {
                double lastWeight = (this.size() == 0) ? 0 : this.lastKey();
                super.put(weight + lastWeight, o.getObject());
            }
        }
    }

    /**
     * 下一个随机对象
     *
     * @return 随机对象
     */
    public T next() {
        if (Maps.isEmpty(this)) {
            return null;
        }
        double randomWeight = this.lastKey() * RANDOM.nextDouble();
        SortedMap<Double, T> tailMap = this.tailMap(randomWeight, false);
        return this.get(tailMap.firstKey());
    }

    /**
     * 带有权重的对象包装
     *
     * @param <T> 对象类型
     * @author looly
     */
    public static class WeightObject<T> {

        /**
         * 对象
         */
        private T object;

        /**
         * 权重
         */
        private final double weight;

        public WeightObject(T obj, double weight) {
            this.object = obj;
            this.weight = weight;
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((object == null) ? 0 : object.hashCode());
            long temp;
            temp = Double.doubleToLongBits(weight);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            WeightObject<?> other = (WeightObject<?>) obj;
            if (this.object == null) {
                if (other.object != null) {
                    return false;
                }
            } else if (!this.object.equals(other.object)) {
                return false;
            }
            return Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
        }
    }

}
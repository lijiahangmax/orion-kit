package com.orion.lang.collect;

import com.orion.utils.collect.Lists;
import com.orion.utils.collect.Maps;

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
                put(w);
            }
        }
    }

    public WeightRandomMap(Map<T, Double> weights) {
        if (Maps.isNotEmpty(weights)) {
            for (Map.Entry<T, Double> e : weights.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * 增加对象
     *
     * @param o      对象
     * @param weight 权重
     * @return this
     */
    public WeightRandomMap<T> put(T o, double weight) {
        return put(new WeightObject<>(o, weight));
    }

    /**
     * 增加对象权重
     *
     * @param o 权重对象
     * @return this
     */
    public WeightRandomMap<T> put(WeightObject<T> o) {
        if (null != o) {
            double weight = o.getWeight();
            if (o.getWeight() > 0) {
                double lastWeight = (this.size() == 0) ? 0 : this.lastKey();
                this.put(weight + lastWeight, o.getObject());
            }
        }
        return this;
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
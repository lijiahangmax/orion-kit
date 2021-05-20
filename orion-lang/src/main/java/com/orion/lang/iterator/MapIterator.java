package com.orion.lang.iterator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * map迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 20:39
 */
public class MapIterator<K, V> implements Iterator<Map.Entry<K, V>>, Iterable<Map.Entry<K, V>>, Serializable {

    private static final long serialVersionUID = -2395934120935349L;

    private Map<K, V> map;

    private Iterator<Map.Entry<K, V>> iterator;

    public MapIterator<K, V> setMap(Map<K, V> map) {
        this.map = map;
        this.iterator = this.map.entrySet().iterator();
        return this;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        map.entrySet().forEach(action);
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return map.entrySet().spliterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Map.Entry<K, V> next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
        iterator.forEachRemaining(action);
    }

}

package com.orion.csv.reader;

import com.orion.able.SafeCloseable;

import java.util.Iterator;

/**
 * Csv 迭代器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/2 18:31
 */
public class CsvReaderIterator<T> implements SafeCloseable, Iterator<T>, Iterable<T> {

    private BaseCsvReader<T> reader;

    /**
     * 下一个元素
     */
    private T next;

    public CsvReaderIterator(BaseCsvReader<T> reader) {
        this.reader = reader;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (reader.end) {
            return false;
        }
        next = reader.nextRow();
        if (reader.end) {
            return false;
        }
        if (next == null && reader.skipNullRows) {
            return hasNext();
        }
        return true;
    }

    @Override
    public T next() {
        reader.rowNum++;
        return next;
    }

    @Override
    public void close() {
        reader.close();
    }

}

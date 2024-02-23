package com.orion.office.excel.reader;

import com.orion.lang.utils.Exceptions;

import java.util.Iterator;

/**
 * excel 迭代器
 * <p>
 * 不会存储也不会消费
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/2 17:42
 */
public class ExcelReaderIterator<T> implements Iterator<T>, Iterable<T> {

    private final BaseExcelReader<?, T> reader;

    /**
     * 是否为第一次
     */
    private boolean first;

    /**
     * 是否还有下一个元素
     */
    private boolean hasNext;

    /**
     * 下一个元素
     */
    private T next;

    public ExcelReaderIterator(BaseExcelReader<?, T> reader) {
        this.reader = reader;
        this.first = true;
    }

    /**
     * 迭代器 不会存储也不会消费
     *
     * @return 迭代器
     */
    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (first) {
            this.first = false;
            this.hasNext = this.getNext();
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (!this.hasNext()) {
            this.throwNoSuch();
        }
        // 获取下一个元素并且返回上一个元素
        T next = this.next;
        this.hasNext = this.getNext();
        return next;
    }

    /**
     * 获取下一个元素
     *
     * @return element
     */
    private boolean getNext() {
        if (reader.end) {
            return false;
        }
        this.next = reader.nextRow();
        return !reader.end;
    }

    /**
     * 抛出异常
     */
    private void throwNoSuch() {
        throw Exceptions.noSuchElement("there are no more elements");
    }

}

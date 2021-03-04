package com.orion.office.excel.reader;

import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;

import java.util.Iterator;

/**
 * Excel 迭代器
 * <p>
 * 不会存储也不会消费
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/2 17:42
 */
public class ExcelReaderIterator<T> implements SafeCloseable, Iterator<T>, Iterable<T> {

    private BaseExcelReader<T> reader;

    /**
     * 是否为第一次
     */
    private boolean first = true;

    /**
     * 是否还有下一个元素
     */
    private boolean hasNext;

    /**
     * 下一个元素
     */
    private T next;

    public ExcelReaderIterator(BaseExcelReader<T> reader) {
        this.reader = reader;
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
            first = false;
            hasNext = this.getNext();
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (first) {
            boolean next = getNext();
            if (!next) {
                throwNoSuch();
            }
        }
        if (!hasNext && !first) {
            throwNoSuch();
        }
        if (first) {
            first = false;
        }
        T next = this.next;
        hasNext = this.getNext();
        reader.rowNum++;
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
        next = reader.nextRow();
        if (reader.end) {
            return false;
        }
        if (next == null && reader.skipNullRows) {
            return getNext();
        }
        return true;
    }

    /**
     * 抛出异常
     */
    private void throwNoSuch() {
        throw Exceptions.noSuchElement("there are no more elements");
    }

    @Override
    public void close() {
        reader.close();
    }

}

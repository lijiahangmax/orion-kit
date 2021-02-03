package com.orion.excel.reader;

import com.orion.able.SafeCloseable;

import java.util.Iterator;

/**
 * Excel 迭代器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/2 17:42
 */
public class ExcelReaderIterator<T> implements SafeCloseable, Iterator<T>, Iterable<T> {

    private BaseExcelReader<T> reader;

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

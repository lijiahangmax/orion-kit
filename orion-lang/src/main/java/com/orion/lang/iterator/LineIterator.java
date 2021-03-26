package com.orion.lang.iterator;

import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Iterator;

/**
 * 行迭代器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 16:28
 */
public class LineIterator implements Iterator<String>, Iterable<String>, SafeCloseable, Serializable {

    private static final long serialVersionUID = 129389409684095L;

    private final BufferedReader bufferedReader;

    private String current;

    private boolean finished;

    private boolean autoClose;

    public LineIterator(Reader reader) {
        Valid.notNull(reader, "reader is null");
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }

    /**
     * 设置流自动关闭
     *
     * @param autoClose 是否自动关闭
     * @return this
     */
    public LineIterator autoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    @Override
    public boolean hasNext() {
        if (finished) {
            return false;
        } else if (current != null) {
            return true;
        }
        try {
            this.current = bufferedReader.readLine();
            if (current == null) {
                this.finished = true;
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw Exceptions.noSuchElement("no more lines");
        }
        String t = current;
        this.current = null;
        return t;
    }

    @Override
    public void close() {
        this.finished = true;
        this.current = null;
        if (autoClose) {
            Streams.close(bufferedReader);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

}
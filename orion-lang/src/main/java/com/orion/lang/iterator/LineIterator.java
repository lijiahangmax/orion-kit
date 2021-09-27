package com.orion.lang.iterator;

import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
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
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 16:28
 */
public class LineIterator implements Iterator<String>, Iterable<String>, SafeCloseable, Serializable {

    private static final long serialVersionUID = 129389409684095L;

    private final BufferedReader reader;

    private String current;

    private boolean finished;

    private boolean autoClose;

    public LineIterator(Reader reader) {
        this(reader, Const.BUFFER_KB_8);
    }

    public LineIterator(Reader reader, int bufferSize) {
        Valid.notNull(reader, "reader is null");
        this.reader = Streams.toBufferedReader(reader, bufferSize);
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
            this.current = reader.readLine();
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
        String tmp = current;
        this.current = null;
        return tmp;
    }

    @Override
    public void close() {
        this.finished = true;
        this.current = null;
        if (autoClose) {
            Streams.close(reader);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }

}
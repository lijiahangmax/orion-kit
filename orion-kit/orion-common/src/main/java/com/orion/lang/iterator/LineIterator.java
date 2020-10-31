package com.orion.lang.iterator;

import com.orion.utils.Exceptions;
import com.orion.utils.io.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 行迭代器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 16:28
 */
public class LineIterator implements Iterator<String> {

    private final BufferedReader bufferedReader;
    private String line;
    private boolean finished = false;

    public LineIterator(Reader reader) {
        if (reader == null) {
            throw Exceptions.argument("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }
    }

    @Override
    public boolean hasNext() {
        if (line != null) {
            return true;
        } else if (finished) {
            return false;
        } else {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    finished = true;
                    return false;
                } else {
                    this.line = line;
                    return true;
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = line;
        line = null;
        return currentLine;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    public void close() {
        finished = true;
        Streams.close(bufferedReader);
        line = null;
    }

}
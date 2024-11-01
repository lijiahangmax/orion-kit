/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.define.io;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 输出追加器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/24 9:55
 */
public class WriterAppender extends Writer implements Iterable<WriterAppender> {

    /**
     * 流
     */
    private final Writer writer;

    /**
     * 父节点
     */
    private WriterAppender prev;

    /**
     * 子节点
     */
    private WriterAppender next;

    /**
     * 关闭时关闭
     */
    private boolean closeOnClose;

    public WriterAppender(Writer writer) {
        this.writer = writer;
        this.closeOnClose = true;
    }

    /**
     * 创建
     *
     * @param writer writer
     * @return WriterAppender
     */
    public static WriterAppender create(Writer writer) {
        return new WriterAppender(writer);
    }

    /**
     * 连接下一个
     *
     * @param writer writer
     * @return next
     */
    public WriterAppender then(Writer writer) {
        WriterAppender appender = new WriterAppender(writer);
        appender.prev = this;
        this.next = appender;
        return appender;
    }

    /**
     * close时是否关闭流
     *
     * @param onClose onClose
     * @return this
     */
    public WriterAppender onClose(boolean onClose) {
        this.closeOnClose = onClose;
        return this;
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public WriterAppender getRoot() {
        return this.findPrev(this);
    }

    /**
     * 获取上一个节点
     *
     * @param appender appender
     * @return prev
     */
    private WriterAppender findPrev(WriterAppender appender) {
        if (appender.prev == null) {
            return appender;
        } else {
            return this.findPrev(appender.prev);
        }
    }

    /**
     * 处理流
     *
     * @param c c
     */
    public void handle(Consumer<Writer> c) {
        for (WriterAppender appender : this) {
            c.accept(appender.writer);
        }
    }

    @Override
    public void write(int b) throws IOException {
        for (WriterAppender appender : this) {
            appender.writer.write(b);
        }
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        writer.write(cbuf);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        writer.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }

    @Override
    public Writer append(char c) throws IOException {
        return writer.append(c);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return writer.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return writer.append(csq, start, end);
    }

    @Override
    public void close() {
        for (WriterAppender appender : this) {
            if (appender.closeOnClose) {
                Streams.close(appender.writer);
            }
        }
    }

    @Override
    public void flush() {
        for (WriterAppender appender : this) {
            Streams.flush(appender.writer);
        }
    }

    @Override
    public Iterator<WriterAppender> iterator() {
        return new WriterAppenderIterator(this.getRoot());
    }

    public Writer getWriter() {
        return writer;
    }

    static class WriterAppenderIterator implements Iterator<WriterAppender> {

        private WriterAppender current;

        private WriterAppenderIterator(WriterAppender current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public WriterAppender next() {
            if (current == null) {
                throw Exceptions.noSuchElement("there are no more elements");
            }
            WriterAppender tmp = this.current;
            this.current = this.current.next;
            return tmp;
        }
    }

}

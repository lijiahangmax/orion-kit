package com.orion.lang.define.io;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Streams;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 输出追加器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/24 9:55
 */
public class OutputAppender extends OutputStream implements Iterable<OutputAppender> {

    /**
     * 流
     */
    private OutputStream out;

    /**
     * 父节点
     */
    private OutputAppender prev;

    /**
     * 子节点
     */
    private OutputAppender next;

    /**
     * 关闭时关闭
     */
    private boolean closeOnClose;

    public OutputAppender(OutputStream out) {
        this.out = out;
        this.closeOnClose = true;
    }

    /**
     * 创建
     *
     * @param logStream logStream
     * @return OutputAppender
     */
    public static OutputAppender create(OutputStream logStream) {
        return new OutputAppender(logStream);
    }

    /**
     * 连接下一个
     *
     * @param logStream logStream
     * @return next
     */
    public OutputAppender then(OutputStream logStream) {
        OutputAppender appender = new OutputAppender(logStream);
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
    public OutputAppender onClose(boolean onClose) {
        this.closeOnClose = onClose;
        return this;
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public OutputAppender getRoot() {
        return this.findPrev(this);
    }

    /**
     * 获取上一个节点
     *
     * @param appender appender
     * @return prev
     */
    private OutputAppender findPrev(OutputAppender appender) {
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
    public void handle(Consumer<OutputStream> c) {
        for (OutputAppender appender : this) {
            c.accept(appender.out);
        }
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputAppender appender : this) {
            appender.out.write(b);
        }
    }

    @Override
    public void write(byte[] bs) throws IOException {
        for (OutputAppender appender : this) {
            appender.out.write(bs);
        }
    }

    @Override
    public void write(byte[] bs, int off, int len) throws IOException {
        for (OutputAppender appender : this) {
            appender.out.write(bs, off, len);
        }
    }

    @Override
    public void close() {
        for (OutputAppender appender : this) {
            if (appender.closeOnClose) {
                Streams.close(appender.out);
            }
        }
    }

    @Override
    public void flush() {
        for (OutputAppender appender : this) {
            Streams.flush(appender.out);
        }
    }

    @Override
    public Iterator<OutputAppender> iterator() {
        return new OutputAppenderIterator(this.getRoot());
    }

    public OutputStream getOut() {
        return out;
    }

    static class OutputAppenderIterator implements Iterator<OutputAppender> {

        private OutputAppender current;

        private OutputAppenderIterator(OutputAppender current) {
            this.current = current;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public OutputAppender next() {
            if (current == null) {
                throw Exceptions.noSuchElement("there are no more elements");
            }
            OutputAppender tmp = this.current;
            this.current = this.current.next;
            return tmp;
        }
    }

}

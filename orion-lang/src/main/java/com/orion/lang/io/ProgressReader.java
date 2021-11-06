package com.orion.lang.io;

import com.orion.support.progress.ByteTransferProgress;
import com.orion.utils.Valid;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * 有进度条的 reader
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:42
 */
public class ProgressReader extends Reader {

    private Reader wrapper;

    private ByteTransferProgress progress;

    public ProgressReader(Reader wrapper) {
        this(wrapper, new ByteTransferProgress(0));
    }

    public ProgressReader(Reader wrapper, long end) {
        this(wrapper, new ByteTransferProgress(end));
    }

    public ProgressReader(Reader wrapper, ByteTransferProgress progress) {
        this.wrapper = Valid.notNull(wrapper, "wrapper reader is null");
        this.progress = progress;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        return wrapper.read(target);
    }

    @Override
    public int read() throws IOException {
        int read = wrapper.read();
        if (read != -1) {
            progress.accept(read);
        }
        return read;
    }

    @Override
    public int read(char[] chars) throws IOException {
        int read = wrapper.read(chars);
        if (read != -1) {
            progress.accept(read);
        }
        return read;
    }

    @Override
    public int read(char[] chars, int off, int len) throws IOException {
        int read = wrapper.read(chars, off, len);
        if (read != -1) {
            progress.accept(read);
        }
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        long skip = wrapper.skip(n);
        progress.accept(skip);
        return skip;
    }

    @Override
    public boolean ready() throws IOException {
        return wrapper.ready();
    }

    @Override
    public boolean markSupported() {
        return wrapper.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        wrapper.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        wrapper.reset();
        progress.reset();
    }

    @Override
    public void close() throws IOException {
        wrapper.close();
        progress.finish();
    }

    /**
     * 开始
     */
    public void start() {
        progress.start();
    }

    public void finish() {
        this.finish(false);
    }

    /**
     * 完成
     *
     * @param error 是否失败
     */
    public void finish(boolean error) {
        progress.finish(error);
    }

    public ByteTransferProgress getProgress() {
        return progress;
    }

    public void setProgress(ByteTransferProgress progress) {
        this.progress = progress;
    }

}

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

import cn.orionsec.kit.lang.support.progress.ByteTransferProgress;
import cn.orionsec.kit.lang.utils.Valid;

import java.io.IOException;
import java.io.InputStream;

/**
 * 有进度回调的 inputStream
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 22:12
 */
public class ProgressInputStream extends InputStream {

    private final InputStream wrapper;

    private ByteTransferProgress progress;

    public ProgressInputStream(InputStream wrapper) {
        this(wrapper, new ByteTransferProgress(0));
    }

    public ProgressInputStream(InputStream wrapper, long end) {
        this(wrapper, new ByteTransferProgress(end));
    }

    public ProgressInputStream(InputStream wrapper, ByteTransferProgress progress) {
        this.wrapper = Valid.notNull(wrapper, "wrapper input stream is null");
        this.progress = progress;
    }

    @Override
    public int read() throws IOException {
        int read = wrapper.read();
        if (read != -1) {
            progress.accept(1);
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = wrapper.read(b);
        if (read != -1) {
            progress.accept(read);
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = wrapper.read(b, off, len);
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
    public int available() throws IOException {
        return wrapper.available();
    }

    @Override
    public void close() throws IOException {
        wrapper.close();
        progress.finish();
    }

    @Override
    public synchronized void mark(int readLimit) {
        wrapper.mark(readLimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        wrapper.reset();
        progress.reset();
    }

    @Override
    public boolean markSupported() {
        return wrapper.markSupported();
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

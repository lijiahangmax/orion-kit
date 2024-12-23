/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
import java.io.OutputStream;

/**
 * 有进度回调的 outputStream
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:27
 */
public class ProgressOutputStream extends OutputStream {

    private final OutputStream wrapper;

    private ByteTransferProgress progress;

    public ProgressOutputStream(OutputStream wrapper) {
        this(wrapper, new ByteTransferProgress(0));
    }

    public ProgressOutputStream(OutputStream wrapper, long end) {
        this(wrapper, new ByteTransferProgress(end));
    }

    public ProgressOutputStream(OutputStream wrapper, ByteTransferProgress progress) {
        this.wrapper = Valid.notNull(wrapper, "wrapper output stream is null");
        this.progress = progress;
    }

    @Override
    public void write(int b) throws IOException {
        wrapper.write(b);
        progress.accept(1);
    }

    @Override
    public void write(byte[] b) throws IOException {
        wrapper.write(b);
        progress.accept(b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        wrapper.write(b, off, len);
        progress.accept(len);
    }

    @Override
    public void flush() throws IOException {
        wrapper.flush();
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

    /**
     * 设置结束
     *
     * @param end 结束
     */
    public void setEnd(long end) {
        progress.setEnd(end);
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

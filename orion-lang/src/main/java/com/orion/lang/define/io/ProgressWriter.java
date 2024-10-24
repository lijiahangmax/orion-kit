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
package com.orion.lang.define.io;

import com.orion.lang.support.progress.ByteTransferProgress;
import com.orion.lang.utils.Valid;

import java.io.IOException;
import java.io.Writer;

/**
 * 有进度回调的 writer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:50
 */
public class ProgressWriter extends Writer {

    private final Writer wrapper;

    private ByteTransferProgress progress;

    public ProgressWriter(Writer wrapper) {
        this(wrapper, new ByteTransferProgress(0));
    }

    public ProgressWriter(Writer wrapper, long end) {
        this(wrapper, new ByteTransferProgress(end));
    }

    public ProgressWriter(Writer wrapper, ByteTransferProgress progress) {
        this.wrapper = Valid.notNull(wrapper, "wrapper writer is null");
        this.progress = progress;
    }

    @Override
    public void write(int c) throws IOException {
        wrapper.write(c);
        progress.accept(1);
    }

    @Override
    public void write(char[] chars) throws IOException {
        wrapper.write(chars);
        progress.accept(chars.length);
    }

    @Override
    public void write(char[] chars, int off, int len) throws IOException {
        wrapper.write(chars);
        progress.accept(len);
    }

    @Override
    public void write(String str) throws IOException {
        wrapper.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        wrapper.write(str, off, len);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return wrapper.append(csq);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return wrapper.append(csq, start, end);
    }

    @Override
    public Writer append(char c) throws IOException {
        return wrapper.append(c);
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

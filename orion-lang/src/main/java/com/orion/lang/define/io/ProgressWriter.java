package com.orion.lang.define.io;

import com.orion.lang.support.progress.ByteTransferProgress;
import com.orion.lang.utils.Valid;

import java.io.IOException;
import java.io.Writer;

/**
 * 有进度条的 writer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:50
 */
public class ProgressWriter extends Writer {

    private Writer wrapper;

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

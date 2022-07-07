package com.orion.lang.define.io;

import com.orion.lang.support.progress.ByteTransferProgress;
import com.orion.lang.utils.Valid;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 有进度条的 outputStream
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:27
 */
public class ProgressOutputStream extends OutputStream {

    private OutputStream wrapper;

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

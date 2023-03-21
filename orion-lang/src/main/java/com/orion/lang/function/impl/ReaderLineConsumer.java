package com.orion.lang.function.impl;

import com.orion.lang.constant.Const;
import com.orion.lang.define.Console;
import com.orion.lang.function.Functions;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * 流 Consumer -> 行 Consumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/20 21:43
 */
public class ReaderLineConsumer implements Consumer<InputStream> {

    /**
     * 编码格式
     */
    private String charset;

    /**
     * 缓冲区大小
     */
    private int bufferSize;

    /**
     * lineConsumer
     */
    private Consumer<String> lineConsumer;

    public ReaderLineConsumer() {
        this(Functions.emptyConsumer());
    }

    public ReaderLineConsumer(Consumer<String> lineConsumer) {
        this.charset = Const.UTF_8;
        this.bufferSize = Const.BUFFER_KB_8;
        this.lineConsumer = lineConsumer;
    }

    public ReaderLineConsumer charset(String charset) {
        this.charset = charset;
        return this;
    }

    public ReaderLineConsumer bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public ReaderLineConsumer lineConsumer(Consumer<String> lineConsumer) {
        Valid.notNull(lineConsumer, "line consumer is null");
        this.lineConsumer = lineConsumer;
        return this;
    }

    public ReaderLineConsumer lineConsumer(Consumer<String> lineConsumer, String charset) {
        Valid.notNull(lineConsumer, "line consumer is null");
        this.lineConsumer = lineConsumer;
        this.charset = charset;
        return this;
    }

    public static ReaderLineConsumer printer() {
        return printer(Const.UTF_8);
    }

    /**
     * 获取实现打印的 ReaderLineConsumer
     *
     * @param charset charset
     * @return ReaderLineConsumer
     */
    public static ReaderLineConsumer printer(String charset) {
        return new ReaderLineConsumer(Console::trace).charset(charset);
    }

    @Override
    public void accept(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset), bufferSize);
            String line;
            while ((line = reader.readLine()) != null) {
                lineConsumer.accept(line);
            }
        } catch (IOException e) {
            if (!Const.STREAM_CLOSE.equals(e.getMessage())) {
                throw Exceptions.ioRuntime(e);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}

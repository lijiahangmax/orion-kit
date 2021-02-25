package com.orion.function.impl;

import com.orion.constant.Const;
import com.orion.function.FunctionConst;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * 流 Consumer -> 行 Consumer
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/20 21:43
 */
public class ReaderLineConsumer implements Consumer<InputStream> {

    private static final ReaderLineConsumer DEFAULT_PRINT = new ReaderLineConsumer(FunctionConst.getPrintConsumer());

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
        this(FunctionConst.getEmptyConsumer());
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

    /**
     * 获取实现打印的 ReaderLineConsumer
     *
     * @return ReaderLineConsumer
     */
    public static ReaderLineConsumer getDefaultPrint() {
        return DEFAULT_PRINT;
    }

    @Override
    public void accept(InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset), bufferSize);
            String line;
            while ((line = reader.readLine()) != null) {
                lineConsumer.accept(line);
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

}

package com.orion.function.impl;

import com.orion.constant.Const;
import com.orion.function.FunctionConst;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;

/**
 * 流 BiConsumer -> 行 BiConsumer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/20 18:32
 */
public class ReaderLineBiConsumer<T> implements BiConsumer<T, InputStream> {

    private static ReaderLineBiConsumer<?> DEFAULT_PRINT_1 = new ReaderLineBiConsumer<>(FunctionConst.getPrint1BiConsumer());
    private static ReaderLineBiConsumer<?> DEFAULT_PRINT_2 = new ReaderLineBiConsumer<>(FunctionConst.getPrint2BiConsumer());
    private static ReaderLineBiConsumer<?> DEFAULT_PRINT = new ReaderLineBiConsumer<>(FunctionConst.getPrintBiConsumer());

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
    private BiConsumer<T, String> lineConsumer;

    public ReaderLineBiConsumer() {
        this(FunctionConst.getEmptyBiConsumer());
    }

    public ReaderLineBiConsumer(BiConsumer<T, String> lineConsumer) {
        this.charset = Const.UTF_8;
        this.bufferSize = Const.BUFFER_KB_8;
        this.lineConsumer = lineConsumer;
    }

    public ReaderLineBiConsumer<T> charset(String charset) {
        this.charset = charset;
        return this;
    }

    public ReaderLineBiConsumer<T> bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public ReaderLineBiConsumer<T> lineConsumer(BiConsumer<T, String> lineConsumer) {
        Valid.notNull(lineConsumer, "line consumer is null");
        this.lineConsumer = lineConsumer;
        return this;
    }

    public ReaderLineBiConsumer<T> lineConsumer(BiConsumer<T, String> lineConsumer, String charset) {
        Valid.notNull(lineConsumer, "line consumer is null");
        this.lineConsumer = lineConsumer;
        this.charset = charset;
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <T> ReaderLineBiConsumer<T> getDefaultPrint1() {
        return (ReaderLineBiConsumer<T>) DEFAULT_PRINT_1;
    }

    @SuppressWarnings("unchecked")
    public static <T> ReaderLineBiConsumer<T> getDefaultPrint2() {
        return (ReaderLineBiConsumer<T>) DEFAULT_PRINT_2;
    }

    @SuppressWarnings("unchecked")
    public static <T> ReaderLineBiConsumer<T> getDefaultPrint() {
        return (ReaderLineBiConsumer<T>) DEFAULT_PRINT;
    }

    @Override
    public void accept(T t, InputStream input) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset), bufferSize);
            String line;
            while ((line = reader.readLine()) != null) {
                lineConsumer.accept(t, line);
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

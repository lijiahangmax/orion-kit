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
package cn.orionsec.kit.lang.function.impl;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.Console;
import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;

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

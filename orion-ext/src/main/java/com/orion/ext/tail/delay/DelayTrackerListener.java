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
package com.orion.ext.tail.delay;

import com.orion.ext.tail.handler.DataHandler;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Valid;

import java.io.File;
import java.io.IOException;

/**
 * 延时文件追踪器 byte
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/16 12:11
 */
public class DelayTrackerListener extends AbstractDelayTracker {

    /**
     * 数据处理器
     */
    private final DataHandler handler;

    /**
     * 缓冲区
     */
    private final byte[] buffer;

    public DelayTrackerListener(String tailFile, DataHandler handler) {
        this(new File(tailFile), handler);
    }

    public DelayTrackerListener(File tailFile, DataHandler handler) {
        super(tailFile);
        this.handler = Valid.notNull(handler, "data handler is null");
        this.buffer = new byte[Const.BUFFER_KB_8];
    }

    @Override
    protected void read() throws IOException {
        int len;
        while ((len = reader.read(buffer)) != -1) {
            handler.read(buffer, len, this);
        }
    }

}

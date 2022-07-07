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
    private DataHandler handler;

    /**
     * 缓冲区
     */
    private byte[] buffer;

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
        int len = reader.read(buffer);
        handler.read(buffer, len, this);
    }

}

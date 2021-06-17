package com.orion.tail.mode;

/**
 * 启动时文件偏移 处理模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/17 19:07
 */
public enum FileOffsetMode {

    /**
     * 跳过字节 tail -c
     */
    BYTE,

    /**
     * 跳过行 tail -n
     */
    LINE

}

package com.orion.tail.mode;

/**
 * 运行时文件减少 处理模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/23 18:15
 */
public enum FileMinusMode {

    /**
     * 关闭
     */
    CLOSE,

    /**
     * 从文件当前位置读取
     */
    CURRENT,

    /**
     * 重新开始
     */
    RESUME,

    /**
     * 从length - offset开始
     */
    OFFSET,

    /**
     * 抛出异常
     */
    THROWS

}

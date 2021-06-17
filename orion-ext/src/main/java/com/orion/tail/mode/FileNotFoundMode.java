package com.orion.tail.mode;

/**
 * 启动时文件未找到 处理模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/23 18:09
 */
public enum FileNotFoundMode {

    /**
     * 关闭
     */
    CLOSE,

    /**
     * 等待
     */
    WAIT,

    /**
     * 等待一定的时间
     */
    WAIT_TIMES,

    /**
     * 等待一定的次数
     */
    WAIT_COUNT,

    /**
     * 抛出异常
     */
    THROWS

}

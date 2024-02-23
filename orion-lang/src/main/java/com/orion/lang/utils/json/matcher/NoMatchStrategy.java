package com.orion.lang.utils.json.matcher;

/**
 * 未匹配处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/11 11:59
 */
public enum NoMatchStrategy {

    /**
     * 保留占位符
     */
    KEEP,

    /**
     * 使用空字符串
     */
    EMPTY,

    /**
     * 抛出异常
     */
    THROW,

}

package com.orion.utils.time.ago;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/29 11:30
 */
public abstract class Ago {

    /**
     * 原时间
     */
    protected Date source;

    /**
     * 对比的时间
     */
    protected Date target;

    /**
     * 配置
     */
    protected DateAgoHint hint;

    public Ago(Date target) {
        this(new Date(), target, null);
    }

    public Ago(Date source, Date target) {
        this(source, target, null);
    }

    public Ago(Date target, DateAgoHint hint) {
        this(new Date(), target, hint);
    }

    public Ago(Date source, Date target, DateAgoHint hint) {
        this.source = source;
        this.target = target;
        this.hint = hint;
    }

    /**
     * 设置配置
     *
     * @param hint hint
     * @return this
     */
    public Ago hint(DateAgoHint hint) {
        this.hint = hint;
        return this;
    }

    /**
     * 获取时间前后
     *
     * @return 时间前后
     */
    public abstract String ago();

    @Override
    public String toString() {
        return ago();
    }
}

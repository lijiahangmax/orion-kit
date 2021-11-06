package com.orion.support.progress;

/**
 * 进度条
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 21:34
 */
public interface Progress {

    /**
     * 开始
     */
    void start();

    /**
     * 完成
     */
    void finish();

    /**
     * 完成
     *
     * @param error 是否失败
     */
    void finish(boolean error);

    /**
     * 重置
     */
    void reset();

    /**
     * 获取当前进度
     *
     * @return 当前进度
     */
    double getProgress();

}

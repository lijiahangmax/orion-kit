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
package cn.orionsec.kit.lang.support.timeout;

import java.io.Closeable;
import java.util.List;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/16 10:09
 */
public interface TimeoutChecker<T extends TimeoutEndpoint> extends Runnable, Closeable {

    /**
     * 添加任务
     *
     * @param task task
     */
    void addTask(T task);

    /**
     * 获取全部任务
     *
     * @return tasks
     */
    List<T> getTasks();

    /**
     * 清空全部任务
     */
    void clear();

    /**
     * 任务是否为空
     *
     * @return isEmpty
     */
    boolean isEmpty();

    /**
     * 是否运行中
     *
     * @return isRun
     */
    boolean isRun();

}

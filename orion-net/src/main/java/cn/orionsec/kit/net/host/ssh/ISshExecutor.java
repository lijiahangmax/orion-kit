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
package cn.orionsec.kit.net.host.ssh;

import cn.orionsec.kit.net.host.IHostExecutor;

/**
 * SSH 执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public interface ISshExecutor extends IHostExecutor {

    /**
     * 设置环境变量
     * 这里只支持设置 /etc/ssh/sshd_config AcceptEnv 的环境变量
     * 否则只能使用 export LANG="en_US"; 来设置
     *
     * @param key   key
     * @param value value
     */
    void env(String key, String value);

    /**
     * 设置环境变量
     * 这里只支持设置 /etc/ssh/sshd_config AcceptEnv 的环境变量
     * 否则只能使用 export LANG="en_US"; 来设置
     *
     * @param key   key
     * @param value value
     */
    void env(byte[] key, byte[] value);

    /**
     * 是否启用 x11forwarding
     *
     * @param enable 是否启用
     */
    void x11Forward(boolean enable);

    /**
     * 启用代理转发
     *
     * @param enable 是否启用
     */
    void setAgentForwarding(boolean enable);

    /**
     * 发送信号量
     *
     * @param signal 信号
     */
    void sendSignal(String signal);

}

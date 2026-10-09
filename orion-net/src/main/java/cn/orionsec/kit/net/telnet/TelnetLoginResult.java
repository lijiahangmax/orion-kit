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
package cn.orionsec.kit.net.telnet;

/**
 * Telnet 登录判定结果
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/2
 */
public final class TelnetLoginResult {

    /**
     * 失败原因
     */
    private final TelnetLoginFailReason reason;

    /**
     * 探测到的命令提示符
     */
    private final String detectedPrompt;

    /**
     * 是否未定
     */
    private final boolean undecided;

    private TelnetLoginResult(TelnetLoginFailReason reason, String detectedPrompt, boolean undecided) {
        this.reason = reason;
        this.detectedPrompt = detectedPrompt;
        this.undecided = undecided;
    }

    public static TelnetLoginResult success(String detectedPrompt) {
        return new TelnetLoginResult(null, detectedPrompt, false);
    }

    public static TelnetLoginResult failure(TelnetLoginFailReason reason) {
        return new TelnetLoginResult(reason, null, false);
    }

    public static TelnetLoginResult undecided() {
        return new TelnetLoginResult(null, null, true);
    }

    public boolean isFailure() {
        return reason != null;
    }

    public boolean isUndecided() {
        return undecided;
    }

    public TelnetLoginFailReason getReason() {
        return reason;
    }

    public String getDetectedPrompt() {
        return detectedPrompt;
    }

}

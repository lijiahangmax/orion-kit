/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.crypto.enums;

/**
 * 工作模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/3 10:08
 */
public enum WorkingMode {

    /**
     * None
     */
    NONE("None"),

    /**
     * 电子密码本模式 AES DES 3DES
     */
    ECB("ECB"),

    /**
     * 密码分组连接模式 AES DES 3DES
     */
    CBC("CBC"),

    /**
     * 密文反馈模式 AES DES 3DES
     */
    CFB("CFB"),

    /**
     * 输出反馈模式 AES DES 3DES
     */
    OFB("OFB"),

    /**
     * 计数器模式 AES DES 3DES
     */
    FTP("FTP"),

    /**
     * 加密认证模式 AES
     */
    GCM("GCM");

    private final String mode;

    WorkingMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}

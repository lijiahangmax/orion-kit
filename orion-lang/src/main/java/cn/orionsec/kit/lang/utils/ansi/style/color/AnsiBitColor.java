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
package cn.orionsec.kit.lang.utils.ansi.style.color;

import cn.orionsec.kit.lang.utils.Valid;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI bit 颜色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 16:40
 */
public abstract class AnsiBitColor implements AnsiColor {

    private final String code;

    public AnsiBitColor(String code) {
        this.code = code;
    }

    /**
     * 生成颜色码
     *
     * @param elem elem
     * @return color
     */
    protected static String color(byte type, byte bit, int... elem) {
        StringBuilder sb = new StringBuilder()
                .append(type)
                .append(JOIN)
                .append(bit)
                .append(JOIN);
        for (int i = 0, len = elem.length; i < len; i++) {
            int color = elem[i];
            Valid.isTrue(color >= 0 && color <= 255, "color must be between 0 and 255");
            sb.append(color);
            if (i < len - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return CSI_PREFIX + code + SGR_SUFFIX;
    }

}

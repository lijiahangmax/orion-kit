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
package cn.orionsec.kit.lang.utils.ansi.style;

import cn.orionsec.kit.lang.utils.collect.Lists;

import java.util.List;

import static cn.orionsec.kit.lang.utils.ansi.AnsiConst.*;

/**
 * ANSI 样式链
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/28 17:00
 */
public class AnsiStyleChain implements AnsiStyle {

    /**
     * 样式
     */
    private final List<AnsiStyle> styles;

    public AnsiStyleChain(AnsiStyle originStyle) {
        this.styles = Lists.of(originStyle);
    }

    /**
     * 创建样式链
     *
     * @param originStyle originStyle
     * @return 样式链
     */
    public static AnsiStyleChain create(AnsiStyle originStyle) {
        return new AnsiStyleChain(originStyle);
    }

    @Override
    public AnsiStyleChain and(AnsiStyle style) {
        styles.add(style);
        return this;
    }

    @Override
    public String getCode() {
        return Lists.join(Lists.map(styles, AnsiStyle::getCode), JOIN);
    }

    @Override
    public String toString() {
        return CSI_PREFIX + this.getCode() + SGR_SUFFIX;
    }

}

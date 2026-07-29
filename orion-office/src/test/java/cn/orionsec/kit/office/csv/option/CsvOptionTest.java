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
package cn.orionsec.kit.office.csv.option;

import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.office.csv.type.CsvEscapeMode;
import org.junit.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * {@link CsvOption} 配置项测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvOptionTest {

    @Test
    public void testConstants() {
        assertEquals(CsvEscapeMode.DOUBLE_QUALIFIER.getMode(), CsvOption.ESCAPE_MODE_DOUBLED);
        assertEquals(CsvEscapeMode.BACKSLASH.getMode(), CsvOption.ESCAPE_MODE_BACKSLASH);
        assertEquals(10, CsvOption.INITIAL_COLUMN_COUNT);
        assertEquals(50, CsvOption.INITIAL_COLUMN_BUFFER_SIZE);
        assertEquals(1, CsvOption.UNICODE);
        assertEquals(2, CsvOption.OCTAL);
        assertEquals(3, CsvOption.DECIMAL);
        assertEquals(4, CsvOption.HEX);
    }

    @Test
    public void testDefaultValues() {
        CsvOption option = new CsvOption();
        assertEquals(Letters.QUOTE, option.getTextQualifier());
        assertTrue(option.isUseTextQualifier());
        assertEquals(Letters.COMMA, option.getDelimiter());
        assertEquals(Letters.NULL, option.getLineDelimiter());
        assertEquals(Letters.POUND, option.getComment());
        assertEquals(CsvOption.ESCAPE_MODE_DOUBLED, option.getEscapeMode());
        assertEquals(StandardCharsets.UTF_8, option.getCharset());
        assertFalse(option.isTrim());
        assertFalse(option.isUseCustomLineDelimiter());
    }

    @Test
    public void testSerializable() {
        assertTrue(new CsvOption() instanceof Serializable);
    }

    @Test
    public void testSetterChaining() {
        CsvOption option = new CsvOption();
        CsvOption result = option.setTextQualifier('\'')
                .setUseTextQualifier(false)
                .setDelimiter(';')
                .setComment('!')
                .setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH)
                .setCharset(StandardCharsets.ISO_8859_1)
                .setTrim(true);
        // 链式调用返回自身
        assertSame(option, result);
        assertEquals('\'', option.getTextQualifier());
        assertFalse(option.isUseTextQualifier());
        assertEquals(';', option.getDelimiter());
        assertEquals('!', option.getComment());
        assertEquals(CsvOption.ESCAPE_MODE_BACKSLASH, option.getEscapeMode());
        assertEquals(StandardCharsets.ISO_8859_1, option.getCharset());
        assertTrue(option.isTrim());
    }

    @Test
    public void testSetLineDelimiterCustomFlag() {
        CsvOption option = new CsvOption();
        assertFalse(option.isUseCustomLineDelimiter());
        option.setLineDelimiter('|');
        assertEquals('|', option.getLineDelimiter());
        assertTrue(option.isUseCustomLineDelimiter());
        // 设置为 NULL 后自定义行边界符关闭
        option.setLineDelimiter(Letters.NULL);
        assertFalse(option.isUseCustomLineDelimiter());
    }

    @Test
    public void testSetEscapeModeByEnum() {
        CsvOption option = new CsvOption();
        option.setEscapeMode(CsvEscapeMode.BACKSLASH);
        assertEquals(CsvOption.ESCAPE_MODE_BACKSLASH, option.getEscapeMode());
        option.setEscapeMode(CsvEscapeMode.DOUBLE_QUALIFIER);
        assertEquals(CsvOption.ESCAPE_MODE_DOUBLED, option.getEscapeMode());
    }

}

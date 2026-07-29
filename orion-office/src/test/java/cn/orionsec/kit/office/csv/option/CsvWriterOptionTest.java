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
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * {@link CsvWriterOption} 配置项测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvWriterOptionTest {

    @Test
    public void testDefaultValues() {
        CsvWriterOption option = new CsvWriterOption();
        assertFalse(option.isForceQualifier());
        // 父类默认值
        assertEquals(Letters.QUOTE, option.getTextQualifier());
        assertTrue(option.isUseTextQualifier());
        assertEquals(Letters.COMMA, option.getDelimiter());
        assertEquals(StandardCharsets.UTF_8, option.getCharset());
        assertFalse(option.isTrim());
    }

    @Test
    public void testDelimiterConstructor() {
        CsvWriterOption option = new CsvWriterOption(';');
        assertEquals(';', option.getDelimiter());
        assertEquals(StandardCharsets.UTF_8, option.getCharset());
    }

    @Test
    public void testDelimiterCharsetConstructor() {
        CsvWriterOption option = new CsvWriterOption('\t', StandardCharsets.UTF_16);
        assertEquals('\t', option.getDelimiter());
        assertEquals(StandardCharsets.UTF_16, option.getCharset());
    }

    @Test
    public void testSetForceQualifier() {
        CsvWriterOption option = new CsvWriterOption();
        CsvWriterOption result = option.setForceQualifier(true);
        assertSame(option, result);
        assertTrue(option.isForceQualifier());
    }

    @Test
    public void testToReaderOption() {
        CsvWriterOption writerOption = new CsvWriterOption(';', StandardCharsets.ISO_8859_1);
        writerOption.setTextQualifier('\'')
                .setUseTextQualifier(false)
                .setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH)
                .setTrim(true);
        CsvReaderOption readerOption = writerOption.toReaderOption();
        // 基础配置继承
        assertEquals(';', readerOption.getDelimiter());
        assertEquals(StandardCharsets.ISO_8859_1, readerOption.getCharset());
        assertEquals('\'', readerOption.getTextQualifier());
        assertFalse(readerOption.isUseTextQualifier());
        assertEquals(CsvOption.ESCAPE_MODE_BACKSLASH, readerOption.getEscapeMode());
        assertTrue(readerOption.isTrim());
    }

}

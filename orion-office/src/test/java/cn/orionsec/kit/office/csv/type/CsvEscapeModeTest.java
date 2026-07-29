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
package cn.orionsec.kit.office.csv.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * {@link CsvEscapeMode} 枚举测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvEscapeModeTest {

    @Test
    public void testValues() {
        CsvEscapeMode[] values = CsvEscapeMode.values();
        assertEquals(2, values.length);
        assertEquals(CsvEscapeMode.DOUBLE_QUALIFIER, values[0]);
        assertEquals(CsvEscapeMode.BACKSLASH, values[1]);
    }

    @Test
    public void testGetMode() {
        assertEquals(1, CsvEscapeMode.DOUBLE_QUALIFIER.getMode());
        assertEquals(2, CsvEscapeMode.BACKSLASH.getMode());
    }

    @Test
    public void testValueOf() {
        assertSame(CsvEscapeMode.DOUBLE_QUALIFIER, CsvEscapeMode.valueOf("DOUBLE_QUALIFIER"));
        assertSame(CsvEscapeMode.BACKSLASH, CsvEscapeMode.valueOf("BACKSLASH"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        CsvEscapeMode.valueOf("UNKNOWN");
    }

}

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
package cn.orionsec.kit.office.csv.core;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.office.csv.option.CsvOption;
import cn.orionsec.kit.office.csv.option.CsvReaderOption;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * {@link CsvReader} 核心读取器测试 (内存字符串解析)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvReaderTest {

    @Test
    public void testParseSimple() throws Exception {
        CsvReader reader = CsvReader.parse("a,b,c\n1,2,3");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a", "b", "c"}, reader.getRow());
        assertEquals(3, reader.getCurrentColumnCount());
        assertEquals(0, reader.getCurrentIndex());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"1", "2", "3"}, reader.getRow());
        assertEquals(1, reader.getCurrentIndex());
        assertFalse(reader.readRow());
        reader.close();
    }

    @Test
    public void testGetColumnValue() throws Exception {
        CsvReader reader = CsvReader.parse("x,y");
        reader.readRow();
        assertEquals("x", reader.get(0));
        assertEquals("y", reader.get(1));
        // 越界返回空字符串
        assertEquals(Strings.EMPTY, reader.get(2));
        assertEquals(Strings.EMPTY, reader.get(-1));
        reader.close();
    }

    @Test
    public void testQualifiedColumn() throws Exception {
        CsvReader reader = CsvReader.parse("\"a,x\",b");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a,x", "b"}, reader.getRow());
        // 限定符标识
        assertTrue(reader.isQualified(0));
        assertFalse(reader.isQualified(1));
        assertFalse(reader.isQualified(9));
        reader.close();
    }

    @Test
    public void testDoubledQualifierEscape() throws Exception {
        CsvReader reader = CsvReader.parse("\"a\"\"b\",c");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a\"b", "c"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testBackslashEscapeInQualifier() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH);
        CsvReader reader = CsvReader.parse("\"a\\\"b\",c", option);
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a\"b", "c"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testBackslashEscapeWithoutQualifier() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setUseTextQualifier(false);
        option.setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH);
        CsvReader reader = CsvReader.parse("a\\,b,c", option);
        assertTrue(reader.readRow());
        // 转义的分隔符不拆分列
        assertArrayEquals(new String[]{"a,b", "c"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testUnicodeEscape() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH);
        CsvReader reader = CsvReader.parse("\"\\u0041b\",x", option);
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"Ab", "x"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testCommentsSkipped() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setUseComments(true);
        CsvReader reader = CsvReader.parse("#comment\na,b", option);
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a", "b"}, reader.getRow());
        assertFalse(reader.readRow());
        reader.close();
    }

    @Test
    public void testCommentsAsDataByDefault() throws Exception {
        // 默认不启用注释, 注释行作为数据读取
        CsvReader reader = CsvReader.parse("#comment\na,b");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"#comment"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testCustomDelimiter() throws Exception {
        CsvReader reader = CsvReader.parse("a;b;c", ';');
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a", "b", "c"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testCustomLineDelimiter() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setLineDelimiter('|');
        CsvReader reader = CsvReader.parse("a,b|c,d", option);
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a", "b"}, reader.getRow());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"c", "d"}, reader.getRow());
        assertFalse(reader.readRow());
        reader.close();
    }

    @Test
    public void testTrim() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setTrim(true);
        CsvReader reader = CsvReader.parse(" a , b ", option);
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a", "b"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testNoTrimByDefault() throws Exception {
        CsvReader reader = CsvReader.parse(" a ,b");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{" a ", "b"}, reader.getRow());
        reader.close();
    }

    @Test
    public void testReadHeaders() throws Exception {
        CsvReader reader = CsvReader.parse("id,name\n1,tom");
        assertTrue(reader.readHeaders());
        assertEquals(2, reader.getHeaderCount());
        assertArrayEquals(new String[]{"id", "name"}, reader.getHeaders());
        assertEquals("id", reader.getHeader(0));
        assertEquals("name", reader.getHeader(1));
        assertEquals(Strings.EMPTY, reader.getHeader(5));
        assertEquals(0, reader.getHeaderIndex("id"));
        assertEquals(1, reader.getHeaderIndex("name"));
        assertEquals(-1, reader.getHeaderIndex("miss"));
        // 读取数据行并按标题取值
        assertTrue(reader.readRow());
        assertEquals("1", reader.get("id"));
        assertEquals("tom", reader.get("name"));
        assertEquals(0, reader.getCurrentIndex());
        reader.close();
    }

    @Test
    public void testSetHeaders() throws Exception {
        CsvReader reader = CsvReader.parse("1,tom");
        reader.setHeaders(new String[]{"id", "name"});
        assertEquals(2, reader.getHeaderCount());
        assertTrue(reader.readRow());
        assertEquals("tom", reader.get("name"));
        // 置空标题
        reader.setHeaders(null);
        assertEquals(0, reader.getHeaderCount());
        reader.close();
    }

    @Test
    public void testSkipRecord() throws Exception {
        CsvReader reader = CsvReader.parse("a,b\nc,d");
        assertTrue(reader.skipRecord());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"c", "d"}, reader.getRow());
        // 跳过的行不计数
        assertEquals(0, reader.getCurrentIndex());
        reader.close();
    }

    @Test
    public void testSkipEmptyRows() throws Exception {
        CsvReader reader = CsvReader.parse("a\n\nb");
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a"}, reader.getRow());
        assertTrue(reader.readRow());
        // 默认跳过空行
        assertArrayEquals(new String[]{"b"}, reader.getRow());
        assertFalse(reader.readRow());
        reader.close();
    }

    @Test
    public void testRawRow() throws Exception {
        CsvReader reader = CsvReader.parse("a,b\nc,d");
        assertTrue(reader.readRow());
        assertEquals("a,b", reader.getRawRow());
        reader.close();
    }

    @Test
    public void testSkipRawRow() throws Exception {
        CsvReaderOption option = new CsvReaderOption();
        option.setSkipRawRow(true);
        CsvReader reader = CsvReader.parse("a,b\nc,d", option);
        assertTrue(reader.readRow());
        assertEquals(Strings.EMPTY, reader.getRawRow());
        reader.close();
    }

    @Test
    public void testGetRowReturnsClone() throws Exception {
        CsvReader reader = CsvReader.parse("a,b");
        reader.readRow();
        String[] row = reader.getRow();
        row[0] = "modified";
        assertEquals("a", reader.get(0));
        reader.close();
    }

    @Test
    public void testClear() throws Exception {
        CsvReader reader = CsvReader.parse("abc,def\nx,y");
        assertTrue(reader.clear());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"x", "y"}, reader.getRow());
        reader.close();
    }

    @Test(expected = RuntimeException.class)
    public void testClosedReadThrows() throws Exception {
        CsvReader reader = CsvReader.parse("a,b");
        reader.close();
        reader.readRow();
    }

    @Test(expected = RuntimeException.class)
    public void testParseBlankThrows() {
        CsvReader.parse(" ");
    }

    @Test
    public void testCloseIdempotent() {
        CsvReader reader = CsvReader.parse("a,b");
        reader.close();
        // 重复关闭无异常
        reader.close();
    }

    @Test
    public void testOptionGetterSetter() {
        CsvReader reader = new CsvReader(new StringReader("a,b"));
        assertNotNull(reader.getOption());
        CsvReaderOption option = new CsvReaderOption(';');
        reader.setOption(option);
        assertSame(option, reader.getOption());
        reader.close();
    }

    @Test
    public void testMoreColumnsThanInitial() throws Exception {
        // 超过初始化列数 10 列触发扩容
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(i);
        }
        CsvReader reader = CsvReader.parse(sb.toString());
        assertTrue(reader.readRow());
        assertEquals(15, reader.getCurrentColumnCount());
        assertEquals("14", reader.get(14));
        reader.close();
    }

}

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

import cn.orionsec.kit.office.csv.option.CsvOption;
import cn.orionsec.kit.office.csv.option.CsvWriterOption;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * {@link CsvWriter} 核心写入器测试 (StringWriter 输出)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvWriterTest {

    @Test
    public void testWriteLine() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"a", "b", "c"});
        writer.flush();
        assertEquals("a,b,c\n", out.toString());
        writer.close();
    }

    @Test
    public void testWriteMultipleColumns() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.write("a");
        writer.write("b");
        writer.newLine();
        writer.flush();
        assertEquals("a,b\n", out.toString());
        writer.close();
    }

    @Test
    public void testDelimiterInContentQuoted() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"a,b", "c"});
        writer.flush();
        assertEquals("\"a,b\",c\n", out.toString());
        writer.close();
    }

    @Test
    public void testQualifierEscapeDoubled() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"a\"b"});
        writer.flush();
        assertEquals("\"a\"\"b\"\n", out.toString());
        writer.close();
    }

    @Test
    public void testQualifierEscapeBackslash() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriterOption option = new CsvWriterOption();
        option.setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH);
        CsvWriter writer = new CsvWriter(out, option);
        writer.writeLine(new String[]{"a\"b"});
        writer.flush();
        assertEquals("\"a\\\"b\"\n", out.toString());
        writer.close();
    }

    @Test
    public void testBackslashEscapeWithoutQualifier() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriterOption option = new CsvWriterOption();
        option.setUseTextQualifier(false);
        option.setEscapeMode(CsvOption.ESCAPE_MODE_BACKSLASH);
        CsvWriter writer = new CsvWriter(out, option);
        writer.writeLine(new String[]{"a,b", "c"});
        writer.flush();
        assertEquals("a\\,b,c\n", out.toString());
        writer.close();
    }

    @Test
    public void testForceQualifier() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriterOption option = new CsvWriterOption();
        option.setForceQualifier(true);
        CsvWriter writer = new CsvWriter(out, option);
        writer.writeLine(new String[]{"a", "b"});
        writer.flush();
        assertEquals("\"a\",\"b\"\n", out.toString());
        writer.close();
    }

    @Test
    public void testWriteComment() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeComment("hello");
        writer.flush();
        assertEquals("#hello\n", out.toString());
        writer.close();
    }

    @Test
    public void testCommentCharInFirstColumnQuoted() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"#a", "b"});
        writer.flush();
        assertEquals("\"#a\",b\n", out.toString());
        writer.close();
    }

    @Test
    public void testEmptyFirstColumnQuoted() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"", "b"});
        writer.flush();
        assertEquals("\"\",b\n", out.toString());
        writer.close();
    }

    @Test
    public void testNullContentAsEmpty() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{null, "b"});
        writer.flush();
        assertEquals("\"\",b\n", out.toString());
        writer.close();
    }

    @Test
    public void testTrimOption() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriterOption option = new CsvWriterOption();
        option.setTrim(true);
        CsvWriter writer = new CsvWriter(out, option);
        writer.write(" a ");
        writer.newLine();
        writer.flush();
        assertEquals("a\n", out.toString());
        writer.close();
    }

    @Test
    public void testPreserveSpacesQuoted() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        // 默认保留空格 首尾空格触发限定符
        writer.write(" a");
        writer.newLine();
        writer.flush();
        assertEquals("\" a\"\n", out.toString());
        writer.close();
    }

    @Test
    public void testCustomLineDelimiter() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriterOption option = new CsvWriterOption();
        option.setLineDelimiter(';');
        CsvWriter writer = new CsvWriter(out, option);
        writer.writeLine(new String[]{"a", "b"});
        writer.writeLine(new String[]{"c"});
        writer.flush();
        assertEquals("a,b;c;", out.toString());
        writer.close();
    }

    @Test
    public void testCustomDelimiter() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out, '\t');
        writer.writeLine(new String[]{"a", "b"});
        writer.flush();
        assertEquals("a\tb\n", out.toString());
        writer.close();
    }

    @Test
    public void testWriteLineEmptyValuesNoOutput() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(null);
        writer.writeLine(new String[0]);
        writer.flush();
        assertEquals("", out.toString());
        writer.close();
    }

    @Test
    public void testRoundTripWithReader() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.writeLine(new String[]{"a\"b", "c,d", "e"});
        writer.writeLine(new String[]{"1", "2", "3"});
        writer.flush();
        writer.close();
        // 写出内容可被 reader 还原
        CsvReader reader = CsvReader.parse(out.toString());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"a\"b", "c,d", "e"}, reader.getRow());
        assertTrue(reader.readRow());
        assertArrayEquals(new String[]{"1", "2", "3"}, reader.getRow());
        reader.close();
    }

    @Test(expected = RuntimeException.class)
    public void testClosedWriteThrows() throws Exception {
        StringWriter out = new StringWriter();
        CsvWriter writer = new CsvWriter(out);
        writer.close();
        writer.write("a");
    }

    @Test
    public void testCloseIdempotent() {
        CsvWriter writer = new CsvWriter(new StringWriter());
        writer.close();
        // 重复关闭无异常
        writer.close();
    }

    @Test
    public void testOptionGetterSetter() {
        CsvWriter writer = new CsvWriter(new StringWriter());
        assertNotNull(writer.getOption());
        CsvWriterOption option = new CsvWriterOption(';');
        writer.setOption(option);
        assertSame(option, writer.getOption());
        writer.close();
    }

}

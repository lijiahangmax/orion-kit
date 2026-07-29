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
package cn.orionsec.kit.office.csv.split;

import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link CsvColumnSplit} 列拆分测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvColumnSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File createCsv(String name, String content) throws Exception {
        File file = folder.newFile(name);
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    public void testSplitColumns() throws Exception {
        File source = createCsv("source.csv", "a1,b1,c1\na2,b2,c2\na3,b3,c3\n");
        File target = folder.newFile("target.csv");
        CsvColumnSplit split = new CsvColumnSplit(new CsvExt(source), 2, 0);
        try (OutputStream out = new FileOutputStream(target)) {
            split.split(out, true);
        } finally {
            split.close();
        }
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("c1,a1", lines.get(0));
        assertEquals("c2,a2", lines.get(1));
        assertEquals("c3,a3", lines.get(2));
    }

    @Test
    public void testSplitWithHeaderAndSkip() throws Exception {
        File source = createCsv("source.csv", "id,name,age\n1,tom,18\n2,jerry,20\n");
        File target = folder.newFile("target.csv");
        CsvColumnSplit split = new CsvColumnSplit(new CsvExt(source), 1, 2);
        try (OutputStream out = new FileOutputStream(target)) {
            // 跳过源表头 写入自定义表头
            split.skip().header("name", "age").split(out, true);
        } finally {
            split.close();
        }
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("name,age", lines.get(0));
        assertEquals("tom,18", lines.get(1));
        assertEquals("jerry,20", lines.get(2));
    }

    @Test
    public void testSplitSingleColumn() throws Exception {
        File source = createCsv("source.csv", "a1,b1\na2,b2\n");
        File target = folder.newFile("target.csv");
        CsvColumnSplit split = new CsvColumnSplit(new CsvExt(source), 1);
        try (OutputStream out = new FileOutputStream(target)) {
            split.bufferLine(1).split(out, true);
        } finally {
            split.close();
        }
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals("b1", lines.get(0));
        assertEquals("b2", lines.get(1));
    }

    @Test
    public void testGetters() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvColumnSplit split = new CsvColumnSplit(new CsvExt(source), 1, 0);
        assertArrayEquals(new int[]{1, 0}, split.getColumns());
        assertNotNull(split.getReader());
        assertSame(split, split.bufferLine(10));
        split.close();
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyColumnsThrows() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvArrayReader reader = new CsvExt(source).arrayReader();
        try {
            new CsvColumnSplit(reader);
        } finally {
            reader.close();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testNullReaderThrows() {
        new CsvColumnSplit((CsvArrayReader) null, 1);
    }

}

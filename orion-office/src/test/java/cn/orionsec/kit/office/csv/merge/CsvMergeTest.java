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
package cn.orionsec.kit.office.csv.merge;

import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * {@link CsvMerge} 行合并测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvMergeTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File createCsv(String name, String content) throws Exception {
        File file = folder.newFile(name);
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    public void testMergeTwoFiles() throws Exception {
        File file1 = createCsv("1.csv", "a1,b1\na2,b2\n");
        File file2 = createCsv("2.csv", "c1,d1\nc2,d2\n");
        File target = folder.newFile("merged.csv");
        CsvArrayReader reader1 = new CsvExt(file1).arrayReader();
        CsvArrayReader reader2 = new CsvExt(file2).arrayReader();
        CsvMerge merge = new CsvMerge(new CsvArrayWriter(target));
        merge.merge(reader1).merge(reader2).close();
        reader1.close();
        reader2.close();
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(4, lines.size());
        assertEquals("a1,b1", lines.get(0));
        assertEquals("a2,b2", lines.get(1));
        assertEquals("c1,d1", lines.get(2));
        assertEquals("c2,d2", lines.get(3));
    }

    @Test
    public void testMergeWithHeader() throws Exception {
        File file1 = createCsv("1.csv", "1,tom\n");
        File file2 = createCsv("2.csv", "2,jerry\n");
        File target = folder.newFile("merged.csv");
        CsvArrayReader reader1 = new CsvExt(file1).arrayReader();
        CsvArrayReader reader2 = new CsvExt(file2).arrayReader();
        CsvMerge merge = new CsvMerge(new CsvArrayWriter(target));
        merge.header("id", "name").merge(reader1).merge(reader2).close();
        reader1.close();
        reader2.close();
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("id,name", lines.get(0));
        assertEquals("1,tom", lines.get(1));
        assertEquals("2,jerry", lines.get(2));
    }

    @Test
    public void testMergeSkipRows() throws Exception {
        File file1 = createCsv("1.csv", "id,name\n1,tom\n");
        File file2 = createCsv("2.csv", "id,name\n2,jerry\n");
        File target = folder.newFile("merged.csv");
        CsvArrayReader reader1 = new CsvExt(file1).arrayReader();
        CsvArrayReader reader2 = new CsvExt(file2).arrayReader();
        CsvMerge merge = new CsvMerge(new CsvArrayWriter(target));
        // 每个文件跳过表头
        merge.header("id", "name")
                .skipRows()
                .merge(reader1)
                .skipRows(1)
                .merge(reader2)
                .close();
        reader1.close();
        reader2.close();
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("id,name", lines.get(0));
        assertEquals("1,tom", lines.get(1));
        assertEquals("2,jerry", lines.get(2));
    }

    @Test
    public void testGetWriterAndBufferLine() throws Exception {
        File target = folder.newFile("merged.csv");
        CsvArrayWriter writer = new CsvArrayWriter(target);
        CsvMerge merge = new CsvMerge(writer);
        assertSame(writer, merge.getWriter());
        assertSame(merge, merge.bufferLine(10));
        merge.close();
    }

}

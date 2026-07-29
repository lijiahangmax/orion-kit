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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link CsvRowSplit} 行拆分测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvRowSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File createCsv(String name, String content) throws Exception {
        File file = folder.newFile(name);
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    public void testSplitToTargetFiles() throws Exception {
        File source = createCsv("source.csv", "a1,b1\na2,b2\na3,b3\na4,b4\na5,b5\n");
        File t1 = folder.newFile("t1.csv");
        File t2 = folder.newFile("t2.csv");
        File t3 = folder.newFile("t3.csv");
        CsvRowSplit split = new CsvRowSplit(new CsvExt(source), 2);
        split.autoClose(true);
        split.target(t1, t2, t3);
        split.split();
        split.close();
        split.getReader().close();
        List<String> lines1 = Files.readAllLines(t1.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, lines1.size());
        assertEquals("a1,b1", lines1.get(0));
        assertEquals("a2,b2", lines1.get(1));
        List<String> lines2 = Files.readAllLines(t2.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, lines2.size());
        assertEquals("a3,b3", lines2.get(0));
        assertEquals("a4,b4", lines2.get(1));
        List<String> lines3 = Files.readAllLines(t3.toPath(), StandardCharsets.UTF_8);
        assertEquals(1, lines3.size());
        assertEquals("a5,b5", lines3.get(0));
    }

    @Test
    public void testSplitTargetPathGenerate() throws Exception {
        File source = createCsv("source.csv", "a1,b1\na2,b2\na3,b3\n");
        File dir = folder.newFolder("out");
        CsvRowSplit split = new CsvRowSplit(new CsvExt(source), 2);
        split.targetPath(dir.getAbsolutePath(), "part");
        split.split();
        split.close();
        split.getReader().close();
        File part1 = new File(dir, "part1.csv");
        File part2 = new File(dir, "part2.csv");
        assertTrue(part1.exists());
        assertTrue(part2.exists());
        List<String> lines1 = Files.readAllLines(part1.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, lines1.size());
        assertEquals("a1,b1", lines1.get(0));
        List<String> lines2 = Files.readAllLines(part2.toPath(), StandardCharsets.UTF_8);
        assertEquals(1, lines2.size());
        assertEquals("a3,b3", lines2.get(0));
    }

    @Test
    public void testSplitWithHeaderAndColumns() throws Exception {
        File source = createCsv("source.csv", "id,name\n1,tom\n2,jerry\n");
        File target = folder.newFile("target.csv");
        CsvRowSplit split = new CsvRowSplit(new CsvExt(source), 10);
        split.autoClose(true);
        split.target(target);
        // 跳过源表头 只保留 name 列
        split.skip().columns(1).header("name").split();
        split.close();
        split.getReader().close();
        List<String> lines = Files.readAllLines(target.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("name", lines.get(0));
        assertEquals("tom", lines.get(1));
        assertEquals("jerry", lines.get(2));
    }

    @Test
    public void testGetters() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvRowSplit split = new CsvRowSplit(new CsvExt(source), 3);
        assertEquals(3, split.getLimit());
        assertNotNull(split.getReader());
        assertSame(split, split.suffix("txt"));
        split.getReader().close();
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidLimitThrows() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvArrayReader reader = new CsvExt(source).arrayReader();
        try {
            // limit 小于 0 抛出异常
            new CsvRowSplit(reader, -1);
        } finally {
            reader.close();
        }
    }

}

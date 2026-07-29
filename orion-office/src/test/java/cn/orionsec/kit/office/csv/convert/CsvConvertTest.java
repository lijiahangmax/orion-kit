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
package cn.orionsec.kit.office.csv.convert;

import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.convert.adapter.ExcelAdapter;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * {@link CsvConvert} 转换器测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CsvConvertTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File createCsv(String name, String content) throws Exception {
        File file = folder.newFile(name);
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<CsvConvert> constructor = CsvConvert.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }

    @Test
    public void testExcelAdapterByCsvExt() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        ExcelAdapter adapter = CsvConvert.excelAdapter(new CsvExt(source));
        assertNotNull(adapter);
        assertNotNull(adapter.getSheet());
        assertNotNull(adapter.getReader());
        adapter.getReader().close();
        adapter.close();
    }

    @Test
    public void testExcelAdapterByReader() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvArrayReader reader = new CsvExt(source).arrayReader();
        ExcelAdapter adapter = CsvConvert.excelAdapter(reader);
        assertNotNull(adapter);
        assertSame(reader, adapter.getReader());
        reader.close();
        adapter.close();
    }

}

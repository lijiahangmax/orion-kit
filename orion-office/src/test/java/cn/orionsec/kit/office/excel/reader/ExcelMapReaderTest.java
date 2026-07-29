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
package cn.orionsec.kit.office.excel.reader;

import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import cn.orionsec.kit.office.excel.writer.ExcelMapWriter;
import cn.orionsec.kit.office.excel.writer.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * ExcelMapWriter 写入 ExcelMapReader 读取 回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelMapReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File file;

    private static Map<String, Object> createRow(long id, String name, String balance, String date) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("balance", new BigDecimal(balance));
        map.put("date", Dates.parse(date, "yyyy-MM-dd HH:mm:ss"));
        return map;
    }

    @Before
    public void prepareFile() throws Exception {
        this.file = folder.newFile("map-round-trip.xlsx");
        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(createRow(1L, "商店一", "10.5", "2023-08-15 12:30:45"));
        rows.add(createRow(2L, "商店二", "20.25", "2023-08-16 06:15:30"));
        rows.add(createRow(3L, "shop3", "30.75", "2023-08-17 18:45:15"));
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelMapWriter<String, Object> writer = build.createMapWriter("map");
        writer.option(0, "id", ExcelFieldType.NUMBER)
                .option(1, "name", ExcelFieldType.TEXT)
                .option(2, "balance", ExcelFieldType.NUMBER)
                .option(3, "date", ExcelFieldType.DATE)
                .headers("id", "名称", "余额", "时间")
                .addRows(rows);
        build.write(file);
        build.close();
    }

    @Test
    public void testMapRoundTrip() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelMapReader<String, Object> reader = ExcelMapReader.create(workbook, workbook.getSheetAt(0));
        reader.linked()
                .option(0, "id", ExcelReadType.LONG)
                .option(1, "name", ExcelReadType.TEXT)
                .option(2, "balance", ExcelReadType.DECIMAL)
                .option(3, "date", ExcelReadType.DATE);
        reader.init().skip().read();
        List<MutableMap<String, Object>> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        MutableMap<String, Object> first = rows.get(0);
        Assert.assertEquals(1L, first.get("id"));
        Assert.assertEquals("商店一", first.get("name"));
        Assert.assertEquals(0, new BigDecimal("10.5").compareTo((BigDecimal) first.get("balance")));
        Assert.assertEquals("2023-08-15 12:30:45", Dates.format((Date) first.get("date"), "yyyy-MM-dd HH:mm:ss"));
        MutableMap<String, Object> last = rows.get(2);
        Assert.assertEquals(3L, last.get("id"));
        Assert.assertEquals("shop3", last.get("name"));
        Assert.assertEquals(0, new BigDecimal("30.75").compareTo((BigDecimal) last.get("balance")));
        reader.close();
    }

    @Test
    public void testMapReaderDefaultValue() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelMapReader<String, Object> reader = ExcelMapReader.create(workbook, workbook.getSheetAt(0));
        // 列 9 不存在 命中默认值
        reader.option(0, "id", ExcelReadType.LONG)
                .option(9, "missing", ExcelReadType.TEXT, "默认值")
                .option(8, "nullKey", ExcelReadType.DATE);
        reader.init().skip().read();
        List<MutableMap<String, Object>> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals("默认值", rows.get(0).get("missing"));
        // 无默认值 nullPutKey 默认 true
        Assert.assertTrue(rows.get(0).containsKey("nullKey"));
        Assert.assertNull(rows.get(0).get("nullKey"));
        reader.close();
    }

    @Test
    public void testMapReaderConsumer() {
        Workbook workbook = Excels.openWorkbook(file);
        List<Object> names = new ArrayList<>();
        ExcelMapReader<String, Object> reader = ExcelMapReader.create(workbook, workbook.getSheetAt(0),
                (java.util.function.Consumer<MutableMap<String, Object>>) map -> names.add(map.get("name")));
        reader.option(1, "name", ExcelReadType.TEXT);
        reader.init().skip().read();
        Assert.assertEquals(3, names.size());
        Assert.assertEquals("商店一", names.get(0));
        Assert.assertEquals("shop3", names.get(2));
        reader.close();
    }

}

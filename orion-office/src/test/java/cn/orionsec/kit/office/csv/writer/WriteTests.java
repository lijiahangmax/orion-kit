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
package cn.orionsec.kit.office.csv.writer;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.csv.core.CsvWriter;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/29 16:23
 */
public class WriteTests {

    private static List<String[]> array = IntStream.rangeClosed(1, 20).mapToObj(i -> {
        String[] str = new String[6];
        str[0] = i + "";
        str[1] = 10000 + i + "";
        str[2] = "   " + Strings.randomChars(5);
        str[3] = Strings.randomChars(5) + (i % 2 == 0 ? "\"" : "\\");
        str[4] = Dates.format(Dates.date(System.currentTimeMillis() + (i * 10000)));
        return str;
    }).collect(Collectors.toList());

    private static List<Map<String, Object>> map = IntStream.rangeClosed(1, 20).mapToObj(i -> {
        Map<String, Object> m = new HashMap<>();
        m.put("num", i);
        m.put("id", 10000 + i);
        m.put("name", Strings.randomChars(5) + (i % 2 == 0 ? "\"" : "\\"));
        m.put("date", Dates.format(Dates.date(System.currentTimeMillis() + (i * 10000))));
        m.put("desc", (i % 2 == 0 ? null : "k"));
        return m;
    }).collect(Collectors.toList());

    private static List<ExportUser> bean = IntStream.rangeClosed(1, 20).mapToObj(i -> {
        ExportUser user = new ExportUser();
        user.setNum(i);
        user.setId(10000 + i);
        user.setName(Strings.randomChars(5) + (i % 2 == 0 ? "\"" : "\\"));
        user.setDate(Dates.format(Dates.date(System.currentTimeMillis() + (i * 10000))));
        user.setDesc((i % 2 == 0 ? null : "k"));
        return user;
    }).collect(Collectors.toList());

    @Test
    public void arrayTests() {
        CsvWriter writer = new CsvWriter("C:\\Users\\ljh15\\Desktop\\csv\\array.csv");
        writer.getOption().setTrim(true);
        CsvArrayWriter cw = new CsvArrayWriter(writer);
        cw.capacity(8)
                .mapping(5, 0)
                .mapping(6, 1)
                .mapping(7, 10, "def")
                .addComment("comment")
                .skip()
                .addRow(null)
                .addRows(array);
        cw.flush();
        cw.close();
    }

    @Test
    public void mapTests() {
        CsvWriter writer = new CsvWriter("C:\\Users\\ljh15\\Desktop\\csv\\map.csv");
        writer.getOption().setTrim(true);
        CsvMapWriter<String, Object> cw = new CsvMapWriter<>(writer);
        cw.capacity(6)
                .mapping(0, "num")
                .mapping(1, "id")
                .mapping(2, "date")
                .mapping(3, "name")
                .mapping(4, "id")
                .mapping(5, "desc", "def")
                .skip()
                .headers("a", "b", "c", "d", "e")
                .skip()
                .addRows(map);
        cw.flush();
        cw.close();
    }

    @Test
    public void beanTests() {
        CsvBeanWriter<ExportUser> writer = new CsvBeanWriter<>("C:\\Users\\ljh15\\Desktop\\csv\\bean.csv", ExportUser.class);
        writer.getOption().setTrim(true);
        writer.defaultValue("desc", "def")
                .skip()
                .headers("a", "b", "c", "d", "e")
                .skip()
                .addRows(bean);
        writer.flush();
        writer.close();
    }

    @Test
    public void lambdaTests() {
        CsvLambdaWriter<ExportUser> writer = new CsvLambdaWriter<>("C:\\Users\\ljh15\\Desktop\\csv\\lambda.csv");
        writer.getOption().setTrim(true);
        writer.capacity(5)
                .mapping(0, ExportUser::getNum)
                .mapping(1, ExportUser::getId)
                .mapping(2, ExportUser::getName)
                .mapping(3, ExportUser::getDate)
                .mapping(4, ExportUser::getDesc, "desc")
                .skip()
                .headers("a", "b", "c", "d", "e")
                .skip()
                .addRows(bean);
        writer.flush();
        writer.close();
    }

}

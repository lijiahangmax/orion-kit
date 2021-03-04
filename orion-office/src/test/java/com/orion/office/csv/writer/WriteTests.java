package com.orion.office.csv.writer;

import com.orion.office.csv.core.CsvWriter;
import com.orion.utils.Strings;
import com.orion.utils.time.Dates;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ljh15
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
        new CsvArrayWriter(writer)
                .capacity(8)
                .mapping(5, new Integer(0))
                .mapping(6, new Integer(1))
                .mapping(7, new Integer(10))
                .defaultValue(10, "def")
                .addComment("comment")
                .skip()
                .addRow(null)
                .addRows(array)
                .flush()
                .close();
    }

    @Test
    public void mapTests() {
        CsvWriter writer = new CsvWriter("C:\\Users\\ljh15\\Desktop\\csv\\map.csv");
        writer.getOption().setTrim(true);
        new CsvMapWriter<String, Object>(writer)
                .capacity(6)
                .mapping(0, "num")
                .mapping(1, "id")
                .mapping(2, "date")
                .mapping(3, "name")
                .mapping(4, "id")
                .mapping(5, "desc")
                .defaultValue("desc", "def")
                .skip()
                .headers("a", "b", "c", "d", "e")
                .skip()
                .addRows(map)
                .flush()
                .close();
    }

    @Test
    public void beanTests() {
        CsvBeanWriter<ExportUser> writer = new CsvBeanWriter<>("C:\\Users\\ljh15\\Desktop\\csv\\bean.csv", ExportUser.class);
        writer.getOption().setTrim(true);
        writer.defaultValue("desc", "def")
                .skip()
                .headers("a", "b", "c", "d", "e")
                .skip()
                .addRows(bean)
                .flush()
                .close();
    }

}

package com.orion.office.excel.writer;

import com.orion.office.excel.writer.exporting.ExcelExport;
import com.orion.utils.Spells;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;
import com.orion.utils.time.Dates;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/30 9:40
 */
public class ExportUserTests {

    @Test
    public void export() {
        List<ExportUser> list = IntStream.rangeClosed(1, 200).mapToObj((i) -> {
            if (i == 2) {
                return null;
            }
            ExportUser e = new ExportUser();
            e.setId((long) (10000 + i));
            e.setName(Strings.randomChars(40));
            e.setDate(Dates.date(System.currentTimeMillis() + (i * 10000)));
            e.setCompute("A" + i);
            e.setAge(Randoms.randomInt(14, 50));
            String realName = Strings.randomChars(3);
            String spell = Spells.getSpell(realName);
            e.setBlogUrl("http://" + spell + ".com");
            e.setEmailUrl(spell + "@orion.com");
            e.setFile("C:/Users/ljh15/Desktop/list.html");
            e.setRealName(realName);
            return e;
        }).collect(Collectors.toList());
        list.add(null);
        list.add(null);

        new ExcelExport<>(ExportUser.class)
                .cleanHeaderStyle(2)
                .cleanColumnStyle(4)
                .init()
                .headers("二级标题", "", "day", "", "用户信息", "", "", "")
                .skip(1)
                .skipNullRows(false)
                .addRows(list)
                .merge(1, 0, 1)
                .merge(1, 4, 7)
                .write("C:\\Users\\ljh15\\Desktop\\1.xlsx")
                .close();
    }

}

package com.orion.office.excel.writer;

import com.orion.office.excel.option.FooterOption;
import com.orion.office.excel.option.HeaderOption;
import com.orion.office.excel.option.PrintOption;
import com.orion.office.excel.option.PropertiesOption;
import com.orion.office.excel.type.ExcelFieldType;
import com.orion.office.excel.type.ExcelPaperType;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;
import com.orion.utils.time.Dates;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/13 16:02
 */
public class WriteTests {

    private ExcelWriterBuilder build = new ExcelWriterBuilder();

    private List<Object[]> array = IntStream.rangeClosed(1, 50).mapToObj(i -> {
        Object[] array = new Object[6];
        array[0] = 1000 + i;
        array[1] = "   " + Strings.randomChars(3) + "  ";
        array[2] = Strings.format("ABS({})", i);
        array[3] = Dates.date(System.currentTimeMillis() + i * 100000);
        array[4] = Randoms.randomDouble(1000000, 9000000);
        array[5] = "disable";
        return array;
    }).collect(Collectors.toList());

    private List<Map<String, Object>> map = IntStream.rangeClosed(1, 50).mapToObj(i -> {
        Map<String, Object> m = new HashMap<>();
        m.put("id", 1000 + i);
        m.put("name", "   " + Strings.randomChars(3) + "  ");
        m.put("formula", Strings.format("ABS({})", i));
        m.put("date", Dates.date(System.currentTimeMillis() + i * 100000));
        m.put("balance", Randoms.randomDouble(1000000, 9000000));
        m.put("hidden", "disable");
        return m;
    }).collect(Collectors.toList());

    private List<WriteUser> bean = IntStream.rangeClosed(1, 50).mapToObj(i -> {
        WriteUser user = new WriteUser();
        user.setId(1000L + i);
        user.setName("   " + Strings.randomChars(3) + "  ");
        user.setFormula(Strings.format("ABS({})", i));
        user.setDate(Dates.date(System.currentTimeMillis() + i * 100000));
        user.setBalance(BigDecimal.valueOf(Randoms.randomDouble(1000000, 9000000)));
        user.setDisable("disable");
        return user;
    }).collect(Collectors.toList());

    @Before
    public void setProperties() {
        PropertiesOption option = new PropertiesOption()
                .setAuthor("李佳航")
                .setDescription("writeTests")
                .setKeywords("poi writer");
        build.properties(option);
    }

    @Test
    public void arrayTests1() {
        ExcelArrayWriter<Object> writer = build.createArrayWriter("array");
        writer.option(0, 0, ExcelFieldType.NUMBER)
                .option(1, 2)
                .option(2, 1, ExcelFieldType.FORMULA)
                .option(3, 3, ExcelFieldType.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss")
                .option(4, 4, ExcelFieldType.DECIMAL_FORMAT, "#,###$")
                .option(5, 5)
                .hidden(5)
                .width(12)
                .width(3, 20)
                .titleHeight(15)
                .headerHeight(30)
                .rowHeight(20)
                .title("测试 array 1", 2)
                .headers("id", "公式", "名称", "时间", "金钱", "隐藏")
                .freeze(3)
                .filter(2)
                .hidden(5)
                .trim()
                .headerUseRowStyle()
                .skip();
        CellStyle style = writer.createCellStyle();
        Font font = writer.createFont();
        font.setColor(Font.COLOR_RED);
        style.setFont(font);
        writer.style(0, style)
                .addRows(this.array);
        PrintOption option = new PrintOption()
                .setScale(50)
                .setAutoLimit(true)
                .setLimit(10)
                .setRepeat(2, 5)
                .setPaper(ExcelPaperType.A5);
        writer.print(option)
                .displayFormulas();
    }

    @Test
    public void mapTests1() {
        ExcelMapWriter<String, Object> writer = build.createMapWriter("map");
        writer.option("id", 0, ExcelFieldType.NUMBER)
                .option("formula", 1, ExcelFieldType.FORMULA)
                .option("name", 2)
                .option("date", 3, ExcelFieldType.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss")
                .option("balance", 4, ExcelFieldType.DECIMAL_FORMAT, "#,###$")
                .option("hidden", 5)
                .hidden(5)
                .width(12)
                .width(3, 20)
                .titleHeight(15)
                .headerHeight(30)
                .rowHeight(20)
                .title("测试 map 1", 2)
                .headers("id", "公式", "名称", "时间", "金钱", "隐藏")
                .freeze(3)
                .filter(2)
                .hidden(5)
                .trim()
                .columnUseDefaultStyle()
                .headerUseRowStyle()
                .skip();
        CellStyle style = writer.createCellStyle();
        Font font = writer.createFont();
        font.setColor(Font.COLOR_RED);
        style.setFont(font);
        writer.style(0, style)
                .addRows(this.map);
        HeaderOption option = new HeaderOption()
                .setLeft("left")
                .setCenter("中心")
                .setRight("right");
        writer.header(option)
                .displayRowColHeadings()
                .protect("123");
    }

    @Test
    public void beanTest1() {
        ExcelBeanWriter<WriteUser> writer = build.createBeanWriter("bean", WriteUser.class);
        writer.option("id", 0, ExcelFieldType.NUMBER)
                .option("formula", 1, ExcelFieldType.FORMULA)
                .option("name", 2)
                .option("date", 3, ExcelFieldType.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss")
                .option("balance", 4, ExcelFieldType.DECIMAL_FORMAT, "#,###$")
                .option("disable", 5)
                .hidden(5)
                .width(12)
                .width(3, 20)
                .titleHeight(15)
                .headerHeight(30)
                .rowHeight(20)
                .title("测试 bean 1", 2)
                .freeze(3)
                .filter(2)
                .hidden(5)
                .selected()
                .headerUseRowStyle();
        CellStyle style = writer.createCellStyle();
        Font font = writer.createFont();
        font.setColor(Font.COLOR_RED);
        style.setFont(font);
        writer.style(0, style)
                // 有样式
                .headers("id", "公式", "名称", "时间", "金钱", "隐藏")
                .skip()
                .addRows(this.bean);
        FooterOption option = new FooterOption()
                .setLeft("left")
                .setCenter("中心")
                .setRight("right");
        writer.footer(option)
                .displayGridLines();
    }

    @Test
    public void allTest() {
        this.arrayTests1();
        this.mapTests1();
        this.beanTest1();
    }

    @After
    public void write() {
        build.write("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    }

}

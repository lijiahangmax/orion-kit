package com.orion.office.excel.reader;

import com.orion.lang.define.Console;
import com.orion.lang.define.collect.MutableMap;
import com.orion.lang.utils.Objects1;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.type.ExcelReadType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/3 19:02
 */
public class ImportShopTest {

    private Workbook workbook = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\shop.xlsx");

    private Sheet sheet = workbook.getSheetAt(0);

    @Test
    public void testBean1() {
        new ExcelBeanReader<>(workbook, sheet, ImportShop.class, Console::trace)
                .trim()
                .init()
                .skip(2)
                .read(2)
                .skip(1)
                .read()
                .close();
    }

    @Test
    public void testBean2() {
        new ExcelBeanReader<>(workbook, sheet, ImportShop.class, Console::trace)
                .option(4, "comment", ExcelReadType.TEXT)
                .option(5, "businessFile", ExcelReadType.TEXT)
                .nullInvoke()
                .init()
                .skip(2)
                .read(2)
                .skip(1)
                .read()
                .close();
    }

    @Test
    public void testBeanIterator1() {
        ExcelBeanReader<ImportShop> reader = new ExcelBeanReader<>(workbook, sheet, ImportShop.class);
        ExcelReaderIterator<ImportShop> iterator = reader.init()
                .skip(2).iterator();
        for (ImportShop s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testBeanIterator2() {
        ExcelBeanReader<ImportShop> reader = new ExcelBeanReader<>(workbook, sheet, ImportShop.class);
        ExcelReaderIterator<ImportShop> iterator = reader
                .option(4, "comment", ExcelReadType.TEXT)
                .option(5, "businessFile", ExcelReadType.TEXT)
                .nullInvoke()
                .init()
                .skip(2)
                .iterator();
        for (ImportShop s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testArray1() {
        new ExcelArrayReader(workbook, sheet, Console::trace)
                .columns(0, 1, 2)
                .skip(2)
                .read(2)
                .skip(1)
                .read()
                .close();
    }

    @Test
    public void testArray2() {
        ExcelArrayReader reader = new ExcelArrayReader(workbook, sheet, Console::trace);
        ExcelReaderIterator<String[]> iterator = reader.columns(0, 1, 2)
                .skip(2)
                .iterator();
        for (String[] s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testMap1() {
        new ExcelMapReader<String, Object>(workbook, sheet, Console::trace)
                .linked()
                .option(0, "shopId", ExcelReadType.TEXT)
                .option(1, "shopName", ExcelReadType.TEXT)
                .option("createDate", new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"))
                .option(4, "picture", ExcelReadType.PICTURE)
                .option(4, "pictureValue", ExcelReadType.TEXT)
                .option(5, "linkAddress", ExcelReadType.LINK_ADDRESS)
                .option(5, "linkValue", ExcelReadType.TEXT)
                .option(5, "comment", ExcelReadType.COMMENT)
                .option("margin", new ImportFieldOption(6, ExcelReadType.DECIMAL, "#.####万元"))
                .defaultValue("createDate", "默认日期")
                .defaultValue("linkAddress", "默认地址")
                .defaultValue("picture", "默认图片")
                .defaultValue("comment", "默认批注")
                .init()
                .skip(2)
                .read(2)
                .skip(1)
                .trim()
                .read()
                .close();
    }

    @Test
    public void testMap2() {
        ExcelMapReader<String, Object> reader = new ExcelMapReader<>(workbook, sheet, Console::trace);
        ExcelReaderIterator<MutableMap<String, Object>> iterator = reader.linked()
                .option(0, "shopId", ExcelReadType.TEXT)
                .option(1, "shopName", ExcelReadType.TEXT)
                .option("createDate", new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"))
                .option(4, "picture", ExcelReadType.PICTURE)
                .option(4, "pictureValue", ExcelReadType.TEXT)
                .option(5, "linkAddress", ExcelReadType.LINK_ADDRESS)
                .option(5, "linkValue", ExcelReadType.TEXT)
                .option(5, "comment", ExcelReadType.COMMENT)
                .option("margin", new ImportFieldOption(6, ExcelReadType.DECIMAL, "#.####万元"))
                .defaultValue("createDate", "默认日期")
                .defaultValue("linkAddress", "默认地址")
                .defaultValue("picture", "默认图片")
                .defaultValue("comment", "默认批注")
                .init()
                .skip(2)
                .iterator();
        for (MutableMap<String, Object> s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testLambda1() {
        new ExcelLambdaReader<>(workbook, sheet, Console::trace, ImportShop::new)
                .<String, Long>option(0, ExcelReadType.TEXT, Long::valueOf, ImportShop::setShopId)
                .option(1, ExcelReadType.TEXT, ImportShop::setShopName)
                .option(new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"), ImportShop::setCreateDate)
                .option(4, ExcelReadType.PICTURE, ImportShop::setBusinessPicture)
                .option(5, ExcelReadType.LINK_ADDRESS, ImportShop::setComment)
                .option(new ImportFieldOption(6, ExcelReadType.DECIMAL, "#.####万元"), BigDecimal::doubleValue, ImportShop::setMargin)
                .init()
                .skip(3)
                .read(2)
                .skip(1)
                .trim()
                .read()
                .close();
    }

    @Test
    public void testLambda2() {
        ExcelLambdaReader<ImportShop> reader = new ExcelLambdaReader<>(workbook, sheet, Console::trace, ImportShop::new);
        reader.nullAddEmptyBean()
                .option(0, ExcelReadType.LONG, ImportShop::setShopId)
                .option(1, ExcelReadType.TEXT, ImportShop::setShopName)
                .option(new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"), ImportShop::setCreateDate)
                // .option(4, ExcelReadType.PICTURE, ImportShop::setBusinessPicture)
                .option(5, ExcelReadType.LINK_ADDRESS, ImportShop::setComment)
                .option(new ImportFieldOption(6, ExcelReadType.DECIMAL, "#.####万元"), BigDecimal::doubleValue, ImportShop::setMargin)
                .init()
                .skip(3)
                .trim();
        for (ImportShop shop : reader) {
            System.out.println(shop);
        }
    }

}

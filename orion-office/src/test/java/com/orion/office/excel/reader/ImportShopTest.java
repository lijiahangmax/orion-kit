package com.orion.office.excel.reader;

import com.orion.lang.Console;
import com.orion.lang.collect.MutableMap;
import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.type.ExcelReadType;
import com.orion.utils.Objects1;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/3 19:02
 */
public class ImportShopTest {

    private Workbook workbook = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\shop.xlsx");

    private Sheet sheet = workbook.getSheetAt(0);

    @Test
    public void testImport1() {
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
    public void testImport2() {
        ExcelReaderIterator<ImportShop> iterator = new ExcelBeanReader<>(workbook, sheet, ImportShop.class)
                .init()
                .skip(2).iterator();
        for (ImportShop s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        iterator.close();
    }

    @Test
    public void testImport3() {
        new ExcelBeanReader<>(workbook, sheet, ImportShop.class, Console::trace)
                .init()
                .option("comment", 4, ExcelReadType.TEXT)
                .option("businessFile", 5, ExcelReadType.TEXT)
                .nullInvoke()
                .skip(2)
                .read(2)
                .skip(1)
                .read()
                .close();
    }

    @Test
    public void testImport4() {
        ExcelReaderIterator<ImportShop> iterator = new ExcelBeanReader<>(workbook, sheet, ImportShop.class)
                .init()
                .option("comment", 4, ExcelReadType.TEXT)
                .option("businessFile", 5, ExcelReadType.TEXT)
                .nullInvoke()
                .skip(2)
                .iterator();
        for (ImportShop s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        iterator.close();
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
        ExcelReaderIterator<String[]> iterator = new ExcelArrayReader(workbook, sheet, Console::trace)
                .columns(0, 1, 2)
                .skip(2)
                .iterator();
        for (String[] s : iterator) {
            System.out.println(Objects1.toString(s));
        }
        iterator.close();
    }

    @Test
    public void testMap1() {
        new ExcelMapReader<String, Object>(workbook, sheet, Console::trace)
                .linked()
                .option("shopId", 0, ExcelReadType.TEXT)
                .option("shopName", 1, ExcelReadType.TEXT)
                .option("createDate", new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"))
                .option("picture", 4, ExcelReadType.PICTURE)
                .option("pictureValue", 4, ExcelReadType.TEXT)
                .option("linkAddress", 5, ExcelReadType.LINK_ADDRESS)
                .option("linkValue", 5, ExcelReadType.TEXT)
                .option("comment", 5, ExcelReadType.COMMENT)
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
        ExcelReaderIterator<MutableMap<String, Object>> iterator = new ExcelMapReader<String, Object>(workbook, sheet, Console::trace)
                .linked()
                .option("shopId", 0, ExcelReadType.TEXT)
                .option("shopName", 1, ExcelReadType.TEXT)
                .option("createDate", new ImportFieldOption(2, ExcelReadType.DATE, "yyyy年MM月dd日"))
                .option("picture", 4, ExcelReadType.PICTURE)
                .option("pictureValue", 4, ExcelReadType.TEXT)
                .option("linkAddress", 5, ExcelReadType.LINK_ADDRESS)
                .option("linkValue", 5, ExcelReadType.TEXT)
                .option("comment", 5, ExcelReadType.COMMENT)
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
        iterator.close();
    }

}

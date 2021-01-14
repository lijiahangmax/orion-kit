package com.orion.test.excel.reader;

import com.orion.excel.Excels;
import com.orion.excel.option.ImportFieldOption;
import com.orion.excel.reader.ExcelArrayReader;
import com.orion.excel.reader.ExcelImport;
import com.orion.excel.reader.ExcelMapReader;
import com.orion.excel.reader.ExcelReader;
import com.orion.excel.type.ExcelReadType;
import com.orion.lang.Console;
import com.orion.lang.collect.MutableMap;
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

    private Workbook workbook = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\export\\shop.xlsx");

    private Sheet sheet = workbook.getSheetAt(0);

    @Test
    public void testImport1() {
        new ExcelImport<>(workbook, sheet, ImportShop.class, Console::trace)
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
        ExcelReader<ImportShop> reader = new ExcelImport<>(workbook, sheet, ImportShop.class)
                .init()
                .skip(2);
        for (ImportShop s : reader) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testImport3() {
        new ExcelImport<>(workbook, sheet, ImportShop.class, Console::trace)
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
        ExcelReader<ImportShop> reader = new ExcelImport<>(workbook, sheet, ImportShop.class)
                .init()
                .option("comment", 4, ExcelReadType.TEXT)
                .option("businessFile", 5, ExcelReadType.TEXT)
                .nullInvoke()
                .skip(2);
        for (ImportShop s : reader) {
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
        ExcelReader<String[]> reader = new ExcelArrayReader(workbook, sheet, Console::trace)
                .columns(0, 1, 2)
                .skip(2);
        for (String[] s : reader) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

    @Test
    public void testMap1() {
        new ExcelMapReader<String>(workbook, sheet, Console::trace)
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
        ExcelReader<MutableMap<String, Object>> reader = new ExcelMapReader<String>(workbook, sheet, Console::trace)
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
                .skip(2);
        for (MutableMap<String, Object> s : reader) {
            System.out.println(Objects1.toString(s));
        }
        reader.close();
    }

}

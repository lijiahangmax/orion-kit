package com.orion.test.excel.importing;

import com.orion.excel.Excels;
import com.orion.excel.importing.ExcelImport;
import com.orion.lang.Console;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/3 19:02
 */
public class ImportShopTest {

    public static void main(String[] args) {
        Workbook workbook = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\export\\shop.xlsx");
        new ExcelImport<>(workbook, workbook.getSheetAt(0), ImportShop.class, Console::trace)
                .init()
                .skip(2)
                .read(2)
                .skip(1)
                .read()
                .close();
    }

}

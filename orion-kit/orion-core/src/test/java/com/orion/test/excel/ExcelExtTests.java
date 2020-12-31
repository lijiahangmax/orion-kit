package com.orion.test.excel;

import com.orion.excel.ExcelExt;
import com.orion.excel.Excels;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/15 18:28
 */
public class ExcelExtTests {

    @Test
    public void openHss() {
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user.xls", null));
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xls", "123"));
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xls", "1234"));
    }

    @Test
    public void openXss() {
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user.xlsx", null));
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xlsx", "123"));
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xlsx", "1234"));
    }

    @Test
    public void openStream() {
        System.out.println(Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user.xlsx", null));
        System.out.println(Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xlsx", "123"));
        System.out.println(Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\data\\user_password.xlsx", "1234"));
    }

    @Test
    public void open() {
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\1.xlsx", null).getClass());
        System.out.println(Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\1.xls", null).getClass());
    }

    @Test
    public void openExt() {
        System.out.println(new ExcelExt("C:\\Users\\ljh15\\Desktop\\data\\user.xlsx"));
        System.out.println(new ExcelExt("C:\\Users\\ljh15\\Desktop\\data\\user_password.xlsx"));
    }

    @Test
    public void writePassword() {
        String s = "C:\\Users\\ljh15\\Desktop\\data\\1.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Excels.write(workbook, s, "123");
    }

}

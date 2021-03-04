package com.orion.office.excel.split;

import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/19 10:24
 */
public class ColumnSplitTests {

    private Workbook w1 = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Workbook w2 = Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet sheet1 = w1.getSheetAt(2);
    private Sheet sheet2 = w2.getSheetAt(2);

    @Test
    public void single1() {
        new ExcelColumnSingleSplit(w1, sheet1, 1, 0, 2)
                .skip(3)
                .header("1", "0", "2")
                .split()
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void single2() {
        new ExcelColumnSingleSplit(w2, sheet2, 1, 0, 2)
                .header("11", "00", "22")
                .split()
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void multiSheet1() {
        new ExcelColumnMultiSheetSplit(w1, 2)
                .skip(3)
                .split(2, 1, 0)
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"})
                .split(new int[]{0, 2, 3, 4}, "123")
                .write("C:\\Users\\ljh15\\Desktop\\4.xlsx")
                .close();
    }

    @Test
    public void multiSheet2() {
        new ExcelColumnMultiSheetSplit(w1, "bean")
                .split(2, 1, 0)
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"})
                .split(new int[]{0, 2, 3, 4}, "123")
                .write("C:\\Users\\ljh15\\Desktop\\5.xlsx")
                .close();
    }


    @Test
    public void multi1() {
        new ExcelColumnMultiSplit(w1, "bean")
                .split(new int[]{2, 1, 0}, "C:\\Users\\ljh15\\Desktop\\split\\1.xlsx")
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"}, "C:\\Users\\ljh15\\Desktop\\split\\2.xlsx")
                .split(new int[]{0, 2, 3, 4}, "123", "C:\\Users\\ljh15\\Desktop\\split\\3.xlsx")
                .close();
    }

    @Test
    public void multi2() {
        new ExcelColumnMultiSplit(w1, "bean")
                .skip(3)
                .split(new int[]{2, 1, 0}, "C:\\Users\\ljh15\\Desktop\\split\\1.xlsx")
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"}, "C:\\Users\\ljh15\\Desktop\\split\\2.xlsx")
                .split(new int[]{0, 2, 3, 4}, "123", System.out)
                .close();
    }

}

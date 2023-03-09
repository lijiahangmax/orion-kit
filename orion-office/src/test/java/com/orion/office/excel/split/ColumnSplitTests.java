package com.orion.office.excel.split;

import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/19 10:24
 */
public class ColumnSplitTests {

    private final Workbook w1 = Excels.openWorkbook("C:\\Users\\lijiahang\\Desktop\\2.xlsx");
    private final Workbook w2 = Excels.openStreamingWorkbook("C:\\Users\\lijiahang\\Desktop\\2.xlsx");
    private final Sheet sheet1 = w1.getSheetAt(2);
    private final Sheet sheet2 = w2.getSheetAt(2);

    @Test
    public void single1() {
        new ExcelColumnSingleSplit(w1, sheet1, 1, 0, 2)
                .skip(3)
                .header("1", "0", "2")
                .split()
                .write("C:\\Users\\lijiahang\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void single2() {
        new ExcelColumnSingleSplit(w2, sheet2, 1, 0, 2)
                .header("11", "00", "22")
                .split()
                .write("C:\\Users\\lijiahang\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void multiSheet1() {
        new ExcelColumnMultiSheetSplit(w1, 2)
                .skip(3)
                .split(2, 1, 0)
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"})
                .split(new int[]{0, 2, 3, 4}, "123")
                .write("C:\\Users\\lijiahang\\Desktop\\4.xlsx")
                .close();
    }

    @Test
    public void multiSheet2() {
        new ExcelColumnMultiSheetSplit(w1, "bean")
                .split(2, 1, 0)
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"})
                .split(new int[]{0, 2, 3, 4}, "123")
                .write("C:\\Users\\lijiahang\\Desktop\\5.xlsx")
                .close();
    }

    @Test
    public void multi1() {
        new ExcelColumnMultiSplit(w1, "bean")
                .split(new int[]{2, 1, 0}, "C:\\Users\\lijiahang\\Desktop\\split\\1.xlsx")
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"}, "C:\\Users\\lijiahang\\Desktop\\split\\2.xlsx")
                .split(new int[]{0, 2, 3, 4}, "123", "C:\\Users\\lijiahang\\Desktop\\split\\3.xlsx")
                .close();
    }

    @Test
    public void multi2() {
        new ExcelColumnMultiSplit(w1, "bean")
                .skip(3)
                .split(new int[]{2, 1, 0}, "C:\\Users\\lijiahang\\Desktop\\split\\1.xlsx")
                .split(new int[]{0, 2, 3}, new String[]{"xx1", "xx2", "xx3"}, "C:\\Users\\lijiahang\\Desktop\\split\\2.xlsx")
                .split(new int[]{0, 2, 3, 4}, "123", System.out)
                .close();
    }

}

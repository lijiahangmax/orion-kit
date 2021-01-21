package com.orion.test.excel.split;

import com.orion.excel.Excels;
import com.orion.excel.split.ExcelRowSplit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/19 10:24
 */
public class RowSplitTests {

    private Workbook w1 = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Workbook w2 = Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet sheet1 = w1.getSheetAt(2);
    private Sheet sheet2 = w2.getSheetAt(2);

    @Test
    public void rowSplit1() {
        new ExcelRowSplit(sheet1, 5)
                .destPath("C:\\Users\\ljh15\\Desktop\\split", "sp")
                .header("1", "3", "4")
                .column(1, 3, 4)
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit2() {
        new ExcelRowSplit(sheet2, 5)
                .destPath("C:\\Users\\ljh15\\Desktop\\split", "sp")
                .header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit3() {
        new ExcelRowSplit(sheet2, 5)
                .dest("C:\\Users\\ljh15\\Desktop\\split\\1.xlsx")
                .header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

}

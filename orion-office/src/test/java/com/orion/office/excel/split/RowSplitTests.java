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
public class RowSplitTests {

    private Workbook w1 = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Workbook w2 = Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet sheet1 = w1.getSheetAt(2);
    private Sheet sheet2 = w2.getSheetAt(2);

    @Test
    public void rowSplit1() {
        ExcelRowSplit s = new ExcelRowSplit(sheet1, 5);
        s.destPath("C:\\Users\\ljh15\\Desktop\\split1", "sp");
        s.header("1", "3", "4")
                .columns(1, 3, 4)
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit2() {
        ExcelRowSplit s = new ExcelRowSplit(sheet2, 5);
        s.destPath("C:\\Users\\ljh15\\Desktop\\split2", "sp");
        s.header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit3() {
        ExcelRowSplit s = new ExcelRowSplit(sheet2, 5);
        s.dest("C:\\Users\\ljh15\\Desktop\\split3\\1.xlsx");
        s.header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

}

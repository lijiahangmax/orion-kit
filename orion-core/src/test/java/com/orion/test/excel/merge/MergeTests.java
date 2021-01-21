package com.orion.test.excel.merge;

import com.orion.excel.Excels;
import com.orion.excel.merge.ExcelMerge;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/18 11:34
 */
public class MergeTests {

    private Workbook w = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet s1 = w.getSheetAt(0);
    private Sheet s2 = w.getSheetAt(1);
    private Sheet s3 = w.getSheetAt(2);

    private Workbook w1 = Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet s11 = w1.getSheetAt(0);
    private Sheet s12 = w1.getSheetAt(1);
    private Sheet s13 = w1.getSheetAt(2);

    @Test
    public void mergeTest1() {
        new ExcelMerge(w, s2)
                .skip()
                .skipRows(3)
                .merge(s1)
                .skip()
                .skipRows(2)
                .merge(s3)
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void mergeTest2() {
        new ExcelMerge()
                .merge(s1)
                .skip()
                .merge(s2)
                .skip()
                .merge(s3)
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void mergeTest3() {
        new ExcelMerge()
                .merge(s11)
                .skip()
                .merge(s12)
                .skip()
                .merge(s13)
                .write("C:\\Users\\ljh15\\Desktop\\4.xlsx")
                .close();
    }

}

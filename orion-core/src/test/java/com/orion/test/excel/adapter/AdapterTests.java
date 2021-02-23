package com.orion.test.excel.adapter;

import com.orion.csv.writer.CsvArrayWriter;
import com.orion.excel.Excels;
import com.orion.excel.convert.ExcelConvert;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/23 14:44
 */
public class AdapterTests {

    @Test
    public void toCsv() {
        Sheet sheet = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\zb.xlsx").getSheetAt(0);
        CsvArrayWriter writer = new CsvArrayWriter("C:\\Users\\ljh15\\Desktop\\zb.csv");
        ExcelConvert.csvAdapter(sheet, writer).forNew().close();
    }

}

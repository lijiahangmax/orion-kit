package com.orion.office.excel.adapter;

import com.orion.office.csv.writer.CsvArrayWriter;
import com.orion.office.excel.Excels;
import com.orion.office.excel.convert.ExcelConvert;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;

/**
 * @author Jiahang Li
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

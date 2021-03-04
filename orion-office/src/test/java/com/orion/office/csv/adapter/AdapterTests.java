package com.orion.office.csv.adapter;

import com.orion.office.csv.CsvExt;
import com.orion.office.csv.convert.CsvConvert;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/23 14:52
 */
public class AdapterTests {

    @Test
    public void toXlsx() {
        CsvExt csvExt = new CsvExt("C:\\Users\\ljh15\\Desktop\\zb.csv");
        CsvConvert.excelAdapter(csvExt).forNew()
                .write("C:\\Users\\ljh15\\Desktop\\zb1.xlsx")
                .close();
    }

}

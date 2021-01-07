package com.orion.test;

import com.orion.lang.StopWatch;
import com.orion.utils.Objects1;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/2 12:32
 */
public class Tests {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Tests.class);

    public static void main(String[] args) throws Exception {
        StopWatch w = StopWatch.begin();

        Workbook wb = new XSSFWorkbook("C:\\Users\\ljh15\\Desktop\\export\\shop.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(2);
        System.out.println(row.getRowNum());
        System.out.println(row.getLastCellNum());

        String[] s = new String[]{"asd", "sd"};
        System.out.println(Objects1.toString(s));


        w.stop();
        System.out.println("\n\n\n\n\n" + w);
    }

}

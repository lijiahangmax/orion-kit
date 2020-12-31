package com.orion.test;

import com.orion.excel.Excels;
import com.orion.lang.StopWatch;
import com.orion.utils.io.Files1;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

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
        // wb.sheets.add()
        StopWatch w = StopWatch.begin();

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("format sheet");
        sheet.setDefaultColumnWidth(20);
        List<File> files = Files1.listFiles("C:\\Users\\ljh15\\Pictures\\帽子");
        for (int i = 0; i < files.size(); i++) {
            Row row = sheet.createRow(i);
            row.setHeightInPoints(200);
            Cell cell = row.createCell(0);
            Picture picture = Excels.setPicture(wb, sheet, files.get(i), i, 3, (String) null);
            picture.resize(0.5, 0.5);
            // int pictureIdx = wb.addPicture(files.get(i), XSSFWorkbook.PICTURE_TYPE_JPEG);
            // Drawing<?> drawing = sheet.createDrawingPatriarch();
            // cell.setCellValue(100);
            // XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, i, 2, i + 1);
            // Picture picture = drawing.createPicture(anchor, pictureIdx);
            // picture.resize(1);
        }

        Excels.filterRow(sheet, 0);
        Excels.addSelectOptions(sheet, 1, new String[]{"xx", "mm"});
        Excels.write(wb, "C:\\Users\\ljh15\\Desktop\\3.xlsx");

        // if (true) {
        //     return;
        // }

        w.stop();
        System.out.println("\n\n\n\n\n" + w);
    }

}

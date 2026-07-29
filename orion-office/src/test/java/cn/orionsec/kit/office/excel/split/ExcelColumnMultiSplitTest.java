package cn.orionsec.kit.office.excel.split;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ExcelColumnMultiSplit 测试
 */
public class ExcelColumnMultiSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("multi");
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue("v" + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testSplitByIndex() throws Exception {
        XSSFWorkbook source = createSource(3, 3);
        File target = folder.newFile("m1.xlsx");
        ExcelColumnMultiSplit split = new ExcelColumnMultiSplit(source, 0);
        assertNotNull(split.getSourceWorkbook());
        assertNotNull(split.getSourceSheet());
        split.skip().split(new int[]{1}, new String[]{"B"}, target);
        split.close();

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            assertEquals("multi", sheet.getSheetName());
            assertEquals("B", sheet.getRow(0).getCell(0).getStringCellValue());
            // 跳过第一行数据
            assertEquals("v1-1", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("v2-1", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals(2, sheet.getLastRowNum());
        }
    }

    @Test
    public void testSplitBySheetName() throws Exception {
        XSSFWorkbook source = createSource(2, 2);
        File target = folder.newFile("m2.xlsx");
        ExcelColumnMultiSplit split = new ExcelColumnMultiSplit(source, "multi");
        split.split(new int[]{0, 1}, target);
        split.close();

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            assertEquals("v0-0", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("v1-1", sheet.getRow(1).getCell(1).getStringCellValue());
        }
    }

}

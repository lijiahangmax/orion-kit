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

import static org.junit.Assert.*;

/**
 * ExcelColumnSingleSplit 测试
 */
public class ExcelColumnSingleSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("data");
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue("v" + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testSplitColumns() throws Exception {
        XSSFWorkbook source = createSource(3, 3);
        File target = folder.newFile("single.xlsx");
        ExcelColumnSingleSplit split = new ExcelColumnSingleSplit(source, source.getSheetAt(0), 0, 2);
        assertArrayEquals(new int[]{0, 2}, split.getColumns());
        assertNotNull(split.getSourceWorkbook());
        assertNotNull(split.getSourceSheet());
        assertNotNull(split.getTargetWorkbook());
        assertEquals("data", split.getTargetSheet().getSheetName());
        split.header("A", "C").split().write(target).close();

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            assertEquals("data", sheet.getSheetName());
            // 表头
            assertEquals("A", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("C", sheet.getRow(0).getCell(1).getStringCellValue());
            // 数据 (第 0/2 列)
            assertEquals("v0-0", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("v0-2", sheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("v2-0", sheet.getRow(3).getCell(0).getStringCellValue());
            assertEquals("v2-2", sheet.getRow(3).getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testSplitWithSkip() throws Exception {
        XSSFWorkbook source = createSource(3, 2);
        File target = folder.newFile("skip.xlsx");
        new ExcelColumnSingleSplit(source, source.getSheetAt(0), 1)
                .skip()
                .split()
                .write(target)
                .close();

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            // 跳过第一行
            assertEquals("v1-1", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("v2-1", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals(1, sheet.getLastRowNum());
        }
    }

}

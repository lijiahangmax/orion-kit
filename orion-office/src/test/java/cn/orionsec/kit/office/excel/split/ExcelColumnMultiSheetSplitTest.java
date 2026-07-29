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
 * ExcelColumnMultiSheetSplit 测试
 */
public class ExcelColumnMultiSheetSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("src");
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue("v" + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testSplitToMultiSheet() throws Exception {
        XSSFWorkbook source = createSource(2, 3);
        File target = folder.newFile("sheets.xlsx");
        ExcelColumnMultiSheetSplit split = new ExcelColumnMultiSheetSplit(source, 0);
        split.split(0)
                .split(new int[]{1, 2}, new String[]{"B", "C"});
        assertEquals(2, split.getSheetNum());
        assertNotNull(split.getSourceWorkbook());
        assertNotNull(split.getSourceSheet());
        assertNotNull(split.getTargetWorkbook());
        split.write(target).close();

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            assertEquals(2, wb.getNumberOfSheets());
            Sheet s1 = wb.getSheet("src1");
            Sheet s2 = wb.getSheet("src2");
            assertNotNull(s1);
            assertNotNull(s2);
            // sheet1 只有第 0 列
            assertEquals("v0-0", s1.getRow(0).getCell(0).getStringCellValue());
            assertEquals("v1-0", s1.getRow(1).getCell(0).getStringCellValue());
            // sheet2 表头 + 第 1/2 列
            assertEquals("B", s2.getRow(0).getCell(0).getStringCellValue());
            assertEquals("C", s2.getRow(0).getCell(1).getStringCellValue());
            assertEquals("v0-1", s2.getRow(1).getCell(0).getStringCellValue());
            assertEquals("v1-2", s2.getRow(2).getCell(1).getStringCellValue());
        }
    }

}

package cn.orionsec.kit.office.excel.merge;

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
 * ExcelMerge 测试
 */
public class ExcelMergeTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(String prefix, int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(prefix);
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue(prefix + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testMergeTwoSheets() throws Exception {
        File target = folder.newFile("merge.xlsx");
        try (XSSFWorkbook s1 = createSource("a", 2, 2);
             XSSFWorkbook s2 = createSource("b", 3, 2)) {
            ExcelMerge merge = new ExcelMerge();
            assertNotNull(merge.getSourceWorkbook());
            assertNotNull(merge.getSourceSheet());
            merge.merge(s1.getSheetAt(0))
                    .merge(s2.getSheetAt(0))
                    .write(target)
                    .close();
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            // 2 + 3 = 5 行
            assertEquals(4, sheet.getLastRowNum());
            assertEquals("a0-0", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("a1-1", sheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("b0-0", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals("b2-1", sheet.getRow(4).getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testMergeWithSkipRows() throws Exception {
        File target = folder.newFile("merge-skip.xlsx");
        try (XSSFWorkbook s1 = createSource("h", 3, 1)) {
            // 跳过合并 sheet 的第一行 (表头)
            new ExcelMerge()
                    .skipRows()
                    .merge(s1.getSheetAt(0))
                    .write(target)
                    .close();
        }
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(target))) {
            Sheet sheet = wb.getSheetAt(0);
            assertEquals(1, sheet.getLastRowNum());
            assertEquals("h1-0", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("h2-0", sheet.getRow(1).getCell(0).getStringCellValue());
        }
    }

}

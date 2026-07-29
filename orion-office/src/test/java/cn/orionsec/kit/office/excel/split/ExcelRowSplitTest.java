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
 * ExcelRowSplit 测试
 */
public class ExcelRowSplitTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("source");
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue("v" + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testSplitBasic() throws Exception {
        try (XSSFWorkbook source = createSource(5, 3)) {
            File f1 = folder.newFile("s1.xlsx");
            File f2 = folder.newFile("s2.xlsx");
            File f3 = folder.newFile("s3.xlsx");
            ExcelRowSplit split = new ExcelRowSplit(source.getSheetAt(0), 2);
            assertEquals(2, split.getLimit());
            assertNotNull(split.getSheet());
            // 必须 autoClose 否则 windows 下文件无法重新读取
            split.autoClose(true).target(f1, f2, f3);
            split.split().close();
            // 5 行 limit=2 -> 2 + 2 + 1
            assertRows(f1, 2, "v0-0", "v1-0");
            assertRows(f2, 2, "v2-0", "v3-0");
            assertRows(f3, 1, "v4-0");
        }
    }

    @Test
    public void testSplitWithHeaderAndSkip() throws Exception {
        try (XSSFWorkbook source = createSource(5, 2)) {
            File f1 = folder.newFile("h1.xlsx");
            File f2 = folder.newFile("h2.xlsx");
            ExcelRowSplit split = new ExcelRowSplit(source.getSheetAt(0), 2)
                    .skip()
                    .header("H1", "H2");
            split.autoClose(true).target(f1, f2);
            split.split().close();
            // 跳过第一行, 每个文件 1 行表头 + 2 行数据
            try (Workbook wb = new XSSFWorkbook(new FileInputStream(f1))) {
                Sheet sheet = wb.getSheetAt(0);
                assertEquals("source", sheet.getSheetName());
                assertEquals("H1", sheet.getRow(0).getCell(0).getStringCellValue());
                assertEquals("H2", sheet.getRow(0).getCell(1).getStringCellValue());
                assertEquals("v1-0", sheet.getRow(1).getCell(0).getStringCellValue());
                assertEquals("v2-0", sheet.getRow(2).getCell(0).getStringCellValue());
            }
            try (Workbook wb = new XSSFWorkbook(new FileInputStream(f2))) {
                Sheet sheet = wb.getSheetAt(0);
                assertEquals("v3-0", sheet.getRow(1).getCell(0).getStringCellValue());
                assertEquals("v4-0", sheet.getRow(2).getCell(0).getStringCellValue());
            }
        }
    }

    private void assertRows(File file, int rowCount, String... firstColValues) throws Exception {
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(file))) {
            Sheet sheet = wb.getSheetAt(0);
            assertEquals("source", sheet.getSheetName());
            assertEquals(rowCount - 1, sheet.getLastRowNum());
            for (int i = 0; i < firstColValues.length; i++) {
                assertEquals(firstColValues[i], sheet.getRow(i).getCell(0).getStringCellValue());
            }
        }
    }

}

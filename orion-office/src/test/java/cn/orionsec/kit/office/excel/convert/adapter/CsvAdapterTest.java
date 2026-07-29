package cn.orionsec.kit.office.excel.convert.adapter;

import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CsvAdapter 测试
 */
public class CsvAdapterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private XSSFWorkbook createSource(int rows, int cols) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("csv");
        for (int r = 0; r < rows; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < cols; c++) {
                row.createCell(c).setCellValue("v" + r + "-" + c);
            }
        }
        return workbook;
    }

    @Test
    public void testExcelToCsv() throws Exception {
        XSSFWorkbook workbook = createSource(3, 2);
        File csv = folder.newFile("out.csv");
        CsvArrayWriter writer = new CsvArrayWriter(csv);
        CsvAdapter adapter = new CsvAdapter(workbook.getSheetAt(0), writer);
        assertSame(workbook.getSheetAt(0), adapter.getSheet());
        assertSame(writer, adapter.getWriter());
        adapter.forNew();
        adapter.close();

        List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("v0-0"));
        assertTrue(lines.get(0).contains("v0-1"));
        assertTrue(lines.get(2).contains("v2-1"));
    }

    @Test
    public void testExcelToCsvWithHeaderAndSkip() throws Exception {
        XSSFWorkbook workbook = createSource(3, 2);
        File csv = folder.newFile("header.csv");
        new CsvAdapter(workbook.getSheetAt(0), new CsvArrayWriter(csv))
                .header("h1", "h2")
                .skip()
                .forNew()
                .close();

        List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
        // 1 行表头 + 2 行数据 (跳过第一行)
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("h1"));
        assertTrue(lines.get(0).contains("h2"));
        assertTrue(lines.get(1).contains("v1-0"));
        assertTrue(lines.get(2).contains("v2-0"));
    }

}

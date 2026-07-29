package cn.orionsec.kit.office.excel.convert;

import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import cn.orionsec.kit.office.excel.convert.adapter.CsvAdapter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ExcelConvert 测试
 */
public class ExcelConvertTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testCsvAdapterFactory() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("s");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("a");
            row.createCell(1).setCellValue("b");

            File csv = folder.newFile("convert.csv");
            CsvArrayWriter writer = new CsvArrayWriter(csv);
            CsvAdapter adapter = ExcelConvert.csvAdapter(sheet, writer);
            assertNotNull(adapter);
            assertSame(sheet, adapter.getSheet());
            assertSame(writer, adapter.getWriter());
            adapter.forNew();

            List<String> lines = Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8);
            assertEquals(1, lines.size());
            assertTrue(lines.get(0).contains("a"));
            assertTrue(lines.get(0).contains("b"));
        }
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<?> constructor = ExcelConvert.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }

}

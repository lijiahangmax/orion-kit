package cn.orionsec.kit.office.excel.copy;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * SheetCopier 测试
 */
public class SheetCopierTest {

    private XSSFWorkbook sourceWorkbook;

    private XSSFWorkbook targetWorkbook;

    @Before
    public void setUp() {
        this.sourceWorkbook = new XSSFWorkbook();
        this.targetWorkbook = new XSSFWorkbook();
    }

    @After
    public void tearDown() throws Exception {
        sourceWorkbook.close();
        targetWorkbook.close();
    }

    @Test
    public void testCopySheet() {
        XSSFSheet source = sourceWorkbook.createSheet("src");
        // 数据 + 样式
        CellStyle style = sourceWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        Font font = sourceWorkbook.createFont();
        font.setBold(true);
        style.setFont(font);
        for (int r = 0; r < 3; r++) {
            Row row = source.createRow(r);
            row.setHeightInPoints(20);
            for (int c = 0; c < 2; c++) {
                Cell cell = row.createCell(c);
                cell.setCellValue("v" + r + "-" + c);
                cell.setCellStyle(style);
            }
        }
        source.setColumnWidth(0, 5000);
        source.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        XSSFSheet target = targetWorkbook.createSheet("dst");
        SheetCopier copier = new SheetCopier(sourceWorkbook, targetWorkbook, source, target);
        assertSame(sourceWorkbook, copier.getSourceWorkbook());
        assertSame(targetWorkbook, copier.getTargetWorkbook());
        assertSame(source, copier.getSourceSheet());
        assertSame(target, copier.getTargetSheet());
        copier.column(4).copy();

        // 单元格值
        assertEquals("v0-0", target.getRow(0).getCell(0).getStringCellValue());
        assertEquals("v2-1", target.getRow(2).getCell(1).getStringCellValue());
        // 样式
        CellStyle copied = target.getRow(0).getCell(0).getCellStyle();
        assertEquals(HorizontalAlignment.CENTER, copied.getAlignment());
        assertEquals(BorderStyle.THIN, copied.getBorderBottom());
        // 合并区域
        assertEquals(1, target.getNumMergedRegions());
        // 列宽 / 行高
        assertEquals(source.getColumnWidth(0), target.getColumnWidth(0));
        assertEquals(source.getRow(0).getHeight(), target.getRow(0).getHeight());
    }

    @Test(expected = Exception.class)
    public void testDifferentSheetClass() {
        XSSFSheet source = sourceWorkbook.createSheet("s");
        // sheet 为 null 会抛出异常
        new SheetCopier(sourceWorkbook, targetWorkbook, source, null);
    }

}

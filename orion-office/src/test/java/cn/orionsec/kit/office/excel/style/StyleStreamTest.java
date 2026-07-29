package cn.orionsec.kit.office.excel.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * StyleStream 测试
 */
public class StyleStreamTest {

    private XSSFWorkbook workbook;

    @Before
    public void setUp() {
        this.workbook = new XSSFWorkbook();
    }

    @After
    public void tearDown() throws Exception {
        workbook.close();
    }

    @Test
    public void testAlignment() {
        StyleStream stream = new StyleStream(workbook)
                .centerAlignment()
                .verticalAlignment(VerticalAlignment.CENTER.getCode());
        assertEquals(HorizontalAlignment.CENTER.getCode(), stream.getAlignment());
        assertEquals(VerticalAlignment.CENTER.getCode(), stream.getVerticalAlignmentType());
        stream.leftAlignment();
        assertEquals(HorizontalAlignment.LEFT.getCode(), stream.getAlignment());
        stream.rightAlignment();
        assertEquals(HorizontalAlignment.RIGHT.getCode(), stream.getAlignment());
    }

    @Test
    public void testWrapTextAndLock() {
        StyleStream stream = new StyleStream(workbook).wrapText().lock().hidden();
        assertTrue(stream.isWrapText());
        assertTrue(stream.isLocked());
        stream.unsetWrapText().unlock();
        assertFalse(stream.isWrapText());
        assertFalse(stream.isLocked());
    }

    @Test
    public void testBorder() {
        StyleStream stream = new StyleStream(workbook).border(BorderStyle.THIN.getCode());
        assertEquals(BorderStyle.THIN.getCode(), stream.getBorderLeftType());
        assertEquals(BorderStyle.THIN.getCode(), stream.getBorderRightType());
        assertEquals(BorderStyle.THIN.getCode(), stream.getBorderTopType());
        assertEquals(BorderStyle.THIN.getCode(), stream.getBorderBottomType());
    }

    @Test
    public void testRotationAndIndention() {
        StyleStream stream = new StyleStream(workbook).rotation(45).indention(3);
        assertEquals(45, stream.getRotation());
        assertEquals(3, stream.getIndention());
    }

    @Test
    public void testDataFormat() {
        StyleStream stream = new StyleStream(workbook).dataFormat("yyyy-MM-dd");
        assertEquals("yyyy-MM-dd", stream.getDataFormatString());
    }

    @Test
    public void testTexture() {
        StyleStream stream = new StyleStream(workbook).texture();
        // 设置纹理后不再是无填充
        assertNotEquals(0, stream.getFillPattern());
    }

    @Test
    public void testGetStyleAndWorkbook() {
        CellStyle style = workbook.createCellStyle();
        StyleStream stream = new StyleStream(workbook, style);
        assertSame(style, stream.getStyle());
        assertSame(workbook, stream.getWorkbook());
        StyleStream factory = StyleStream.styleStream(workbook);
        assertNotNull(factory.getStyle());
        assertSame(style, StyleStream.styleStream(workbook, style).getStyle());
    }

}

package cn.orionsec.kit.office.excel.style;

import cn.orionsec.kit.office.excel.option.FontOption;
import cn.orionsec.kit.office.excel.type.ExcelUnderType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * FontStream 测试
 */
public class FontStreamTest {

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
    public void testChainSetters() {
        FontStream stream = new FontStream(workbook)
                .name("Arial")
                .size(14)
                .bold()
                .italic()
                .deleteLine()
                .underLine()
                .typeOffset(1)
                .charset(1)
                .family(2)
                .scheme(2);
        assertEquals("Arial", stream.getFontName());
        assertEquals(14, stream.getFontSize());
        assertTrue(stream.isBold());
        assertTrue(stream.isItalic());
        assertTrue(stream.isDelete());
        assertEquals(1, stream.getUnderLineType());
        assertEquals(1, stream.getOffsetType());
        assertEquals(1, stream.getCharset());
        assertEquals(2, stream.getFamily());
        assertEquals(2, stream.getScheme());
        assertSame(workbook, stream.getWorkbook());
        assertNotNull(stream.getFont());
    }

    @Test
    public void testUnsetters() {
        FontStream stream = new FontStream(workbook)
                .bold().italic().deleteLine().underDoubleLine()
                .unsetBold().unsetItalic().unsetDeleteLine().unsetUnderLine();
        assertFalse(stream.isBold());
        assertFalse(stream.isItalic());
        assertFalse(stream.isDelete());
        assertEquals(0, stream.getUnderLineType());
    }

    @Test
    public void testUnderDoubleLine() {
        FontStream stream = new FontStream(workbook).underDoubleLine();
        assertEquals(2, stream.getUnderLineType());
    }

    @Test
    public void testColorString() {
        FontStream stream = new FontStream(workbook).color("#FF0000");
        XSSFFont font = (XSSFFont) stream.getFont();
        byte[] rgb = font.getXSSFColor().getRGB();
        assertEquals((byte) 0xFF, rgb[0]);
        assertEquals((byte) 0x00, rgb[1]);
        assertEquals((byte) 0x00, rgb[2]);
    }

    @Test
    public void testStaticFactory() {
        FontStream stream = FontStream.fontStream(workbook);
        assertNotNull(stream.getFont());
        Font font = workbook.createFont();
        FontStream stream2 = FontStream.fontStream(workbook, font);
        assertSame(font, stream2.getFont());
    }

    @Test
    public void testParseFont() {
        FontOption option = new FontOption();
        option.setFontName("SimSun");
        option.setFontSize(16);
        option.setColor("#00FF00");
        option.setBold(true);
        option.setItalic(true);
        option.setDelete(true);
        // under 不能为 null 否则 parseFont 会 NPE
        option.setUnder(ExcelUnderType.SINGLE);
        Font font = FontStream.parseFont(workbook, option);
        assertNotNull(font);
        assertEquals("SimSun", font.getFontName());
        assertEquals(16, font.getFontHeightInPoints());
        assertTrue(font.getBold());
        assertTrue(font.getItalic());
        assertTrue(font.getStrikeout());
        assertEquals(1, font.getUnderline());
        byte[] rgb = ((XSSFFont) font).getXSSFColor().getRGB();
        assertEquals((byte) 0x00, rgb[0]);
        assertEquals((byte) 0xFF, rgb[1]);
        assertEquals((byte) 0x00, rgb[2]);
    }

    @Test
    public void testParseFontNullOption() {
        assertNull(FontStream.parseFont(workbook, null));
    }

}

package cn.orionsec.kit.office.excel.type;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * excel 枚举类型测试
 */
public class ExcelTypeTest {

    @Test
    public void testExcelAlignType() {
        assertEquals(8, ExcelAlignType.values().length);
        assertEquals(HorizontalAlignment.GENERAL.getCode(), ExcelAlignType.DEFAULT.getCode());
        assertEquals(HorizontalAlignment.LEFT.getCode(), ExcelAlignType.LEFT.getCode());
        assertEquals(HorizontalAlignment.CENTER.getCode(), ExcelAlignType.CENTER.getCode());
        assertEquals(HorizontalAlignment.RIGHT.getCode(), ExcelAlignType.RIGHT.getCode());
        assertEquals(HorizontalAlignment.FILL.getCode(), ExcelAlignType.FILL.getCode());
        assertEquals(HorizontalAlignment.JUSTIFY.getCode(), ExcelAlignType.JUSTIFY.getCode());
        assertEquals(HorizontalAlignment.CENTER_SELECTION.getCode(), ExcelAlignType.CENTER_SELECTION.getCode());
        assertEquals(HorizontalAlignment.DISTRIBUTED.getCode(), ExcelAlignType.DISTRIBUTED.getCode());
    }

    @Test
    public void testExcelBorderType() {
        assertEquals(8, ExcelBorderType.values().length);
        assertEquals(BorderStyle.NONE.getCode(), ExcelBorderType.DEFAULT.getCode());
        assertEquals(BorderStyle.THIN.getCode(), ExcelBorderType.THIN.getCode());
        assertEquals(BorderStyle.MEDIUM.getCode(), ExcelBorderType.MEDIUM.getCode());
        assertEquals(BorderStyle.THICK.getCode(), ExcelBorderType.THICK.getCode());
        assertEquals(BorderStyle.DASHED.getCode(), ExcelBorderType.DASHED.getCode());
        assertEquals(BorderStyle.DOTTED.getCode(), ExcelBorderType.DOTTED.getCode());
        assertEquals(BorderStyle.HAIR.getCode(), ExcelBorderType.HAIR.getCode());
        assertEquals(BorderStyle.DOUBLE.getCode(), ExcelBorderType.DOUBLE.getCode());
    }

    @Test
    public void testExcelFieldType() {
        assertEquals(8, ExcelFieldType.values().length);
        assertEquals(ExcelFieldType.TEXT, ExcelFieldType.of(null));
        assertEquals(ExcelFieldType.NUMBER, ExcelFieldType.of(Integer.class));
        assertEquals(ExcelFieldType.NUMBER, ExcelFieldType.of(int.class));
        assertEquals(ExcelFieldType.NUMBER, ExcelFieldType.of(BigDecimal.class));
        assertEquals(ExcelFieldType.DATE, ExcelFieldType.of(Date.class));
        assertEquals(ExcelFieldType.BOOLEAN, ExcelFieldType.of(Boolean.class));
        assertEquals(ExcelFieldType.BOOLEAN, ExcelFieldType.of(boolean.class));
        assertEquals(ExcelFieldType.TEXT, ExcelFieldType.of(String.class));
    }

    @Test
    public void testExcelLinkType() {
        assertEquals(4, ExcelLinkType.values().length);
        assertEquals(ExcelLinkType.LINK_URL, ExcelLinkType.valueOf("LINK_URL"));
        assertEquals(ExcelLinkType.LINK_DOC, ExcelLinkType.valueOf("LINK_DOC"));
        assertEquals(ExcelLinkType.LINK_EMAIL, ExcelLinkType.valueOf("LINK_EMAIL"));
        assertEquals(ExcelLinkType.LINK_FILE, ExcelLinkType.valueOf("LINK_FILE"));
    }

    @Test
    public void testExcelMarginType() {
        assertEquals(6, ExcelMarginType.values().length);
        assertEquals(Sheet.LeftMargin, ExcelMarginType.LEFT.getCode());
        assertEquals(Sheet.RightMargin, ExcelMarginType.RIGHT.getCode());
        assertEquals(Sheet.TopMargin, ExcelMarginType.TOP.getCode());
        assertEquals(Sheet.BottomMargin, ExcelMarginType.BOTTOM.getCode());
        assertEquals(Sheet.HeaderMargin, ExcelMarginType.HEADER.getCode());
        assertEquals(Sheet.FooterMargin, ExcelMarginType.FOOTER.getCode());
    }

    @Test
    public void testExcelPaperType() {
        assertEquals(16, ExcelPaperType.values().length);
        assertEquals(PrintSetup.PRINTER_DEFAULT_PAPERSIZE, ExcelPaperType.DEFAULT.getCode());
        assertEquals(PrintSetup.A3_PAPERSIZE, ExcelPaperType.A3.getCode());
        assertEquals(PrintSetup.A4_PAPERSIZE, ExcelPaperType.A4.getCode());
        assertEquals(PrintSetup.A5_PAPERSIZE, ExcelPaperType.A5.getCode());
        assertEquals(PrintSetup.B4_PAPERSIZE, ExcelPaperType.B4.getCode());
        assertEquals(PrintSetup.B5_PAPERSIZE, ExcelPaperType.B5.getCode());
        assertEquals(PrintSetup.ENVELOPE_DL_PAPERSIZE, ExcelPaperType.DL.getCode());
        assertEquals(PrintSetup.QUARTO_PAPERSIZE, ExcelPaperType.QUARTO.getCode());
    }

    @Test
    public void testExcelPictureType() {
        assertEquals(13, ExcelPictureType.values().length);
        // AUTO
        assertEquals(0, ExcelPictureType.AUTO.getType1());
        assertEquals(0, ExcelPictureType.AUTO.getType2());
        assertEquals("", ExcelPictureType.AUTO.getSuffix());
        // 常用类型
        assertEquals(Workbook.PICTURE_TYPE_JPEG, ExcelPictureType.JPG.getType1());
        assertEquals(XSSFWorkbook.PICTURE_TYPE_JPEG, ExcelPictureType.JPG.getType2());
        assertEquals("jpg", ExcelPictureType.JPG.getSuffix());
        assertEquals(Workbook.PICTURE_TYPE_PNG, ExcelPictureType.PNG.getType1());
        assertEquals("png", ExcelPictureType.PNG.getSuffix());
        // xls 不支持的类型 type1 为 -1
        assertEquals(-1, ExcelPictureType.GIF.getType1());
        assertEquals(XSSFWorkbook.PICTURE_TYPE_GIF, ExcelPictureType.GIF.getType2());
        assertEquals(-1, ExcelPictureType.BMP.getType1());
    }

    @Test
    public void testExcelPictureTypeOf() {
        assertEquals(ExcelPictureType.PNG, ExcelPictureType.of(null));
        assertEquals(ExcelPictureType.PNG, ExcelPictureType.of(""));
        assertEquals(ExcelPictureType.JPG, ExcelPictureType.of("jpg"));
        assertEquals(ExcelPictureType.JPEG, ExcelPictureType.of("jpeg"));
        assertEquals(ExcelPictureType.GIF, ExcelPictureType.of("gif"));
        // 未知后缀返回 PNG
        assertEquals(ExcelPictureType.PNG, ExcelPictureType.of("unknown"));
    }

    @Test
    public void testExcelReadType() {
        assertEquals(9, ExcelReadType.values().length);
        assertEquals(ExcelReadType.TEXT, ExcelReadType.values()[0]);
        assertEquals(ExcelReadType.DECIMAL, ExcelReadType.valueOf("DECIMAL"));
        assertEquals(ExcelReadType.INTEGER, ExcelReadType.valueOf("INTEGER"));
        assertEquals(ExcelReadType.LONG, ExcelReadType.valueOf("LONG"));
        assertEquals(ExcelReadType.PHONE, ExcelReadType.valueOf("PHONE"));
        assertEquals(ExcelReadType.DATE, ExcelReadType.valueOf("DATE"));
        assertEquals(ExcelReadType.LINK_ADDRESS, ExcelReadType.valueOf("LINK_ADDRESS"));
        assertEquals(ExcelReadType.COMMENT, ExcelReadType.valueOf("COMMENT"));
        assertEquals(ExcelReadType.PICTURE, ExcelReadType.valueOf("PICTURE"));
    }

    @Test
    public void testExcelUnderType() {
        assertEquals(5, ExcelUnderType.values().length);
        assertEquals(Font.U_NONE, ExcelUnderType.NONE.getCode());
        assertEquals(Font.U_SINGLE, ExcelUnderType.SINGLE.getCode());
        assertEquals(Font.U_DOUBLE, ExcelUnderType.DOUBLE.getCode());
        assertEquals(Font.U_SINGLE_ACCOUNTING, ExcelUnderType.SINGLE_ACCOUNTING.getCode());
        assertEquals(Font.U_DOUBLE_ACCOUNTING, ExcelUnderType.DOUBLE_ACCOUNTING.getCode());
    }

    @Test
    public void testExcelVerticalAlignType() {
        assertEquals(6, ExcelVerticalAlignType.values().length);
        assertEquals(-1, ExcelVerticalAlignType.DEFAULT.getCode());
        assertEquals(VerticalAlignment.TOP.getCode(), ExcelVerticalAlignType.TOP.getCode());
        assertEquals(VerticalAlignment.CENTER.getCode(), ExcelVerticalAlignType.CENTER.getCode());
        assertEquals(VerticalAlignment.BOTTOM.getCode(), ExcelVerticalAlignType.BOTTOM.getCode());
        assertEquals(VerticalAlignment.JUSTIFY.getCode(), ExcelVerticalAlignType.JUSTIFY.getCode());
        assertEquals(VerticalAlignment.DISTRIBUTED.getCode(), ExcelVerticalAlignType.DISTRIBUTED.getCode());
    }

}

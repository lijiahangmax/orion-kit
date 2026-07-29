package cn.orionsec.kit.office.excel.option;

import cn.orionsec.kit.office.excel.type.*;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * excel 配置项测试
 */
public class ExcelOptionTest {

    @Test
    public void testCellOption() {
        CellOption option = new CellOption();
        assertNull(option.getFormat());
        assertFalse(option.isRich());
        option.setFormat("yyyy-MM-dd");
        option.setRich(true);
        assertEquals("yyyy-MM-dd", option.getFormat());
        assertTrue(option.isRich());
        assertEquals("#.00", new CellOption("#.00").getFormat());
    }

    @Test
    public void testCommentOption() {
        CommentOption option = new CommentOption();
        assertNull(option.getComment());
        assertFalse(option.isVisible());
        option.setComment("comment");
        option.setAuthor("author");
        option.setVisible(true);
        assertEquals("comment", option.getComment());
        assertEquals("author", option.getAuthor());
        assertTrue(option.isVisible());
        assertEquals("c1", new CommentOption("c1").getComment());
    }

    @Test
    public void testHeaderFooterOption() {
        HeaderOption header = new HeaderOption("l", "c", "r");
        assertEquals("l", header.getLeft());
        assertEquals("c", header.getCenter());
        assertEquals("r", header.getRight());
        header.setCenter("c2");
        assertEquals("c2", header.getCenter());

        FooterOption footer = new FooterOption("fl", "fc", "fr");
        assertEquals("fl", footer.getLeft());
        assertEquals("fc", footer.getCenter());
        assertEquals("fr", footer.getRight());
        footer.setLeft("fl2");
        assertEquals("fl2", footer.getLeft());
    }

    @Test
    public void testFontOption() {
        FontOption option = new FontOption();
        assertNull(option.getFontName());
        assertNull(option.getFontSize());
        assertFalse(option.isBold());
        assertFalse(option.isItalic());
        assertFalse(option.isDelete());
        assertNull(option.getUnder());
        option.setFontName("Arial");
        option.setFontSize(12);
        option.setColor("#FF0000");
        option.setBold(true);
        option.setItalic(true);
        option.setDelete(true);
        option.setUnder(ExcelUnderType.SINGLE);
        option.setPaletteColorIndex((short) 9);
        assertEquals("Arial", option.getFontName());
        assertEquals(Integer.valueOf(12), option.getFontSize());
        assertEquals("#FF0000", option.getColor());
        assertTrue(option.isBold());
        assertTrue(option.isItalic());
        assertTrue(option.isDelete());
        assertEquals(ExcelUnderType.SINGLE, option.getUnder());
        assertEquals(9, option.getPaletteColorIndex());
    }

    @Test
    public void testExportFieldOption() {
        ExportFieldOption option = new ExportFieldOption();
        assertNull(option.getWidth());
        assertFalse(option.isWrapText());
        option.setWidth(20);
        option.setWrapText(true);
        option.setVerticalAlign(ExcelVerticalAlignType.CENTER);
        option.setAlign(ExcelAlignType.LEFT);
        option.setBackgroundColor("#FFFFFF");
        option.setBorder(ExcelBorderType.THIN);
        option.setBorderColor("#000000");
        option.setIndent((short) 2);
        option.setFormat("#.0");
        option.setType(ExcelFieldType.NUMBER);
        option.setHeader("h");
        option.setSkipHeaderStyle(true);
        option.setHidden(true);
        option.setLock(true);
        option.setAutoResize(true);
        option.setQuotePrefixed(true);
        option.setTrim(true);
        option.setSelectOptions(new String[]{"a", "b"});
        assertEquals(Integer.valueOf(20), option.getWidth());
        assertTrue(option.isWrapText());
        assertEquals(ExcelVerticalAlignType.CENTER, option.getVerticalAlign());
        assertEquals(ExcelAlignType.LEFT, option.getAlign());
        assertEquals("#FFFFFF", option.getBackgroundColor());
        assertEquals(ExcelBorderType.THIN, option.getBorder());
        assertEquals("#000000", option.getBorderColor());
        assertEquals(Short.valueOf((short) 2), option.getIndent());
        assertEquals("#.0", option.getFormat());
        assertEquals(ExcelFieldType.NUMBER, option.getType());
        assertEquals("h", option.getHeader());
        assertTrue(option.isSkipHeaderStyle());
        assertTrue(option.isHidden());
        assertTrue(option.isLock());
        assertTrue(option.isAutoResize());
        assertTrue(option.isQuotePrefixed());
        assertTrue(option.isTrim());
        assertEquals(2, option.getSelectOptions().length);
        // 复合配置
        option.setFontOption(new FontOption());
        option.setCommentOption(new CommentOption("c"));
        option.setLinkOption(new LinkOption());
        option.setCellOption(new CellOption("f"));
        option.setPictureOption(new PictureOption());
        assertNotNull(option.getFontOption());
        assertEquals("c", option.getCommentOption().getComment());
        assertNotNull(option.getLinkOption());
        assertEquals("f", option.getCellOption().getFormat());
        assertNotNull(option.getPictureOption());
    }

    @Test
    public void testExportSheetOption() {
        ExportSheetOption option = new ExportSheetOption();
        // 无参构造器默认跳过空行
        assertTrue(option.isSkipNullRows());
        assertNull(option.getName());
        option.setName("sheet1");
        option.setColumnWidth(20);
        option.setTitleHeight(30);
        option.setRowHeight(15);
        option.setHeaderHeight(25);
        option.setZoom(80);
        option.setHeaderUseColumnStyle(true);
        option.setSkipPictureException(true);
        option.setFreezeHeader(true);
        option.setFilterHeader(true);
        option.setSelected(true);
        option.setHidden(true);
        option.setTitle("title");
        option.setColumnMaxIndex(5);
        option.setTitleAndHeaderLastRowIndex(2);
        assertEquals("sheet1", option.getName());
        assertEquals(Integer.valueOf(20), option.getColumnWidth());
        assertEquals(Integer.valueOf(30), option.getTitleHeight());
        assertEquals(Integer.valueOf(15), option.getRowHeight());
        assertEquals(Integer.valueOf(25), option.getHeaderHeight());
        assertEquals(Integer.valueOf(80), option.getZoom());
        assertTrue(option.isHeaderUseColumnStyle());
        assertTrue(option.isSkipPictureException());
        assertTrue(option.isFreezeHeader());
        assertTrue(option.isFilterHeader());
        assertTrue(option.isSelected());
        assertTrue(option.isHidden());
        assertEquals("title", option.getTitle());
        assertEquals(5, option.getColumnMaxIndex());
        assertEquals(2, option.getTitleAndHeaderLastRowIndex());
        option.setHeaderOption(new HeaderOption("a", "b", "c"));
        option.setFooterOption(new FooterOption("d", "e", "f"));
        option.setTitleOption(new TitleOption());
        option.setPrintOption(new PrintOption());
        option.setPropertiesOption(new PropertiesOption());
        assertEquals("a", option.getHeaderOption().getLeft());
        assertEquals("f", option.getFooterOption().getRight());
        assertNotNull(option.getTitleOption());
        assertNotNull(option.getPrintOption());
        assertNotNull(option.getPropertiesOption());
    }

    @Test
    public void testPrintOptionDefaults() {
        PrintOption option = new PrintOption();
        // 默认纸张
        assertEquals(ExcelPaperType.DEFAULT, option.getPaper());
        assertNull(option.getScale());
        assertNull(option.getLimit());
        assertNull(option.getRepeat());
        assertFalse(option.isPrintGridLines());
        assertFalse(option.isFit());
    }

    @Test
    public void testPrintOptionSetters() {
        PrintOption option = new PrintOption();
        option.setPrintGridLines(true);
        option.setPrintRowHeading(true);
        option.setAutoLimit(true);
        option.setLimit(100);
        option.setFit(true);
        option.setHorizontallyCenter(true);
        option.setVerticallyCenter(true);
        option.setPaper(ExcelPaperType.A4);
        option.setColor(true);
        option.setLandScapePrint(true);
        option.setSetPrintOrientation(true);
        option.setScale(90);
        option.setNotes(true);
        option.setHorizontalResolution(600);
        option.setVerticalResolution(600);
        option.setWidth(1);
        option.setHeight(2);
        option.setUsePage(true);
        option.setPageStart(1);
        option.setCopies(2);
        option.setDraft(true);
        option.setTopToBottom(true);
        option.setLeftMargin(0.5);
        option.setRightMargin(0.6);
        option.setTopMargin(0.7);
        option.setBottomMargin(0.8);
        option.setHeaderMargin(0.3);
        option.setFooterMargin(0.4);
        assertTrue(option.isPrintGridLines());
        assertTrue(option.isPrintRowHeading());
        assertTrue(option.isAutoLimit());
        assertEquals(Integer.valueOf(100), option.getLimit());
        assertTrue(option.isFit());
        assertTrue(option.isHorizontallyCenter());
        assertTrue(option.isVerticallyCenter());
        assertEquals(ExcelPaperType.A4, option.getPaper());
        assertTrue(option.isColor());
        assertTrue(option.isLandScapePrint());
        assertTrue(option.isSetPrintOrientation());
        assertEquals(Integer.valueOf(90), option.getScale());
        assertTrue(option.isNotes());
        assertEquals(Integer.valueOf(600), option.getHorizontalResolution());
        assertEquals(Integer.valueOf(600), option.getVerticalResolution());
        assertEquals(Integer.valueOf(1), option.getWidth());
        assertEquals(Integer.valueOf(2), option.getHeight());
        assertTrue(option.isUsePage());
        assertEquals(Integer.valueOf(1), option.getPageStart());
        assertEquals(Integer.valueOf(2), option.getCopies());
        assertTrue(option.isDraft());
        assertTrue(option.isTopToBottom());
        assertEquals(0.5, option.getLeftMargin(), 0.0001);
        assertEquals(0.6, option.getRightMargin(), 0.0001);
        assertEquals(0.7, option.getTopMargin(), 0.0001);
        assertEquals(0.8, option.getBottomMargin(), 0.0001);
        assertEquals(0.3, option.getHeaderMargin(), 0.0001);
        assertEquals(0.4, option.getFooterMargin(), 0.0001);
    }

    @Test
    public void testPrintOptionRepeat() {
        PrintOption option = new PrintOption();
        option.setRepeat(3, 4);
        assertArrayEquals(new int[]{0, 3, 0, 4}, option.getRepeat());
        option.setRepeat(1, 2, 3, 4);
        assertArrayEquals(new int[]{1, 2, 3, 4}, option.getRepeat());
        option.setRepeat(new int[]{5, 6, 7, 8});
        assertArrayEquals(new int[]{5, 6, 7, 8}, option.getRepeat());
    }

    @Test
    public void testImportFieldOption() {
        ImportFieldOption option = new ImportFieldOption(1, ExcelReadType.TEXT);
        assertEquals(1, option.getIndex());
        assertEquals(ExcelReadType.TEXT, option.getType());

        ImportFieldOption dateOption = new ImportFieldOption(2, ExcelReadType.DATE, "yyyy-MM-dd");
        assertEquals(2, dateOption.getIndex());
        assertEquals(ExcelReadType.DATE, dateOption.getType());
        assertNotNull(dateOption.getCellOption());
        assertEquals("yyyy-MM-dd", dateOption.getCellOption().getFormat());

        ImportFieldOption empty = new ImportFieldOption();
        empty.setIndex(3);
        empty.setType(ExcelReadType.INTEGER);
        empty.setCellOption(new CellOption("0"));
        assertEquals(3, empty.getIndex());
        assertEquals(ExcelReadType.INTEGER, empty.getType());
        assertEquals("0", empty.getCellOption().getFormat());
    }

    @Test
    public void testWriteFieldOption() {
        WriteFieldOption option = new WriteFieldOption(0);
        assertEquals(0, option.getIndex());

        WriteFieldOption typed = new WriteFieldOption(1, ExcelFieldType.NUMBER);
        assertEquals(1, typed.getIndex());
        assertEquals(ExcelFieldType.NUMBER, typed.getType());

        WriteFieldOption formatted = new WriteFieldOption(2, ExcelFieldType.DATE_FORMAT, "yyyy");
        assertEquals(2, formatted.getIndex());
        assertEquals(ExcelFieldType.DATE_FORMAT, formatted.getType());
        assertNotNull(formatted.getCellOption());
        assertEquals("yyyy", formatted.getCellOption().getFormat());
    }

    @Test
    public void testLinkOptionConstants() {
        assertEquals("!", LinkOption.NORMAL_PREFIX);
        assertEquals("$", LinkOption.FIELD_PREFIX);
        assertEquals("@", LinkOption.ORIGIN);
        LinkOption option = new LinkOption();
        option.setType(ExcelLinkType.LINK_EMAIL);
        option.setAddress("mail@test.com");
        option.setText("mail");
        option.setOriginLink(true);
        option.setOriginText(true);
        assertEquals(ExcelLinkType.LINK_EMAIL, option.getType());
        assertEquals("mail@test.com", option.getAddress());
        assertEquals("mail", option.getText());
        assertTrue(option.isOriginLink());
        assertTrue(option.isOriginText());
    }

    @Test
    public void testPictureOptionConstants() {
        assertEquals("!", PictureOption.NORMAL_PREFIX);
        assertEquals("$", PictureOption.FIELD_PREFIX);
        assertEquals("@", PictureOption.ORIGIN);
        PictureOption option = new PictureOption();
        option.setType(ExcelPictureType.PNG);
        option.setBase64(true);
        option.setAutoClose(true);
        option.setScaleX(1.5);
        option.setScaleY(2.5);
        option.setImage("img");
        option.setText("txt");
        option.setNoneText(true);
        assertEquals(ExcelPictureType.PNG, option.getType());
        assertTrue(option.isBase64());
        assertTrue(option.isAutoClose());
        assertEquals(1.5, option.getScaleX(), 0.0001);
        assertEquals(2.5, option.getScaleY(), 0.0001);
        assertEquals("img", option.getImage());
        assertEquals("txt", option.getText());
        assertTrue(option.isNoneText());
    }

    @Test
    public void testTitleOption() {
        TitleOption option = new TitleOption();
        option.setTitle("t");
        option.setUseRow(2);
        option.setUseColumn(3);
        option.setVerticalAlign(ExcelVerticalAlignType.CENTER);
        option.setAlign(ExcelAlignType.CENTER);
        option.setBackgroundColor("#123456");
        option.setBorder(ExcelBorderType.DOUBLE);
        option.setBorderColor("#654321");
        option.setPaletteColorIndex((short) 10);
        option.setFont(new FontOption());
        assertEquals("t", option.getTitle());
        assertEquals(2, option.getUseRow());
        assertEquals(3, option.getUseColumn());
        assertEquals(ExcelVerticalAlignType.CENTER, option.getVerticalAlign());
        assertEquals(ExcelAlignType.CENTER, option.getAlign());
        assertEquals("#123456", option.getBackgroundColor());
        assertEquals(ExcelBorderType.DOUBLE, option.getBorder());
        assertEquals("#654321", option.getBorderColor());
        assertEquals(10, option.getPaletteColorIndex());
        assertNotNull(option.getFont());
    }

    @Test
    public void testPropertiesOption() {
        PropertiesOption option = new PropertiesOption();
        Date now = new Date();
        option.setAuthor("author");
        option.setTitle("title");
        option.setSubject("subject");
        option.setKeywords("keywords");
        option.setRevision("1.0");
        option.setDescription("desc");
        option.setCategory("cat");
        option.setCompany("company");
        option.setManager("manager");
        option.setApplication("app");
        option.setModifiedUser("user");
        option.setContentStatus("status");
        option.setContentType("type");
        option.setIdentifier("id");
        option.setCreated(now);
        option.setModified(now);
        assertEquals("author", option.getAuthor());
        assertEquals("title", option.getTitle());
        assertEquals("subject", option.getSubject());
        assertEquals("keywords", option.getKeywords());
        assertEquals("1.0", option.getRevision());
        assertEquals("desc", option.getDescription());
        assertEquals("cat", option.getCategory());
        assertEquals("company", option.getCompany());
        assertEquals("manager", option.getManager());
        assertEquals("app", option.getApplication());
        assertEquals("user", option.getModifiedUser());
        assertEquals("status", option.getContentStatus());
        assertEquals("type", option.getContentType());
        assertEquals("id", option.getIdentifier());
        assertEquals(now, option.getCreated());
        assertEquals(now, option.getModified());
    }

}

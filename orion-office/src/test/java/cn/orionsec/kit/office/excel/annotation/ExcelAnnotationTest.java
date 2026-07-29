package cn.orionsec.kit.office.excel.annotation;

import cn.orionsec.kit.office.excel.option.LinkOption;
import cn.orionsec.kit.office.excel.option.PictureOption;
import cn.orionsec.kit.office.excel.type.*;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * excel 注解元数据测试
 */
public class ExcelAnnotationTest {

    private static final Class<?>[] ALL = {
            ExportComment.class, ExportField.class, ExportFont.class, ExportFooter.class,
            ExportHeader.class, ExportIgnore.class, ExportLink.class, ExportMeta.class,
            ExportPicture.class, ExportPrint.class, ExportSheet.class, ExportTitle.class,
            ImportField.class, ImportIgnore.class
    };

    private static final Class<?>[] METHOD_FIELD = {
            ExportComment.class, ExportField.class, ExportFont.class, ExportIgnore.class,
            ExportLink.class, ExportPicture.class, ImportField.class, ImportIgnore.class
    };

    private static final Class<?>[] TYPE_ONLY = {
            ExportFooter.class, ExportHeader.class, ExportMeta.class,
            ExportPrint.class, ExportSheet.class, ExportTitle.class
    };

    private static Object def(Class<?> type, String method) throws Exception {
        return type.getMethod(method).getDefaultValue();
    }

    @Test
    public void testRetentionAndDocumented() {
        for (Class<?> clazz : ALL) {
            assertTrue(clazz.getName(), clazz.isAnnotation());
            Retention retention = clazz.getAnnotation(Retention.class);
            assertNotNull(clazz.getName(), retention);
            assertEquals(clazz.getName(), RetentionPolicy.RUNTIME, retention.value());
            assertNotNull(clazz.getName(), clazz.getAnnotation(Documented.class));
        }
    }

    @Test
    public void testMethodFieldTarget() {
        for (Class<?> clazz : METHOD_FIELD) {
            Target target = clazz.getAnnotation(Target.class);
            assertNotNull(clazz.getName(), target);
            ElementType[] types = target.value();
            assertEquals(clazz.getName(), 2, types.length);
            assertEquals(clazz.getName(), ElementType.METHOD, types[0]);
            assertEquals(clazz.getName(), ElementType.FIELD, types[1]);
        }
    }

    @Test
    public void testTypeTarget() {
        for (Class<?> clazz : TYPE_ONLY) {
            Target target = clazz.getAnnotation(Target.class);
            assertNotNull(clazz.getName(), target);
            ElementType[] types = target.value();
            assertEquals(clazz.getName(), 1, types.length);
            assertEquals(clazz.getName(), ElementType.TYPE, types[0]);
        }
    }

    @Test
    public void testExportFieldDefaults() throws Exception {
        // index 为必填项无默认值
        assertNull(def(ExportField.class, "index"));
        assertEquals(-1, def(ExportField.class, "width"));
        assertEquals("", def(ExportField.class, "header"));
        assertEquals(false, def(ExportField.class, "wrapText"));
        assertEquals(false, def(ExportField.class, "rich"));
        assertEquals(ExcelVerticalAlignType.CENTER, def(ExportField.class, "verticalAlign"));
        assertEquals(ExcelAlignType.DEFAULT, def(ExportField.class, "align"));
        assertEquals("", def(ExportField.class, "backgroundColor"));
        assertEquals(ExcelBorderType.DEFAULT, def(ExportField.class, "border"));
        assertEquals("", def(ExportField.class, "borderColor"));
        assertEquals(-1, def(ExportField.class, "indent"));
        assertEquals("", def(ExportField.class, "format"));
        assertEquals(ExcelFieldType.AUTO, def(ExportField.class, "type"));
        assertEquals(false, def(ExportField.class, "trim"));
        assertEquals(false, def(ExportField.class, "skipHeaderStyle"));
        assertEquals(false, def(ExportField.class, "hidden"));
        assertEquals(false, def(ExportField.class, "lock"));
        assertEquals(false, def(ExportField.class, "autoResize"));
        assertEquals(false, def(ExportField.class, "quotePrefixed"));
        assertEquals(0, ((String[]) def(ExportField.class, "selectOptions")).length);
    }

    @Test
    public void testExportFontDefaults() throws Exception {
        assertEquals("", def(ExportFont.class, "fontName"));
        assertEquals(-1, def(ExportFont.class, "fontSize"));
        assertEquals("", def(ExportFont.class, "color"));
        assertEquals(false, def(ExportFont.class, "bold"));
        assertEquals(false, def(ExportFont.class, "italic"));
        assertEquals(false, def(ExportFont.class, "delete"));
        assertEquals(ExcelUnderType.NONE, def(ExportFont.class, "under"));
    }

    @Test
    public void testExportCommentDefaults() throws Exception {
        assertNull(def(ExportComment.class, "comment"));
        assertEquals("", def(ExportComment.class, "author"));
        assertEquals(false, def(ExportComment.class, "visible"));
    }

    @Test
    public void testExportLinkDefaults() throws Exception {
        assertEquals(ExcelLinkType.LINK_URL, def(ExportLink.class, "type"));
        assertEquals(LinkOption.ORIGIN, def(ExportLink.class, "address"));
        assertEquals(LinkOption.ORIGIN, def(ExportLink.class, "text"));
    }

    @Test
    public void testExportPictureDefaults() throws Exception {
        assertEquals(ExcelPictureType.AUTO, def(ExportPicture.class, "type"));
        assertEquals(false, def(ExportPicture.class, "base64"));
        assertEquals(false, def(ExportPicture.class, "autoClose"));
        assertEquals(1.0, (Double) def(ExportPicture.class, "scaleX"), 0.0001);
        assertEquals(1.0, (Double) def(ExportPicture.class, "scaleY"), 0.0001);
        assertEquals(PictureOption.ORIGIN, def(ExportPicture.class, "image"));
        assertEquals("", def(ExportPicture.class, "text"));
    }

    @Test
    public void testExportPrintDefaults() throws Exception {
        assertEquals(true, def(ExportPrint.class, "printGridLines"));
        assertEquals(false, def(ExportPrint.class, "printHeading"));
        assertEquals(true, def(ExportPrint.class, "fit"));
        assertEquals(-1, def(ExportPrint.class, "limit"));
        assertEquals(0, ((int[]) def(ExportPrint.class, "repeat")).length);
        assertEquals(ExcelPaperType.DEFAULT, def(ExportPrint.class, "paper"));
        assertEquals(false, def(ExportPrint.class, "color"));
        assertEquals(false, def(ExportPrint.class, "landScapePrint"));
        assertEquals(100, def(ExportPrint.class, "scale"));
        assertEquals(true, def(ExportPrint.class, "usePage"));
        assertEquals(1, def(ExportPrint.class, "pageStart"));
        assertEquals(1, def(ExportPrint.class, "copies"));
        assertEquals(false, def(ExportPrint.class, "draft"));
        assertEquals(true, def(ExportPrint.class, "topToBottom"));
    }

    @Test
    public void testExportSheetDefaults() throws Exception {
        assertEquals("", def(ExportSheet.class, "name"));
        assertEquals(-1, def(ExportSheet.class, "columnWidth"));
        assertEquals(-1, def(ExportSheet.class, "zoom"));
        assertEquals(false, def(ExportSheet.class, "columnUseDefaultStyle"));
        assertEquals(false, def(ExportSheet.class, "indexToSort"));
        assertEquals(true, def(ExportSheet.class, "headerUseColumnStyle"));
        assertEquals(false, def(ExportSheet.class, "skipFieldHeader"));
        assertEquals(true, def(ExportSheet.class, "skipPictureException"));
        assertEquals(false, def(ExportSheet.class, "freezeHeader"));
        assertEquals(false, def(ExportSheet.class, "filterHeader"));
        assertEquals(false, def(ExportSheet.class, "selected"));
        assertEquals(false, def(ExportSheet.class, "hidden"));
    }

    @Test
    public void testExportTitleDefaults() throws Exception {
        assertNull(def(ExportTitle.class, "title"));
        assertEquals(1, def(ExportTitle.class, "useRow"));
        assertEquals(-1, def(ExportTitle.class, "useColumn"));
        assertEquals(ExcelVerticalAlignType.CENTER, def(ExportTitle.class, "verticalAlign"));
        assertEquals(ExcelAlignType.CENTER, def(ExportTitle.class, "align"));
        assertEquals("#6B9AC9", def(ExportTitle.class, "backgroundColor"));
        assertEquals(ExcelBorderType.DEFAULT, def(ExportTitle.class, "border"));
        ExportFont font = (ExportFont) def(ExportTitle.class, "font");
        assertNotNull(font);
        assertTrue(font.bold());
        assertFalse(font.italic());
    }

    @Test
    public void testImportFieldDefaults() throws Exception {
        assertNull(def(ImportField.class, "index"));
        assertEquals(ExcelReadType.TEXT, def(ImportField.class, "type"));
        assertEquals("", def(ImportField.class, "parseFormat"));
    }

    @Test
    public void testExportMetaDefaults() throws Exception {
        // 全部属性默认值为空字符串
        for (Method method : ExportMeta.class.getDeclaredMethods()) {
            assertEquals(method.getName(), "", method.getDefaultValue());
        }
        assertEquals(14, ExportMeta.class.getDeclaredMethods().length);
    }

    @Test
    public void testHeaderFooterDefaults() throws Exception {
        for (String m : new String[]{"left", "center", "right"}) {
            assertEquals("", def(ExportHeader.class, m));
            assertEquals("", def(ExportFooter.class, m));
        }
    }

    @Test
    public void testIgnoreAnnotationHasNoAttribute() {
        assertEquals(0, ExportIgnore.class.getDeclaredMethods().length);
        assertEquals(0, ImportIgnore.class.getDeclaredMethods().length);
    }

}

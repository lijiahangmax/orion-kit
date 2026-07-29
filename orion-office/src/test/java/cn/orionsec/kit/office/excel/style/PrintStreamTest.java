package cn.orionsec.kit.office.excel.style;

import cn.orionsec.kit.office.excel.option.PrintOption;
import cn.orionsec.kit.office.excel.type.ExcelPaperType;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PrintStream 测试
 */
public class PrintStreamTest {

    private XSSFWorkbook workbook;

    private Sheet sheet;

    @Before
    public void setUp() {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("print");
    }

    @After
    public void tearDown() throws Exception {
        workbook.close();
    }

    @Test
    public void testChainSetters() {
        PrintStream stream = new PrintStream(sheet)
                .printGridLines()
                .fit()
                .horizontallyCenter()
                .verticallyCenter()
                .scale(80)
                .copies(2)
                .pageStart(3)
                .draft()
                .notes()
                .paper(ExcelPaperType.A4)
                .width(2)
                .height(3)
                .topToBottomOrder();
        assertTrue(stream.isPrintGridLines());
        assertTrue(stream.isFit());
        assertTrue(stream.isHorizontallyCenter());
        assertTrue(stream.isVerticallyCenter());
        assertEquals(80, stream.getScale());
        assertEquals(2, stream.getCopies());
        assertEquals(3, stream.getPageStart());
        assertTrue(stream.getUsePage());
        assertTrue(stream.isDraft());
        assertTrue(stream.isPrintNotes());
        assertEquals(ExcelPaperType.A4.getCode(), stream.getPaper());
        assertEquals(2, stream.getWidth());
        assertEquals(3, stream.getHeight());
        // 自上而下打印
        assertEquals(1, stream.getPrintOrder());
        assertSame(sheet, stream.getSheet());
        assertNotNull(stream.getPrintSetup());
    }

    @Test
    public void testLandScapePrint() {
        PrintStream stream = new PrintStream(sheet).landScapePrint();
        assertEquals(3, stream.getOrientation());
    }

    @Test
    public void testMargin() {
        PrintStream stream = new PrintStream(sheet)
                .leftMargin(0.5)
                .rightMargin(0.6)
                .topMargin(0.7)
                .bottomMargin(0.8);
        assertEquals(0.5, stream.getLeftMargin(), 0.0001);
        assertEquals(0.6, stream.getRightMargin(), 0.0001);
        assertEquals(0.7, stream.getTopMargin(), 0.0001);
        assertEquals(0.8, stream.getBottomMargin(), 0.0001);
    }

    @Test
    public void testRepeat() {
        PrintStream stream = new PrintStream(sheet).repeat(1, 2);
        int[] repeat = stream.getRepeat();
        assertEquals(1, repeat[1]);
        assertEquals(2, repeat[3]);
    }

    @Test
    public void testStaticFactory() {
        PrintStream stream = PrintStream.stream(sheet);
        assertSame(sheet, stream.getSheet());
    }

    @Test
    public void testParsePrint() {
        PrintOption option = new PrintOption();
        // scale 不能为 null 否则 parsePrint 拆箱 NPE
        option.setScale(90);
        option.setPrintGridLines(true);
        option.setFit(true);
        option.setCopies(2);
        option.setPageStart(2);
        option.setUsePage(true);
        option.setPaper(ExcelPaperType.A4);
        option.setLeftMargin(0.5);
        option.setHorizontallyCenter(true);
        PrintSetup setup = PrintStream.parsePrint(sheet, option);
        assertNotNull(setup);
        PrintStream check = new PrintStream(sheet);
        assertEquals(90, check.getScale());
        assertTrue(check.isPrintGridLines());
        assertTrue(check.isFit());
        assertEquals(2, check.getCopies());
        assertEquals(2, check.getPageStart());
        assertEquals(ExcelPaperType.A4.getCode(), check.getPaper());
        assertEquals(0.5, check.getLeftMargin(), 0.0001);
        assertTrue(check.isHorizontallyCenter());
    }

}

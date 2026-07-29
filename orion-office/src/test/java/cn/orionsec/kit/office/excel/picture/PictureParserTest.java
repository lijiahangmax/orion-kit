package cn.orionsec.kit.office.excel.picture;

import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * PictureParser 测试
 */
public class PictureParserTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private byte[] createPngBytes() throws Exception {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    @Test
    public void testAnalysisPicture() throws Exception {
        byte[] png = createPngBytes();
        File file = folder.newFile("pic.xlsx");
        // 在 (row=2, col=1) 插入图片
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("pics");
            int pictureIndex = workbook.addPicture(png, Workbook.PICTURE_TYPE_PNG);
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 1, 2, 2, 3);
            drawing.createPicture(anchor, pictureIndex);
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
        // 重新打开并解析
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
            PictureParser parser = new PictureParser(workbook, workbook.getSheetAt(0));
            parser.analysis();
            PictureData picture = parser.getPicture(2, 1);
            assertNotNull(picture);
            assertArrayEquals(png, picture.getData());
            assertTrue(picture.getMimeType().contains("png"));
            // 不存在的位置
            assertNull(parser.getPicture(0, 0));
        }
    }

    @Test
    public void testAnalysisWithoutPicture() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("empty");
            PictureParser parser = new PictureParser(workbook, sheet);
            parser.analysis();
            assertNull(parser.getPicture(0, 0));
        }
    }

}

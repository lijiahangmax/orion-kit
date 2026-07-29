package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * FileTypes 工具类测试
 */
public class FileTypesTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testGetFileTypeByBytes_png() {
        // PNG file header
        byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String type = FileTypes.getFileType(pngHeader);
        assertEquals("png", type);
    }

    @Test
    public void testGetFileTypeByBytes_jpg() {
        byte[] jpgHeader = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String type = FileTypes.getFileType(jpgHeader);
        assertEquals("jpg", type);
    }

    @Test
    public void testGetFileTypeByBytes_pdf() {
        byte[] pdfHeader = new byte[]{0x25, 0x50, 0x44, 0x46, 0x2D, 0x31, 0x2E, 0x33, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        String type = FileTypes.getFileType(pdfHeader);
        assertEquals("pdf", type);
    }

    @Test
    public void testGetFileTypeByBytes_unknown() {
        byte[] unknownHeader = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x20};
        String type = FileTypes.getFileType(unknownHeader);
        assertNull(type);
    }

    @Test
    public void testGetFileTypeByFile() throws Exception {
        File pngFile = tempFolder.newFile("test.png");
        try (FileOutputStream fos = new FileOutputStream(pngFile)) {
            fos.write(new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        }
        String type = FileTypes.getFileType(pngFile);
        assertEquals("png", type);
    }

    @Test
    public void testGetFileTypeByInputStream() {
        byte[] pngHeader = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ByteArrayInputStream in = new ByteArrayInputStream(pngHeader);
        String type = FileTypes.getFileType(in);
        assertEquals("png", type);
    }

    @Test
    public void testGetContentType() throws Exception {
        File txtFile = tempFolder.newFile("test.txt");
        String contentType = FileTypes.getContentType(txtFile);
        assertNotNull(contentType);
    }
}

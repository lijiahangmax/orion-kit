package cn.orionsec.kit.lang.utils.io;

import cn.orionsec.kit.lang.utils.io.compress.CompressTypeEnum;
import cn.orionsec.kit.lang.utils.io.compress.Compresses;
import cn.orionsec.kit.lang.utils.io.compress.FileCompressor;
import cn.orionsec.kit.lang.utils.io.compress.FileDecompressor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * Compresses / CompressTypeEnum 压缩解压工具类测试
 */
public class CompressesTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testZipAndUnzip() throws Exception {
        // 创建临时目录和文件
        File srcDir = tempFolder.newFolder("src");
        File file1 = new File(srcDir, "file1.txt");
        File file2 = new File(srcDir, "file2.txt");
        Files.write(file1.toPath(), "content of file1".getBytes());
        Files.write(file2.toPath(), "content of file2".getBytes());

        // 压缩
        File zipFile = new File(tempFolder.getRoot(), "test.zip");
        Compresses.zip(srcDir, zipFile.getAbsolutePath());
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);

        // 解压
        File unzipDir = tempFolder.newFolder("unzipped");
        Compresses.unzip(zipFile, unzipDir);

        // 验证解压后的文件
        File unzippedSrc = new File(unzipDir, srcDir.getName());
        File unzippedFile1 = new File(unzippedSrc, "file1.txt");
        File unzippedFile2 = new File(unzippedSrc, "file2.txt");
        assertTrue(unzippedFile1.exists());
        assertTrue(unzippedFile2.exists());
        assertEquals("content of file1", new String(Files.readAllBytes(unzippedFile1.toPath())));
        assertEquals("content of file2", new String(Files.readAllBytes(unzippedFile2.toPath())));
    }

    @Test
    public void testCompressTypeEnumOf() {
        assertEquals(CompressTypeEnum.ZIP, CompressTypeEnum.of(".zip"));
        assertEquals(CompressTypeEnum.GZ, CompressTypeEnum.of(".gz"));
        assertEquals(CompressTypeEnum.TAR, CompressTypeEnum.of(".tar"));
        assertNull(CompressTypeEnum.of(null));
        assertNull(CompressTypeEnum.of(".unknown"));
    }

    @Test
    public void testCompressTypeEnumCompressor() {
        FileCompressor compressor = CompressTypeEnum.ZIP.compressor().get();
        assertNotNull(compressor);
        assertEquals("zip", compressor.getSuffix());
    }

    @Test
    public void testCompressTypeEnumDecompressor() {
        FileDecompressor decompressor = CompressTypeEnum.ZIP.decompressor().get();
        assertNotNull(decompressor);
        assertEquals("zip", decompressor.getSuffix());
    }

    @Test
    public void testZipCompressorWithByteArray() throws Exception {
        FileCompressor compressor = CompressTypeEnum.ZIP.compressor().get();
        compressor.addFile("test.txt", "hello world zip".getBytes());
        File zipFile = new File(tempFolder.getRoot(), "byte_test.zip");
        compressor.setAbsoluteCompressPath(zipFile.getAbsolutePath());
        compressor.compress();
        assertTrue(zipFile.exists());
        assertTrue(zipFile.length() > 0);
    }
}

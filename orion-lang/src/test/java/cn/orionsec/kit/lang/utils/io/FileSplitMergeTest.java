package cn.orionsec.kit.lang.utils.io;

import cn.orionsec.kit.lang.utils.io.split.FileMerge;
import cn.orionsec.kit.lang.utils.io.split.FileSplit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * FileSplit / FileMerge 文件拆分合并测试
 */
public class FileSplitMergeTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testSplitByBlockCount() throws Exception {
        // 创建一个大一点的临时文件
        File srcFile = tempFolder.newFile("splitme.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("line ").append(i).append(" content data for splitting\n");
        }
        Files.write(srcFile.toPath(), sb.toString().getBytes());

        // 拆分为4块
        FileSplit split = new FileSplit(srcFile, 4);
        String[] blockPaths = split.call();

        assertNotNull(blockPaths);
        assertEquals(4, blockPaths.length);
        for (String path : blockPaths) {
            File blockFile = new File(path);
            assertTrue(blockFile.exists());
            assertTrue(blockFile.length() > 0);
        }
    }

    @Test
    public void testSplitByBlockSize() throws Exception {
        File srcFile = tempFolder.newFile("splitsize.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("data line ").append(i).append("\n");
        }
        byte[] content = sb.toString().getBytes();
        Files.write(srcFile.toPath(), content);

        // 按块大小拆分
        long blockSize = content.length / 3 + 1;
        FileSplit split = new FileSplit(srcFile, blockSize);
        String[] blockPaths = split.call();

        assertNotNull(blockPaths);
        assertTrue(blockPaths.length >= 2);
    }

    @Test
    public void testSplitAndMerge() throws Exception {
        // 创建临时源文件
        File srcFile = tempFolder.newFile("mergetest.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("merge line ").append(i).append(" data\n");
        }
        String originalContent = sb.toString();
        Files.write(srcFile.toPath(), originalContent.getBytes());

        // 拆分为3块
        FileSplit split = new FileSplit(srcFile, 3);
        String[] blockPaths = split.call();
        assertNotNull(blockPaths);
        assertEquals(3, blockPaths.length);

        // 合并 - FileMerge needs a directory containing the block files
        File blockDir = new File(blockPaths[0]).getParentFile();
        FileMerge merge = new FileMerge(blockDir);
        String mergedPath = merge.call();

        assertNotNull(mergedPath);
        File mergedFile = new File(mergedPath);
        assertTrue(mergedFile.exists());

        // 验证合并后的内容与原始一致
        String mergedContent = new String(Files.readAllBytes(mergedFile.toPath()));
        assertEquals(originalContent, mergedContent);
    }

    @Test
    public void testSplitBlockSizeLargerThanFile() throws Exception {
        File srcFile = tempFolder.newFile("small.txt");
        Files.write(srcFile.toPath(), "small content".getBytes());

        // 块大小大于文件, 应该返回原文件路径
        FileSplit split = new FileSplit(srcFile, 1024 * 1024L);
        String[] paths = split.call();
        assertNotNull(paths);
        assertEquals(1, paths.length);
        assertEquals(srcFile.getAbsolutePath(), paths[0]);
    }
}

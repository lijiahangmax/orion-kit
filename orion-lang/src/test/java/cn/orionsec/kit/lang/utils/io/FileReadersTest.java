package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * FileReaders 工具类测试
 */
public class FileReadersTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testReadFromRandomAccessFile() throws Exception {
        File file = tempFolder.newFile("reader.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("hello world content".getBytes());
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            byte[] result = FileReaders.read(raf, 0, 5);
            assertEquals("hello", new String(result));
        }
    }

    @Test
    public void testReadLineFromRandomAccessFile() throws Exception {
        File file = tempFolder.newFile("readline.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("first line\nsecond line\n".getBytes());
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            String line = FileReaders.readLine(raf);
            assertEquals("first line", line);
        }
    }

    @Test
    public void testReadAllLines() throws Exception {
        File file = tempFolder.newFile("alllines.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("line1\nline2\nline3".getBytes());
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            String allLines = FileReaders.readAllLines(raf);
            assertTrue(allLines.contains("line1"));
            assertTrue(allLines.contains("line2"));
            assertTrue(allLines.contains("line3"));
        }
    }

    @Test
    public void testReadFileLines() throws Exception {
        File file = tempFolder.newFile("filelines.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("a\nb\nc\nd\ne".getBytes());
        }
        List<String> lines = FileReaders.readLines(file);
        assertEquals(5, lines.size());
        assertEquals("a", lines.get(0));
        assertEquals("e", lines.get(4));
    }

    @Test
    public void testReadFileLinesWithLimit() throws Exception {
        File file = tempFolder.newFile("limitlines.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("a\nb\nc\nd\ne".getBytes());
        }
        List<String> lines = FileReaders.readLines(file, 3);
        assertEquals(3, lines.size());
    }
}

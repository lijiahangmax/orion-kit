package cn.orionsec.kit.lang.utils.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * FileLocks 工具类测试
 */
public class FileLocksTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testChannelFileLock() throws Exception {
        File file = tempFolder.newFile("locktest.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("lock content".getBytes());
        }
        FileLocks.ChannelFileLock lock = FileLocks.getChannelFileLock(file);
        assertFalse(lock.isLocked());
        boolean locked = lock.tryLock();
        assertTrue(locked);
        assertTrue(lock.isLocked());
        lock.unLock();
        assertFalse(lock.isLocked());
    }

    @Test
    public void testChannelFileLockByPath() throws Exception {
        File file = tempFolder.newFile("locktest2.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("lock content".getBytes());
        }
        Path path = file.toPath();
        FileLocks.ChannelFileLock lock = FileLocks.getChannelFileLock(path);
        boolean locked = lock.tryLock();
        assertTrue(locked);
        assertTrue(lock.isLocked());
        lock.unLock();
    }

    @Test
    public void testNamedFileLock() throws Exception {
        File file = tempFolder.newFile("named_lock.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("content".getBytes());
        }
        FileLocks.NamedFileLock lock = FileLocks.getNamedFileLock(file);
        assertFalse(lock.isLocked());
        boolean locked = lock.tryLock();
        assertTrue(locked);
        assertTrue(lock.isLocked());
        lock.unLock();
        assertFalse(lock.isLocked());
    }

    @Test
    public void testPrefixFileLock() throws Exception {
        File file = tempFolder.newFile("prefix_lock.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("content".getBytes());
        }
        FileLocks.NamedFileLock lock = FileLocks.getPrefixFileLock(file);
        boolean locked = lock.tryLock();
        assertTrue(locked);
        lock.unLock();
    }

    @Test
    public void testSuffixFileLock() throws Exception {
        File file = tempFolder.newFile("suffix_lock.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write("content".getBytes());
        }
        FileLocks.NamedFileLock lock = FileLocks.getSuffixFileLock(file);
        boolean locked = lock.tryLock();
        assertTrue(locked);
        lock.unLock();
    }
}

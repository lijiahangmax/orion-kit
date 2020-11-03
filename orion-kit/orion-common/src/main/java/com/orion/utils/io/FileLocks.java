package com.orion.utils.io;

import com.orion.able.Lockable;
import com.orion.utils.Exceptions;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件锁工具
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/20 22:15
 */
public class FileLocks {

    private FileLocks() {
    }

    /**
     * 获取文件管道锁
     *
     * @param file 文件
     * @return 管道锁
     */
    public static ChannelFileLock getChannelFileLock(String file) {
        return new ChannelFileLock(file);
    }

    /**
     * 获取文件管道锁
     *
     * @param file 文件
     * @return 管道锁
     */
    public static ChannelFileLock getChannelFileLock(File file) {
        return new ChannelFileLock(file);
    }

    /**
     * 获取文件后缀锁
     *
     * @param file 文件
     * @return 后缀锁
     */
    public static SuffixFileLock getSuffixFileLock(String file) {
        return new SuffixFileLock(file);
    }

    /**
     * 获取文件后缀锁
     *
     * @param file 文件
     * @return 后缀锁
     */
    public static SuffixFileLock getSuffixFileLock(File file) {
        return new SuffixFileLock(file);
    }

    /**
     * 获取文件后缀锁
     *
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 后缀锁
     */
    public static SuffixFileLock getSuffixFileLock(String lockSuffix, String file) {
        return new SuffixFileLock(lockSuffix, file);
    }

    /**
     * 获取文件后缀锁
     *
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 后缀锁
     */
    public static SuffixFileLock getSuffixFileLock(String lockSuffix, File file) {
        return new SuffixFileLock(lockSuffix, file);
    }

    /**
     * 文件管道锁
     */
    public static class ChannelFileLock implements Lockable {

        private File file;
        private FileChannel channel;
        private FileLock lock;

        private ChannelFileLock(String file) {
            this.file = new File(file);
        }

        private ChannelFileLock(File file) {
            this.file = file;
        }

        @Override
        public boolean tryLock() {
            boolean success = false;
            try {
                Path path = Paths.get(file.getPath());
                if (channel != null && channel.isOpen()) {
                    return false;
                }
                channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.READ);
                lock = channel.tryLock();
                if (lock != null) {
                    success = true;
                    return true;
                }
            } catch (Exception e) {
                Exceptions.printStacks(e);
                return false;
            } finally {
                if (!success) {
                    Streams.close(channel);
                }
            }
            return false;
        }

        @Override
        public void unLock() {
            try {
                if (lock != null) {
                    lock.release();
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            } finally {
                Streams.close(channel);
            }
        }

        @Override
        public boolean checkLock() {
            if (lock == null) {
                return false;
            } else {
                return lock.isValid();
            }
        }

    }

    /**
     * 文件后缀锁
     */
    public static class SuffixFileLock implements Lockable {

        /**
         * 锁后缀
         */
        private String suffix;

        /**
         * 源文件
         */
        private File file;

        /**
         * 锁文件
         */
        private File lockFile;

        private SuffixFileLock(String file) {
            this.suffix = ".lock";
            this.file = new File(file);
            this.lockFile = new File(this.file.getAbsolutePath() + this.suffix);
        }

        private SuffixFileLock(File file) {
            this.file = file;
            this.suffix = ".lock";
            this.lockFile = new File(this.file.getAbsolutePath() + this.suffix);
        }

        private SuffixFileLock(String suffix, String file) {
            this.suffix = "." + suffix;
            this.file = new File(file);
            this.lockFile = new File(this.file.getAbsolutePath() + this.suffix);
        }

        private SuffixFileLock(String suffix, File file) {
            this.suffix = "." + suffix;
            this.file = file;
            this.lockFile = new File(this.file.getAbsolutePath() + this.suffix);
        }

        @Override
        public boolean tryLock() {
            if (!file.exists()) {
                throw Exceptions.ioRuntime("File Not Found Error: " + file.getAbsolutePath());
            }
            if (lockFile.exists()) {
                return true;
            }
            try {
                Files1.mkdirs(lockFile.getParentFile());
                return lockFile.createNewFile();
            } catch (Exception e) {
                throw Exceptions.ioRuntime("Create Lock File Error: " + file.getAbsolutePath());
            }
        }

        @Override
        public void unLock() {
            if (lockFile.exists() && lockFile.isFile()) {
                Files1.deleteFile(lockFile);
            }
        }

        @Override
        public boolean checkLock() {
            return lockFile.exists();
        }

    }

}

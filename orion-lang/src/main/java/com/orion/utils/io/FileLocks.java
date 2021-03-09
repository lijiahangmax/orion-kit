package com.orion.utils.io;

import com.orion.able.Lockable;
import com.orion.constant.Const;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

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
     * 获取文件管道锁
     *
     * @param file 文件
     * @return 管道锁
     */
    public static ChannelFileLock getChannelFileLock(Path file) {
        return new ChannelFileLock(file);
    }

    /**
     * 获取文件前缀锁
     *
     * @param file 文件
     * @return 前缀锁
     */
    public static NamedFileLock getPrefixFileLock(String file) {
        return new NamedFileLock(file).suffix(Strings.EMPTY);
    }

    /**
     * 获取文件前缀锁
     *
     * @param file 文件
     * @return 前缀锁
     */
    public static NamedFileLock getPrefixFileLock(File file) {
        return new NamedFileLock(file).suffix(Strings.EMPTY);
    }

    /**
     * 获取文件前缀锁
     *
     * @param lockPrefix 锁前缀
     * @param file       文件
     * @return 前缀锁
     */
    public static NamedFileLock getPrefixFileLock(String lockPrefix, String file) {
        return new NamedFileLock(file).prefix(lockPrefix).suffix(Strings.EMPTY);
    }

    /**
     * 获取文件前缀锁
     *
     * @param lockPrefix 锁前缀
     * @param file       文件
     * @return 前缀锁
     */
    public static NamedFileLock getPrefixFileLock(String lockPrefix, File file) {
        return new NamedFileLock(file).prefix(lockPrefix).suffix(Strings.EMPTY);
    }

    /**
     * 获取文件后缀锁
     *
     * @param file 文件
     * @return 后缀锁
     */
    public static NamedFileLock getSuffixFileLock(String file) {
        return new NamedFileLock(file).prefix(Strings.EMPTY);
    }

    /**
     * 获取文件后缀锁
     *
     * @param file 文件
     * @return 后缀锁
     */
    public static NamedFileLock getSuffixFileLock(File file) {
        return new NamedFileLock(file).prefix(Strings.EMPTY);
    }

    /**
     * 获取文件后缀锁
     *
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 后缀锁
     */
    public static NamedFileLock getSuffixFileLock(String lockSuffix, String file) {
        return new NamedFileLock(file).prefix(Strings.EMPTY).suffix(lockSuffix);
    }

    /**
     * 获取文件后缀锁
     *
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 后缀锁
     */
    public static NamedFileLock getSuffixFileLock(String lockSuffix, File file) {
        return new NamedFileLock(file).prefix(Strings.EMPTY).suffix(lockSuffix);
    }

    /**
     * 获取文件名称锁
     *
     * @param file 文件
     * @return 名称锁
     */
    public static NamedFileLock getNamedFileLock(String file) {
        return new NamedFileLock(file);
    }

    /**
     * 获取文件名称锁
     *
     * @param file 文件
     * @return 名称锁
     */
    public static NamedFileLock getNamedFileLock(File file) {
        return new NamedFileLock(file);
    }

    /**
     * 获取文件名称锁
     *
     * @param lockPrefix 锁前缀
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 名称锁
     */
    public static NamedFileLock getNamedFileLock(String lockPrefix, String lockSuffix, String file) {
        return new NamedFileLock(file).prefix(lockPrefix).suffix(lockSuffix);
    }

    /**
     * 获取文件名称锁
     *
     * @param lockPrefix 锁前缀
     * @param lockSuffix 锁后缀
     * @param file       文件
     * @return 名称锁
     */
    public static NamedFileLock getNamedFileLock(String lockPrefix, String lockSuffix, File file) {
        return new NamedFileLock(file).prefix(lockPrefix).suffix(lockSuffix);
    }

    /**
     * 文件管道锁
     */
    public static class ChannelFileLock implements Lockable {

        private Path file;
        private FileChannel channel;
        private FileLock lock;

        private ChannelFileLock(String file) {
            this.file = Paths.get(file);
        }

        private ChannelFileLock(File file) {
            this.file = Paths.get(file.getAbsolutePath());
        }

        private ChannelFileLock(Path file) {
            this.file = file;
        }

        @Override
        public boolean tryLock() {
            boolean success = false;
            try {
                if (channel != null && channel.isOpen()) {
                    return false;
                }
                channel = FileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.READ);
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
     * 文件名称锁
     */
    public static class NamedFileLock implements Lockable {

        /**
         * 锁前缀
         */
        private String prefix;

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

        private NamedFileLock(String file) {
            this(new File(file));
        }

        private NamedFileLock(File file) {
            this.file = file;
            this.prefix = "~";
            this.suffix = ".lock";
        }

        public NamedFileLock prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public NamedFileLock suffix(String suffix) {
            this.suffix = "." + suffix;
            return this;
        }

        @Override
        public boolean tryLock() {
            init();
            if (!file.exists()) {
                throw Exceptions.ioRuntime("file not found error: " + file.getAbsolutePath());
            }
            if (lockFile.exists()) {
                return true;
            }
            try {
                Files1.mkdirs(lockFile.getParentFile());
                return lockFile.createNewFile();
            } catch (Exception e) {
                throw Exceptions.ioRuntime("create lock file error: " + file.getAbsolutePath());
            }
        }

        @Override
        public void unLock() {
            init();
            if (lockFile.exists() && lockFile.isFile()) {
                Files1.deleteFile(lockFile);
            }
        }

        @Override
        public boolean checkLock() {
            init();
            return lockFile.exists();
        }

        private void init() {
            if (this.lockFile == null) {
                this.lockFile = new File(this.file.getParent() + Const.SEPARATOR + this.prefix + this.file.getName() + this.suffix);
            }
        }

    }

}

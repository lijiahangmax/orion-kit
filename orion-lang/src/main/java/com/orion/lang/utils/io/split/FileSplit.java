package com.orion.lang.utils.io.split;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

/**
 * 文件拆分器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/14 15:38
 */
public class FileSplit implements Callable<String[]> {

    /**
     * 默认分割为几块 8
     */
    private static final int DEFAULT_SPLIT_BLOCK = 8;

    /**
     * 文件
     */
    private final File file;

    /**
     * 块数量
     */
    private final int blockCount;

    /**
     * 块大小
     */
    private long blockSize;

    /**
     * 缓冲区大小
     */
    private int bufferSize;

    /**
     * 是否不切分
     */
    private boolean nothing;

    public FileSplit(String file) {
        this(new File(file), DEFAULT_SPLIT_BLOCK, Const.BUFFER_KB_2);
    }

    public FileSplit(File file) {
        this(file, DEFAULT_SPLIT_BLOCK, Const.BUFFER_KB_2);
    }

    public FileSplit(String file, int blockCount) {
        this(new File(file), blockCount, Const.BUFFER_KB_2);
    }

    public FileSplit(File file, int blockCount) {
        this(file, blockCount, Const.BUFFER_KB_2);
    }

    public FileSplit(String file, long blockSize) {
        this(new File(file), blockSize, Const.BUFFER_KB_2);
    }

    public FileSplit(File file, long blockSize) {
        this(file, blockSize, Const.BUFFER_KB_2);
    }

    public FileSplit(String file, int blockCount, int bufferSize) {
        this(new File(file), blockCount, bufferSize);
    }

    public FileSplit(String file, long blockSize, int bufferSize) {
        this(new File(file), blockSize, bufferSize);
    }

    public FileSplit(File file, int blockCount, int bufferSize) {
        if (blockCount < 2) {
            blockCount = DEFAULT_SPLIT_BLOCK;
        }
        this.blockCount = blockCount;
        if (bufferSize < Const.BUFFER_KB_2) {
            bufferSize = Const.BUFFER_KB_2;
        }
        this.bufferSize = bufferSize;
        if (file == null || !file.exists()) {
            throw Exceptions.runtime(Strings.format("file not found {}", file));
        }
        if (file.length() == 0) {
            throw Exceptions.runtime(Strings.format("file is empty {}", file));
        }
        this.file = file;
        double size = (double) file.length() / (double) blockCount;
        if (Double.compare(size, (int) size) == 0) {
            this.blockSize = (long) size;
        } else {
            this.blockSize = (long) size + 1;
        }
    }

    public FileSplit(File file, long blockSize, int bufferSize) {
        if (bufferSize < Const.BUFFER_KB_2) {
            bufferSize = Const.BUFFER_KB_2;
        }
        this.bufferSize = bufferSize;
        if (file == null || !file.exists() || file.length() == 0) {
            throw Exceptions.runtime("file is illegal");
        }
        this.file = file;
        if (blockSize < Const.BUFFER_KB_1) {
            blockSize = Const.BUFFER_KB_1;
        }
        if (blockSize >= file.length()) {
            this.nothing = true;
        }
        this.blockSize = blockSize;
        int blockCount = (int) (file.length() / blockSize);
        if (file.length() % blockSize != 0) {
            blockCount++;
        }
        this.blockCount = blockCount;
    }

    @Override
    public String[] call() {
        if (nothing) {
            return new String[]{file.getAbsolutePath()};
        }
        String[] blockPaths = new String[blockCount];
        File df = new File(file.getAbsolutePath() + ".block");
        Files1.mkdirs(df);
        for (int i = 0; i < blockCount; i++) {
            String splitFileName = df.getAbsolutePath() + "\\" + file.getName() + ".000" + (i + 1);
            Files1.touch(splitFileName);
            blockPaths[i] = splitFileName;
            randomReadToFile((i * blockSize), new File(splitFileName));
        }
        return blockPaths;
    }

    private void randomReadToFile(long offset, File writeFile) {
        RandomAccessFile accessFile = null;
        FileOutputStream outputStream = null;
        try {
            accessFile = new RandomAccessFile(file, Const.ACCESS_R);
            accessFile.seek(offset);
            if (accessFile.length() - offset < blockSize) {
                blockSize = accessFile.length() - offset;
            }
            Files1.touch(writeFile);
            outputStream = new FileOutputStream(writeFile);
            if (blockSize < bufferSize) {
                bufferSize = (int) blockSize;
            }
            long times = blockSize / bufferSize;
            long more = blockSize % bufferSize;
            if (more != 0) {
                times++;
            } else {
                more = bufferSize;
            }
            byte[] buffer = new byte[bufferSize];
            for (int i = 0; i < times - 1; i++) {
                outputStream.write(buffer, 0, accessFile.read(buffer));
            }
            buffer = new byte[(int) more];
            outputStream.write(buffer, 0, accessFile.read(buffer));
            outputStream.flush();
        } catch (Exception e) {
            Exceptions.printStacks(e);
        } finally {
            Streams.close(outputStream);
            Streams.close(accessFile);
        }
    }

}

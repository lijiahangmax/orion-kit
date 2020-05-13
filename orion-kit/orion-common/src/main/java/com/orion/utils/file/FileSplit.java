package com.orion.utils.file;

import com.orion.utils.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

/**
 * 文件拆分器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/14 15:38
 */
public class FileSplit implements Callable<String[]> {

    /**
     * 默认分割为几块 8
     */
    private static int DEFAULT_SPLIT_BLOCK = 8;

    /**
     * 默认缓冲区大小 2m
     */
    private static int BUFFER_SIZE = 1024 * 2;

    /**
     * 文件
     */
    private File file;

    /**
     * 块大小
     */
    private long blockSize;

    /**
     * 块数量
     */
    private int blockCount;

    /**
     * 缓冲区大小
     */
    private int bufferSize;

    /**
     * 是否不切分
     */
    private boolean nothing;

    public FileSplit(String file) {
        this(new File(file), DEFAULT_SPLIT_BLOCK, BUFFER_SIZE);
    }

    public FileSplit(File file) {
        this(file, DEFAULT_SPLIT_BLOCK, BUFFER_SIZE);
    }

    public FileSplit(String file, int blockCount) {
        this(new File(file), blockCount, BUFFER_SIZE);
    }

    public FileSplit(File file, int blockCount) {
        this(file, blockCount, BUFFER_SIZE);
    }

    public FileSplit(String file, long blockSize) {
        this(new File(file), blockSize, BUFFER_SIZE);
    }

    public FileSplit(File file, long blockSize) {
        this(file, blockSize, BUFFER_SIZE);
    }

    public FileSplit(String file, int blockCount, int bufferSize) {
        this(new File(file), blockCount, bufferSize);
    }

    public FileSplit(File file, int blockCount, int bufferSize) {
        if (blockCount < 2) {
            blockCount = DEFAULT_SPLIT_BLOCK;
        }
        this.blockCount = blockCount;
        if (bufferSize < BUFFER_SIZE) {
            bufferSize = BUFFER_SIZE;
        }
        this.bufferSize = bufferSize;
        if (file == null || !file.exists() || file.length() == 0) {
            throw new RuntimeException("File is illegal");
        }
        this.file = file;
        double size = (double) file.length() / (double) blockCount;
        if (size == file.length() / blockCount) {
            this.blockSize = (long) size;
        } else {
            this.blockSize = (long) size + BUFFER_SIZE;
        }
    }

    public FileSplit(String file, long blockSize, int bufferSize) {
        this(new File(file), blockSize, bufferSize);

    }

    public FileSplit(File file, long blockSize, int bufferSize) {
        if (bufferSize < BUFFER_SIZE) {
            bufferSize = BUFFER_SIZE;
        }
        this.bufferSize = bufferSize;
        if (file == null || !file.exists() || file.length() == 0) {
            throw new RuntimeException("File is illegal");
        }
        this.file = file;
        if (blockSize < 1024) {
            blockSize = 1024;
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
        System.out.println("MD5 sign: " + Files1.md5(file));
        String[] blockPaths = new String[blockCount];
        File df = new File(file.getAbsolutePath() + ".block");
        df.mkdir();
        for (int i = 0; i < blockCount; i++) {
            String splitFileName = df.getAbsolutePath() + "\\" + file.getName() + ".000" + (i + 1);
            blockPaths[i] = splitFileName;
            randomReadToFile((i * blockSize), new File(splitFileName));
        }
        return blockPaths;
    }

    private void randomReadToFile(long offset, File writeFile) {
        RandomAccessFile accessFile;
        FileOutputStream outputStream = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            accessFile.seek(offset);
            if (accessFile.length() - offset < blockSize) {
                blockSize = accessFile.length() - offset;
            }
            if (!writeFile.exists()) {
                writeFile.createNewFile();
            }
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
            e.printStackTrace();
        } finally {
            Streams.closeQuietly(outputStream);
        }
    }

}

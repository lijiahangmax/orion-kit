/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.io.split;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSplit.class);

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
            LOGGER.error("FileSplit.randomReadToFile error", e);
        } finally {
            Streams.close(outputStream);
            Streams.close(accessFile);
        }
    }

}

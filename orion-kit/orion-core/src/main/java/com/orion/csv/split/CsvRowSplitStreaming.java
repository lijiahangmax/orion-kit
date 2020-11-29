package com.orion.csv.split;

import com.orion.csv.CsvExt;
import com.orion.csv.core.CsvSymbol;
import com.orion.csv.exporting.CsvBuilder;
import com.orion.csv.importing.CsvStream;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 行切分器 不可以获取行数据
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 13:43
 */
public class CsvRowSplitStreaming {

    /**
     * 读取流
     */
    private CsvStream stream;

    /**
     * 拆分文件最大行数
     */
    private int rowSize;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    /**
     * symbol
     */
    private CsvSymbol symbol;

    /**
     * 拆分输出对流
     */
    private List<OutputStream> dist;

    /**
     * 自动生成的文件目录
     */
    private String generatorPathDir;

    /**
     * 自动生成的文件名称
     */
    private String generatorBaseName;

    public CsvRowSplitStreaming(CsvExt ext, int rowSize) {
        Valid.notNull(ext, "split ext is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.stream = ext.stream();
        this.rowSize = rowSize;
    }

    public CsvRowSplitStreaming(CsvStream stream, int rowSize) {
        Valid.notNull(stream, "split stream is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.stream = stream;
        this.rowSize = rowSize;
    }

    /**
     * 设置拆分文件输出流
     *
     * @param s    symbol
     * @param dist dist
     * @return this
     */
    public CsvRowSplitStreaming dist(CsvSymbol s, OutputStream... dist) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(dist, "dist file is empty");
        this.dist = Lists.of(dist);
        this.symbol = s;
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param s    symbol
     * @param dist dist
     * @return this
     */
    public CsvRowSplitStreaming dist(CsvSymbol s, File... dist) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(dist, "dist file is empty");
        List<OutputStream> out = new ArrayList<>();
        for (File file : dist) {
            Files1.touch(file);
            out.add(Files1.openOutputStreamSafe(file));
        }
        this.dist = out;
        this.symbol = s;
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param s    symbol
     * @param dist dist
     * @return this
     */
    public CsvRowSplitStreaming dist(CsvSymbol s, String... dist) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(dist, "dist file is empty");
        List<OutputStream> out = new ArrayList<>();
        for (String file : dist) {
            Files1.touch(file);
            out.add(Files1.openOutputStreamSafe(file));
        }
        this.dist = out;
        this.symbol = s;
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @param s        symbol
     * @return this
     */
    public CsvRowSplitStreaming distPath(String pathDir, String baseName, CsvSymbol s) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        Valid.notNull(s, "csvSymbol is null");
        this.dist = null;
        this.symbol = s;
        this.generatorPathDir = pathDir;
        this.generatorBaseName = baseName;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvRowSplitStreaming skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvRowSplitStreaming skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public CsvRowSplitStreaming header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public CsvRowSplitStreaming execute() {
        if (generatorPathDir == null) {
            Valid.notNull(dist, "split file dist is null");
        }
        stream.skipLines(skip);
        for (int i = 0, size = Lists.size(dist), loop = size == 0 ? Integer.MAX_VALUE : size; i < loop; i++) {
            List<String[]> readLines = stream.clean().readLines(rowSize).lines();
            if (readLines.isEmpty()) {
                break;
            }
            List<String[]> fileLines = new ArrayList<>();
            if (this.header != null) {
                fileLines.add(this.header);
            }
            fileLines.addAll(readLines);
            OutputStream out;
            if (generatorPathDir != null) {
                out = generatorOutputStream(i);
            } else {
                out = dist.get(i);
            }
            try {
                new CsvBuilder(fileLines)
                        .symbol(symbol.getSymbol())
                        .charset(symbol.getCharset())
                        .dist(out)
                        .build();
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
            stream.clean();
        }
        return this;
    }

    /**
     * 关闭流
     */
    public void close() {
        if (dist != null) {
            for (OutputStream outputStream : dist) {
                Streams.close(outputStream);
            }
        }
    }

    /**
     * 生成OutputStream
     *
     * @param i index
     * @return ignore
     */
    private OutputStream generatorOutputStream(int i) {
        String path = Files1.getPath(generatorPathDir + "/" + generatorBaseName + "_row_split_" + (i + 1) + "." + symbol.getSuffix());
        Files1.touch(path);
        return Files1.openOutputStreamSafe(path);
    }

    public CsvStream getStream() {
        return stream;
    }

    public int getRowSize() {
        return rowSize;
    }

}

package com.orion.csv.split;

import com.orion.csv.CsvExt;
import com.orion.csv.core.CsvSymbol;
import com.orion.csv.exporting.CsvBuilder;
import com.orion.csv.importing.CsvStream;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 行切分器 可以获取行数据
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 10:44
 */
public class CsvRowSplit {

    /**
     * 读取流
     */
    private CsvStream stream;

    /**
     * 每个文件的数据行数
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
     * 每个文件的数据
     */
    private List<List<String[]>> fileLines = new ArrayList<>();

    public CsvRowSplit(CsvExt ext, int rowSize) {
        Valid.notNull(ext, "split ext is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.stream = ext.stream();
        this.rowSize = rowSize;
    }

    public CsvRowSplit(CsvStream stream, int rowSize) {
        Valid.notNull(stream, "split stream is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.stream = stream;
        this.rowSize = rowSize;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvRowSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvRowSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public CsvRowSplit header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public CsvRowSplit execute() {
        List<String[]> lines = stream.skipLines(skip).readLines().lines();
        int size = lines.size(), loop = size / rowSize, mod = size % rowSize;
        if (mod != 0) {
            loop++;
        }
        for (int i = 0; i < loop; i++) {
            int start = i * rowSize;
            int end = start + rowSize;
            if (i == loop - 1) {
                // last
                end = size;
            }
            boolean addHeader = header != null;
            List<String[]> fileLine = new ArrayList<>();
            for (int is = 0; start < end; start++, is++) {
                if (addHeader) {
                    fileLine.add(header);
                    start--;
                    addHeader = false;
                } else {
                    fileLine.add(lines.get(start));
                }
            }
            this.fileLines.add(fileLine);
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param s     symbol
     * @param files files
     * @return this
     */
    public CsvRowSplit write(CsvSymbol s, String... files) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(files, "write file is empty");
        for (int i = 0; i < fileLines.size(); i++) {
            if (i < files.length) {
                write(new File(files[i]), i, s);
            }
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param s     symbol
     * @param files files
     * @return this
     */
    public CsvRowSplit write(CsvSymbol s, File... files) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(files, "write file is empty");
        for (int i = 0; i < fileLines.size(); i++) {
            if (i < files.length) {
                write(files[i], i, s);
            }
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件流
     *
     * @param outs 流
     * @param s    symbol
     * @return this
     */
    public CsvRowSplit write(CsvSymbol s, OutputStream... outs) {
        Valid.notNull(s, "csvSymbol is null");
        Valid.notEmpty(outs, "write stream is empty");
        for (int i = 0; i < fileLines.size(); i++) {
            if (i < outs.length) {
                List<String[]> row = fileLines.get(i);
                OutputStream out = outs[i];
                try {
                    new CsvBuilder(row)
                            .symbol(s.getSymbol())
                            .charset(s.getCharset())
                            .dist(out)
                            .build();
                } catch (Exception e) {
                    throw Exceptions.ioRuntime(e);
                }
            }
        }
        return this;
    }

    /**
     * 写入数据到文件
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @param s        symbol
     * @return this
     */
    public CsvRowSplit writeToPath(String pathDir, String baseName, CsvSymbol s) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        Valid.notNull(s, "csvSymbol is null");
        for (int i = 0; i < fileLines.size(); i++) {
            String path = Files1.getPath(pathDir + "/" + baseName + "_row_split_" + (i + 1) + "." + s.getSuffix());
            write(new File(path), i, s);
        }
        return this;
    }

    /**
     * 写入数据到文件
     *
     * @param file     文件
     * @param rowIndex 文件数据索引
     * @param s        symbol
     */
    private void write(File file, int rowIndex, CsvSymbol s) {
        try {
            new CsvBuilder(fileLines.get(rowIndex))
                    .symbol(s.getSymbol())
                    .charset(s.getCharset())
                    .dist(file)
                    .build()
                    .close();
        } catch (Exception e) {
            e.printStackTrace();
            throw Exceptions.ioRuntime(e);
        }
    }

    public List<String[]> lines() {
        return stream.lines();
    }

    public CsvStream getStream() {
        return stream;
    }

}

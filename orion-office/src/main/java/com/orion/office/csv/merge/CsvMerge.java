package com.orion.office.csv.merge;

import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.writer.CsvArrayWriter;
import com.orion.utils.io.Streams;

import java.util.List;

/**
 * CSV行合并器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/15 17:10
 */
public class CsvMerge implements SafeCloseable {

    /**
     * 输出流
     */
    private CsvArrayWriter writer;

    /**
     * 跳过合并文件的行
     */
    private int skipRows;

    /**
     * 读取行缓冲区
     */
    private int bufferLine;

    public CsvMerge(CsvArrayWriter writer) {
        this.writer = writer;
        this.bufferLine = Const.N_100;
    }

    /**
     * 设置文件头
     *
     * @param header header
     * @return this
     */
    public CsvMerge header(String... header) {
        this.writer.headers(header);
        return this;
    }

    /**
     * 设置 读取行缓冲区
     *
     * @param bufferLine 缓冲区行数
     * @return this
     */
    public CsvMerge bufferLine(int bufferLine) {
        this.bufferLine = bufferLine;
        return this;
    }

    /**
     * 合并csv跳过一行
     *
     * @return this
     */
    public CsvMerge skip() {
        writer.skip();
        return this;
    }

    /**
     * 合并csv跳过多行
     *
     * @param skip 文件跳过的行
     * @return this
     */
    public CsvMerge skip(int skip) {
        writer.skip(skip);
        return this;
    }

    /**
     * 跳过合并文件一行
     *
     * @return this
     */
    public CsvMerge skipRows() {
        skipRows += 1;
        return this;
    }

    /**
     * 跳过合并文件多行
     *
     * @param skip skip
     * @return this
     */
    public CsvMerge skipRows(int skip) {
        skipRows += skip;
        return this;
    }

    /**
     * 执行合并
     *
     * @return this
     */
    public CsvMerge merge(CsvArrayReader reader) {
        if (skipRows != 0) {
            reader.skip(skipRows);
        }
        // 数据
        List<String[]> lines;
        while (!(lines = reader.clear().read(bufferLine).getRows()).isEmpty()) {
            writer.addRows(lines);
        }
        skipRows = 0;
        writer.flush();
        return this;
    }

    @Override
    public void close() {
        Streams.close(writer);
    }

    public CsvArrayWriter getWriter() {
        return writer;
    }

}

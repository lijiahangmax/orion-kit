package com.orion.office.csv.split;

import com.orion.constant.Const;
import com.orion.office.csv.CsvExt;
import com.orion.office.csv.core.CsvWriter;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.csv.writer.CsvArrayWriter;
import com.orion.office.support.DestinationGenerator;
import com.orion.utils.Arrays1;
import com.orion.utils.Valid;

import java.util.List;

/**
 * CSV 行切分器 可以获取行数据
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/15 10:44
 */
public class CsvRowSplit extends DestinationGenerator {

    /**
     * reader
     */
    private CsvArrayReader reader;

    /**
     * 拆分文件最大行数
     */
    private int limit;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 列
     */
    private int[] columns;

    private boolean end;

    public CsvRowSplit(CsvExt ext, int limit) {
        this(ext.arrayReader(), limit);
    }

    public CsvRowSplit(CsvArrayReader reader, int limit) {
        Valid.notNull(reader, "reader is null");
        Valid.lte(0, limit, "limit not be lte 0");
        this.reader = reader;
        this.limit = limit;
        this.suffix = Const.SUFFIX_CSV;
    }

    /**
     * 设置后缀
     *
     * @param suffix suffix
     * @return this
     */
    public CsvRowSplit suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public CsvRowSplit skip() {
        return skip(1);
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public CsvRowSplit skip(int skip) {
        reader.skip(skip);
        return this;
    }

    /**
     * 设置读取的列
     *
     * @param columns 列
     * @return this
     */
    public CsvRowSplit columns(int... columns) {
        if (!Arrays1.isEmpty(columns)) {
            this.columns = columns;
        }
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
    public CsvRowSplit split() {
        do {
            if (!super.hasNext()) {
                end = true;
                break;
            }
            List<String[]> rows = reader.clear().read(limit).getRows();
            if (rows.isEmpty()) {
                end = true;
                break;
            }
            if (rows.size() < limit) {
                end = true;
            }
            super.next();
            CsvArrayWriter currentWriter = new CsvArrayWriter(new CsvWriter(currentOutputStream, reader.getOption().toWriterOption()));
            if (!Arrays1.isEmpty(header)) {
                currentWriter.addRow(header);
            }
            if (Arrays1.isEmpty(columns)) {
                currentWriter.addRows(rows);
            } else {
                for (String[] row : rows) {
                    int length = row.length;
                    String[] newRow = new String[columns.length];
                    for (int i = 0; i < columns.length; i++) {
                        if (length > columns[i]) {
                            newRow[i] = row[columns[i]];
                        }
                    }
                    currentWriter.addRow(newRow);
                }
            }
            currentWriter.flush();
            if (autoClose) {
                currentWriter.close();
            }
        } while (!end);
        return this;
    }

    public CsvArrayReader getReader() {
        return reader;
    }

    public int getLimit() {
        return limit;
    }

}

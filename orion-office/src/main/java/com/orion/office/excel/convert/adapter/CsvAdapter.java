package com.orion.office.excel.convert.adapter;

import com.orion.lang.able.Adaptable;
import com.orion.lang.able.SafeCloseable;
import com.orion.lang.utils.io.Streams;
import com.orion.office.csv.writer.CsvArrayWriter;
import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * excel -> csv 适配器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/18 0:33
 */
public class CsvAdapter implements Adaptable<CsvAdapter>, SafeCloseable {

    /**
     * sheet
     */
    private final Sheet sheet;

    /**
     * CsvArrayWriter
     */
    private final CsvArrayWriter writer;

    /**
     * 跳过的行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    public CsvAdapter(Sheet sheet, CsvArrayWriter writer) {
        this.sheet = sheet;
        this.writer = writer;
    }

    /**
     * 跳过Excel一多
     *
     * @return this
     */
    public CsvAdapter skip() {
        skip += 1;
        return this;
    }

    /**
     * 跳过Excel行多
     *
     * @param i 行
     * @return this
     */
    public CsvAdapter skip(int i) {
        skip += i;
        return this;
    }

    /**
     * 设置Csv表头
     *
     * @param header 头
     * @return this
     */
    public CsvAdapter header(String... header) {
        if (header == null || header.length == 0) {
            this.header = null;
        } else {
            this.header = header;
        }
        return this;
    }

    @Override
    public CsvAdapter forNew() {
        if (header != null) {
            writer.headers(header);
        }
        int i = 0;
        for (Row cells : sheet) {
            if (i++ < skip) {
                continue;
            }
            List<String> row = new ArrayList<>();
            for (Cell cell : cells) {
                String value = Excels.getCellValue(cell);
                row.add(value);
            }
            writer.addRow(row.toArray(new String[0]));
        }
        writer.flush();
        writer.close();
        return this;
    }

    /**
     * 关闭sheet
     */
    @Override
    public void close() {
        try {
            Streams.close(sheet.getWorkbook());
        } catch (Exception e) {
            // skip streaming sheet UnsupportedOperationException
        }
    }

    public Sheet getSheet() {
        return sheet;
    }

    public CsvArrayWriter getWriter() {
        return writer;
    }

}

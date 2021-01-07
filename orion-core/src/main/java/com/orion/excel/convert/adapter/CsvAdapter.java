package com.orion.excel.convert.adapter;

import com.orion.able.Adaptable;
import com.orion.able.SafeCloseable;
import com.orion.csv.exporting.CsvExport;
import com.orion.excel.Excels;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel -> CSV 适配器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/18 0:33
 */
public class CsvAdapter implements Adaptable<CsvAdapter>, SafeCloseable {

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * CsvExport
     */
    private CsvExport export;

    /**
     * 跳过的行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    public CsvAdapter(Sheet sheet, CsvExport export) {
        this.sheet = sheet;
        this.export = export;
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
            export.addRecord(header);
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
            export.addRecord(row.toArray(new String[0]));
        }
        export.flush().close();
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

    public CsvExport getExport() {
        return export;
    }

}

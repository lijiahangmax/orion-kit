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
package cn.orionsec.kit.office.excel.convert.adapter;

import cn.orionsec.kit.lang.able.Adaptable;
import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import cn.orionsec.kit.office.excel.Excels;
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

/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.office.csv.convert.adapter;

import cn.orionsec.kit.lang.able.Adaptable;
import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import cn.orionsec.kit.office.excel.writer.BaseExcelWriteable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;

/**
 * csv -> excel 文本适配器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/18 2:36
 */
public class ExcelAdapter extends BaseExcelWriteable implements Adaptable<ExcelAdapter>, SafeCloseable {

    /**
     * sheet
     */
    private final Sheet sheet;

    /**
     * csvStream
     */
    private final CsvArrayReader reader;

    /**
     * 跳过的行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 读取行缓冲区
     */
    private int bufferLine;

    public ExcelAdapter(CsvExt csvExt) {
        this(csvExt.arrayReader());
    }

    public ExcelAdapter(CsvArrayReader reader) {
        super(new XSSFWorkbook());
        this.reader = reader;
        this.sheet = workbook.createSheet();
        this.bufferLine = Const.N_100;
    }

    /**
     * 设置 读取行缓冲区
     *
     * @param bufferLine 缓冲区行数
     * @return this
     */
    public ExcelAdapter bufferLine(int bufferLine) {
        this.bufferLine = bufferLine;
        return this;
    }

    /**
     * 跳过一行
     *
     * @return this
     */
    public ExcelAdapter skip() {
        skip += 1;
        return this;
    }

    /**
     * 跳过行多
     *
     * @param i 行
     * @return this
     */
    public ExcelAdapter skip(int i) {
        skip += i;
        return this;
    }

    /**
     * 设置Excel表头
     *
     * @param header 头
     * @return this
     */
    public ExcelAdapter header(String... header) {
        if (header == null || header.length == 0) {
            this.header = null;
        } else {
            this.header = header;
        }
        return this;
    }

    @Override
    public ExcelAdapter forNew() {
        int i = 0;
        // 表头
        if (header != null) {
            Row headerRow = sheet.createRow(i++);
            for (int hi = 0; hi < header.length; hi++) {
                Cell hc = headerRow.createCell(hi);
                hc.setCellValue(header[hi]);
            }
        }
        reader.skip(skip);
        // 数据
        Collection<String[]> lines;
        while (!(lines = reader.clear().read(bufferLine).getRows()).isEmpty()) {
            for (String[] line : lines) {
                Row rr = sheet.createRow(i++);
                for (int ri = 0; ri < line.length; ri++) {
                    Cell rc = rr.createCell(ri);
                    rc.setCellValue(line[ri]);
                }
            }
        }
        return this;
    }

    /**
     * 关闭workbook
     */
    @Override
    public void close() {
        Streams.close(workbook);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public CsvArrayReader getReader() {
        return reader;
    }

}

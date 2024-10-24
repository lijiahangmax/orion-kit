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
package com.orion.office.excel.split;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.office.excel.Excels;
import com.orion.office.excel.writer.BaseExcelWriteable;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel 列拆分器 拆分一个文件一个sheet 不支持复杂类型 占用内存少
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnSingleSplit extends BaseExcelWriteable {

    /**
     * source workbook
     */
    private final Workbook sourceWorkbook;

    /**
     * source sheet
     */
    public Sheet sourceSheet;

    /**
     * target sheet
     */
    public Sheet targetSheet;

    /**
     * 跳过行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] headers;

    /**
     * 列
     */
    private final int[] columns;

    /**
     * 是否是流式读取 (样式)
     */
    private final boolean streaming;

    public ExcelColumnSingleSplit(Workbook sourceWorkbook, Sheet sourceSheet, int... columns) {
        super(new SXSSFWorkbook());
        this.sourceWorkbook = Valid.notNull(sourceWorkbook, "split workbook is null");
        this.sourceSheet = Valid.notNull(sourceSheet, "split sheet is null");
        Valid.isTrue(!Arrays1.isEmpty(columns), "split columns is empty");
        this.targetSheet = super.workbook.createSheet(sourceSheet.getSheetName());
        this.columns = columns;
        this.streaming = Excels.isStreamingSheet(sourceSheet);
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelColumnSingleSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelColumnSingleSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 文件的表头
     * @return ignore
     */
    public ExcelColumnSingleSplit header(String... headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelColumnSingleSplit split() {
        ExcelColumnSplitSupport.split(sourceSheet, super.workbook, targetSheet, columns, headers, skip, streaming);
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelColumnSingleSplit protect(String password) {
        targetSheet.protectSheet(password);
        return this;
    }

    @Override
    public void close() {
        super.close();
        Streams.close(sourceWorkbook);
    }

    public Workbook getSourceWorkbook() {
        return sourceWorkbook;
    }

    public Sheet getSourceSheet() {
        return sourceSheet;
    }

    public Workbook getTargetWorkbook() {
        return super.workbook;
    }

    public Sheet getTargetSheet() {
        return targetSheet;
    }

    public int[] getColumns() {
        return columns;
    }

}

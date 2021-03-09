package com.orion.office.excel.split;

import com.orion.office.excel.Excels;
import com.orion.office.excel.writer.BaseExcelWriteable;
import com.orion.utils.Arrays1;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Excel 列拆分器 拆分一个文件一个sheet 不支持复杂类型 占用内存少
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnSingleSplit extends BaseExcelWriteable {

    /**
     * source workbook
     */
    private Workbook sourceWorkbook;

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
    private int[] columns;

    /**
     * 是否是流式读取 (样式)
     */
    private boolean streaming;

    public ExcelColumnSingleSplit(Workbook sourceWorkbook, Sheet sourceSheet, int... columns) {
        super(new SXSSFWorkbook());
        Valid.notNull(sourceWorkbook, "split workbook is null");
        Valid.notNull(sourceSheet, "split sheet is null");
        Valid.isFalse(Arrays1.isEmpty(columns), "split columns is empty");
        this.sourceWorkbook = sourceWorkbook;
        this.sourceSheet = sourceSheet;
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

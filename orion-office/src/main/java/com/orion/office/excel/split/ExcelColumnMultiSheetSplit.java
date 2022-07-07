package com.orion.office.excel.split;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.office.excel.Excels;
import com.orion.office.excel.writer.BaseExcelWriteable;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel 列拆分器 只是拆分一个文件 多个sheet 不支持复杂类型 占用内存多
 * 不支持流式读取 (流式只能读取一次)
 * 流式请用 {@link ExcelColumnSingleSplit}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnMultiSheetSplit extends BaseExcelWriteable {

    /**
     * source workbook
     */
    private final Workbook sourceWorkbook;

    /**
     * source sheet
     */
    private Sheet sourceSheet;

    /**
     * 跳过行数
     */
    private int skip;

    /**
     * target sheet数量
     */
    private int sheetNum;

    public ExcelColumnMultiSheetSplit(Workbook sourceWorkbook, int sourceSheetIndex) {
        this(sourceWorkbook);
        Valid.gte(sourceSheetIndex, 0, "split sheet index must >= 0");
        this.sourceSheet = sourceWorkbook.getSheetAt(sourceSheetIndex);
    }

    public ExcelColumnMultiSheetSplit(Workbook sourceWorkbook, String sourceSheetName) {
        this(sourceWorkbook);
        Valid.notBlank(sourceSheetName, "split sheet name is null");
        this.sourceSheet = sourceWorkbook.getSheet(sourceSheetName);
    }

    public ExcelColumnMultiSheetSplit(Workbook sourceWorkbook, Sheet sourceSheet) {
        this(sourceWorkbook);
        this.sourceSheet = Valid.notNull(sourceSheet, "split sheet is null");
    }

    private ExcelColumnMultiSheetSplit(Workbook sourceWorkbook) {
        super(new SXSSFWorkbook());
        this.sourceWorkbook = Valid.notNull(sourceWorkbook, "split workbook is null");
        Valid.isTrue(!Excels.isStreamingWorkbook(sourceWorkbook), "unsupported streaming reading, please use ExcelColumnSingleSplit");
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelColumnMultiSheetSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelColumnMultiSheetSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 执行拆分
     *
     * @param columns 列
     * @return this
     */
    public ExcelColumnMultiSheetSplit split(int... columns) {
        return this.split(columns, null, null);
    }

    /**
     * 执行拆分
     *
     * @param columns 列
     * @param headers 表头
     * @return this
     */
    public ExcelColumnMultiSheetSplit split(int[] columns, String[] headers) {
        return this.split(columns, headers, null);
    }

    /**
     * 执行拆分
     *
     * @param columns  列
     * @param password 保护表格的密码
     * @return this
     */
    public ExcelColumnMultiSheetSplit split(int[] columns, String password) {
        return this.split(columns, null, password);
    }

    /**
     * 执行拆分
     *
     * @param columns  列
     * @param headers  表头
     * @param password 保护表格的密码
     * @return this
     */
    public ExcelColumnMultiSheetSplit split(int[] columns, String[] headers, String password) {
        Sheet targetSheet = super.workbook.createSheet(sourceSheet.getSheetName() + (++sheetNum));
        ExcelColumnSplitSupport.split(sourceSheet, super.workbook, targetSheet, columns, headers, skip, false);
        if (!Strings.isBlank(password)) {
            targetSheet.protectSheet(password);
        }
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

    public int getSheetNum() {
        return sheetNum;
    }

}

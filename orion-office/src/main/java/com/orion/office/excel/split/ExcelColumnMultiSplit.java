package com.orion.office.excel.split;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;

/**
 * excel 列拆分器 拆分多个文件一个sheet 不支持复杂类型 占用内存少
 * 不支持流式读取 (流式只能读取一次)
 * 流式请用 {@link ExcelColumnSingleSplit}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnMultiSplit implements SafeCloseable {

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

    public ExcelColumnMultiSplit(Workbook sourceWorkbook, int sourceSheetIndex) {
        this(sourceWorkbook);
        Valid.gte(sourceSheetIndex, 0, "split sheet index must >= 0");
        this.sourceSheet = sourceWorkbook.getSheetAt(sourceSheetIndex);
    }

    public ExcelColumnMultiSplit(Workbook sourceWorkbook, String sourceSheetName) {
        this(sourceWorkbook);
        Valid.notBlank(sourceSheetName, "split sheet name is null");
        this.sourceSheet = sourceWorkbook.getSheet(sourceSheetName);
    }

    public ExcelColumnMultiSplit(Workbook sourceWorkbook, Sheet sourceSheet) {
        this(sourceWorkbook);
        this.sourceSheet = Valid.notNull(sourceSheet, "split sheet is null");
    }

    private ExcelColumnMultiSplit(Workbook sourceWorkbook) {
        this.sourceWorkbook = Valid.notNull(sourceWorkbook, "split workbook is null");
        Valid.isTrue(!Excels.isStreamingWorkbook(sourceWorkbook), "unsupported streaming reading, please use ExcelColumnSingleSplit");
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelColumnMultiSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelColumnMultiSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    public ExcelColumnMultiSplit split(int[] columns, File file) {
        return this.split(columns, null, null, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, File file) {
        return this.split(columns, headers, null, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String password, File file) {
        return this.split(columns, null, password, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, String password, File file) {
        return this.split(columns, headers, password, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String file) {
        return this.split(columns, null, null, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, String file) {
        return this.split(columns, headers, null, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String password, String file) {
        return this.split(columns, null, password, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, String password, String file) {
        return this.split(columns, headers, password, Files1.openOutputStreamSafe(file), true);
    }

    public ExcelColumnMultiSplit split(int[] columns, OutputStream out) {
        return this.split(columns, null, null, out, false);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, OutputStream out) {
        return this.split(columns, headers, null, out, false);
    }

    public ExcelColumnMultiSplit split(int[] columns, String password, OutputStream out) {
        return this.split(columns, null, password, out, false);
    }

    public ExcelColumnMultiSplit split(int[] columns, String[] headers, String password, OutputStream out) {
        return this.split(columns, headers, password, out, false);
    }

    /**
     * 执行拆分
     *
     * @param columns  列
     * @param headers  表头
     * @param password 保护表格的密码
     * @param out      目标路径
     * @param close    是否关闭
     * @return this
     */
    public ExcelColumnMultiSplit split(int[] columns, String[] headers, String password, OutputStream out, boolean close) {
        Valid.notNull(out, "dest stream is null");
        Workbook targetWorkbook = new SXSSFWorkbook();
        Sheet targetSheet = targetWorkbook.createSheet(sourceSheet.getSheetName());
        ExcelColumnSplitSupport.split(sourceSheet, targetWorkbook, targetSheet, columns, headers, skip, false);
        if (!Strings.isBlank(password)) {
            targetSheet.protectSheet(password);
        }
        // 设置默认属性
        Excels.setDefaultProperties(targetWorkbook);
        // 写入
        Excels.write(targetWorkbook, out);
        Streams.close(targetWorkbook);
        if (close) {
            Streams.close(out);
        }
        return this;
    }

    @Override
    public void close() {
        Streams.close(sourceWorkbook);
    }

    public Workbook getSourceWorkbook() {
        return sourceWorkbook;
    }

    public Sheet getSourceSheet() {
        return sourceSheet;
    }

}

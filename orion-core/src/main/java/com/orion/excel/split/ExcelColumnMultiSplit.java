package com.orion.excel.split;

import com.orion.able.SafeCloseable;
import com.orion.excel.Excels;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;

/**
 * Excel 列拆分器 拆分多个文件一个sheet 不支持复杂类型 占用内存少
 * 不支持流式读取 (流式只能读取一次)
 * 流式请用 {@link ExcelColumnSingleSplit}
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnMultiSplit implements SafeCloseable {

    /**
     * source workbook
     */
    private Workbook sourceWorkbook;

    /**
     * source sheet index
     */
    private int sourceSheetIndex;

    /**
     * source sheet name
     */
    private String sourceSheetName;

    /**
     * 跳过行数
     */
    private int skip;

    public ExcelColumnMultiSplit(Workbook sourceWorkbook, int sourceSheetIndex) {
        Valid.notNull(sourceWorkbook, "split workbook is null");
        Valid.gte(sourceSheetIndex, 0, "split sheet index must >= 0");
        this.sourceWorkbook = sourceWorkbook;
        this.sourceSheetIndex = sourceSheetIndex;
        Valid.isFalse(Excels.isStreamingWorkbook(sourceWorkbook), "unsupported streaming reading, please use ExcelColumnSingleSplit");
    }

    public ExcelColumnMultiSplit(Workbook sourceWorkbook, String sourceSheetName) {
        Valid.notNull(sourceWorkbook, "split workbook is null");
        Valid.notBlank(sourceSheetName, "split sheet name is null");
        this.sourceWorkbook = sourceWorkbook;
        this.sourceSheetName = sourceSheetName;
        Valid.isFalse(Excels.isStreamingWorkbook(sourceWorkbook), "unsupported streaming reading, please use ExcelColumnSingleSplit");
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
        Sheet sourceSheet;
        if (!Strings.isBlank(sourceSheetName)) {
            sourceSheet = sourceWorkbook.getSheet(sourceSheetName);
        } else {
            sourceSheet = sourceWorkbook.getSheetAt(sourceSheetIndex);
        }
        Workbook targetWorkbook = new SXSSFWorkbook();
        Sheet targetSheet = targetWorkbook.createSheet(sourceSheet.getSheetName());
        ExcelColumnSplitSupport.split(sourceSheet, targetWorkbook, targetSheet, columns, headers, skip, false);
        if (!Strings.isBlank(password)) {
            targetSheet.protectSheet(password);
        }
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

    public int getSourceSheetIndex() {
        return sourceSheetIndex;
    }

    public String getSourceSheetName() {
        return sourceSheetName;
    }

}

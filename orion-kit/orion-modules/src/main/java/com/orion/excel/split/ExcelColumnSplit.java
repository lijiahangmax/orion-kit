package com.orion.excel.split;

import com.orion.excel.Excels;
import com.orion.utils.Arrays1;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 列拆分器
 * <p>
 * 只是拆分 可以获取行数据 不支持样式 只能拆分一个 速度快 如果不保存数据占用内存少 (最好使用StreamingSheet)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/8 18:36
 */
public class ExcelColumnSplit {

    /**
     * sheet
     */
    public Sheet sheet;

    /**
     * 头部跳过行数
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
     * 是否保存数据
     */
    private boolean saveData;

    /**
     * workbook
     */
    private Workbook workbook;

    /**
     * rows
     */
    private List<List<String>> rows = new ArrayList<>();

    public ExcelColumnSplit(Sheet sheet, int... columns) {
        Valid.notNull(sheet, "split sheet is null");
        Valid.isFalse(Arrays1.isEmpty(columns), "split columns is null");
        this.sheet = sheet;
        this.columns = columns;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelColumnSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelColumnSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 保存数据
     *
     * @return this
     */
    public ExcelColumnSplit saveData() {
        this.saveData = true;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 文件的表头
     * @return ignore
     */
    public ExcelColumnSplit header(String... headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelColumnSplit execute() {
        int index = 0, i = 0;
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        if (headers != null) {
            Row headerRow = sheet.createRow(i++);
            for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
                Cell headerCell = headerRow.createCell(headerIndex);
                headerCell.setCellValue(headers[headerIndex]);
            }
        }
        int cellIndex = 0;
        for (Row row : this.sheet) {
            if (index++ < skip) {
                continue;
            }
            List<String> rowList = new ArrayList<>();
            for (Cell cell : row) {
                rowList.add(Excels.getValue(cell));
            }
            if (saveData) {
                rows.add(rowList);
            }
            Row rowRow = sheet.createRow(i++);
            for (int column : columns) {
                Cell rowCell = rowRow.createCell(cellIndex++);
                if (rowList.size() > column && column >= 0) {
                    rowCell.setCellValue(rowList.get(column));
                } else {
                    rowCell.setCellValue("");
                }
            }
            cellIndex = 0;
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param file file
     * @return this
     */
    public ExcelColumnSplit write(String file) {
        Valid.notNull(file, "write file is null");
        Excels.write(workbook, file);
        write(new File(file));
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param file file
     * @return this
     */
    public ExcelColumnSplit write(File file) {
        Valid.notNull(file, "write file is null");
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 将数据写入到拆分文件流
     *
     * @param out 流
     * @return this
     */
    public ExcelColumnSplit write(OutputStream out) {
        Valid.notNull(out, "write file is null");
        Excels.write(workbook, out);
        return this;
    }

    /**
     * 关闭 workbook
     */
    public void close() {
        Streams.close(workbook);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public List<List<String>> getRows() {
        return rows;
    }

}

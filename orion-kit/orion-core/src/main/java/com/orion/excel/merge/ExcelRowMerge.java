package com.orion.excel.merge;

import com.orion.able.SafeCloseable;
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
 * Excel 列合并器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/9 1:07
 */
public class ExcelRowMerge implements SafeCloseable {

    /**
     * workbook
     */
    private Workbook workbook;

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * 需要合并的sheet
     */
    private List<Sheet> mergeSheets;

    /**
     * 文件头
     */
    private String[] header;

    /**
     * 合并sheet跳过的行
     */
    private int[] skip;

    /**
     * 是否自动关闭 merge sheet
     */
    private boolean autoCloseSheet = true;

    public ExcelRowMerge(List<Sheet> mergeSheets) {
        Valid.notEmpty(mergeSheets, "merge sheets is empty");
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.mergeSheets = mergeSheets;
    }

    /**
     * 设置文件头
     *
     * @param header header
     * @return this
     */
    public ExcelRowMerge header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 合并sheet跳过行
     *
     * @param skip 文件跳过的行
     * @return this
     */
    public ExcelRowMerge skip(int... skip) {
        this.skip = skip;
        return this;
    }

    /**
     * 是否自动关闭sheet
     *
     * @param autoCloseSheet ignore
     * @return this
     */
    public ExcelRowMerge autoCloseSheet(boolean autoCloseSheet) {
        this.autoCloseSheet = autoCloseSheet;
        return this;
    }

    /**
     * 执行合并
     *
     * @return this
     */
    public ExcelRowMerge execute() {
        int i = 0, sheetIndex = 0, sheetRowIndex = 0;
        if (header != null) {
            Row headerRow = sheet.createRow(i++);
            for (int headerIndex = 0; headerIndex < header.length; headerIndex++) {
                Cell headerCell = headerRow.createCell(headerIndex);
                headerCell.setCellValue(header[headerIndex]);
            }
        }
        for (Sheet mergeSheet : mergeSheets) {
            if (mergeSheet == null) {
                continue;
            }
            int skip = getSkip(sheetIndex++);
            for (Row sheetRow : mergeSheet) {
                if (sheetRowIndex++ < skip) {
                    continue;
                }
                List<String> rowList = new ArrayList<>();
                for (Cell cell : sheetRow) {
                    rowList.add(Excels.getValue(cell));
                }
                Row row = sheet.createRow(i++);
                for (int sheetCellIndex = 0; sheetCellIndex < rowList.size(); sheetCellIndex++) {
                    String cellValue = rowList.get(sheetCellIndex);
                    Cell cell = row.createCell(sheetCellIndex);
                    cell.setCellValue(cellValue);
                }
            }
            sheetRowIndex = 0;
            if (autoCloseSheet) {
                try {
                    Streams.close(mergeSheet.getWorkbook());
                } catch (Exception e) {
                    // skip streaming sheet UnsupportedOperationException
                }
            }
        }
        return this;
    }

    /**
     * 获取sheet需要跳过的行
     *
     * @param sheet index
     * @return skip line
     */
    private int getSkip(int sheet) {
        int length = Arrays1.length(skip);
        if (length == 0) {
            return 0;
        }
        if (length > sheet) {
            int i = skip[sheet];
            if (i > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 写入workbook到文件
     *
     * @param file 文件
     * @return this
     */
    public ExcelRowMerge write(String file) {
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入workbook到文件
     *
     * @param file 文件
     * @return this
     */
    public ExcelRowMerge write(File file) {
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入workbook到流
     *
     * @param out 流
     * @return this
     */
    public ExcelRowMerge write(OutputStream out) {
        Excels.write(workbook, out);
        return this;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

}

package com.orion.csv.convert.adapter.impl;

import com.orion.csv.CsvExt;
import com.orion.csv.convert.adapter.CsvConvertAdapter;
import com.orion.csv.importing.CsvStream;
import com.orion.excel.Excels;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * CSV -> Excel 适配器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/18 2:36
 */
public class ExcelAdapter implements CsvConvertAdapter {

    /**
     * workbook
     */
    private Workbook workbook;

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * csvStream
     */
    private CsvStream csvStream;

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
    private int bufferLine = 100;

    public ExcelAdapter(CsvExt csvExt) {
        this(csvExt.stream());
    }

    public ExcelAdapter(CsvStream csvStream) {
        this.csvStream = csvStream;
        this.workbook = new XSSFWorkbook();
        this.sheet = this.workbook.createSheet();
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
     * 跳过CSV一多
     *
     * @return this
     */
    public ExcelAdapter skip() {
        skip += 1;
        return this;
    }

    /**
     * 跳过CSV行多
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
    public void forNew() {
        int i = 0;
        // 表头
        if (header != null) {
            Row headerRow = sheet.createRow(i++);
            for (int hi = 0; hi < header.length; hi++) {
                Cell hc = headerRow.createCell(hi);
                hc.setCellValue(header[hi]);
            }
        }
        csvStream.skipLines(skip);
        // 数据
        List<String[]> lines;
        while (!(lines = csvStream.clean().readLines(bufferLine).lines()).isEmpty()) {
            for (String[] line : lines) {
                Row rr = sheet.createRow(i++);
                for (int ri = 0; ri < line.length; ri++) {
                    Cell rc = rr.createCell(ri);
                    rc.setCellValue(line[ri]);
                }
            }
        }
    }

    /**
     * 写入workbook到文件
     *
     * @param file file
     * @return this
     */
    public ExcelAdapter write(File file) {
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入workbook到文件
     *
     * @param file file
     * @return this
     */
    public ExcelAdapter write(String file) {
        Excels.write(workbook, file);
        return this;
    }

    /**
     * 写入workbook到流
     *
     * @param out out
     * @return this
     */
    public ExcelAdapter write(OutputStream out) {
        Excels.write(workbook, out);
        return this;
    }

    /**
     * 关闭workbook
     */
    public void close() {
        Streams.close(sheet.getWorkbook());
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public CsvStream getCsvStream() {
        return csvStream;
    }

}

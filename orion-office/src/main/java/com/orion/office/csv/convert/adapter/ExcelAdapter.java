package com.orion.office.csv.convert.adapter;

import com.orion.able.Adaptable;
import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.office.csv.CsvExt;
import com.orion.office.csv.reader.CsvArrayReader;
import com.orion.office.excel.writer.BaseExcelWriteable;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * CSV -> Excel 文本适配器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/18 2:36
 */
public class ExcelAdapter extends BaseExcelWriteable implements Adaptable<ExcelAdapter>, SafeCloseable {

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * csvStream
     */
    private CsvArrayReader reader;

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
     * 跳过CSV一行
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
        List<String[]> lines;
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

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public CsvArrayReader getReader() {
        return reader;
    }

}

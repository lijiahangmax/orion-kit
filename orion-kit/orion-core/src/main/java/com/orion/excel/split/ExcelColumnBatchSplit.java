package com.orion.excel.split;

import com.orion.excel.Excels;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 列拆分器
 * <p>
 * 只是拆分 可以获取行数据 不支持样式 可以拆分多个 速度慢 占用内存多 (最好使用StreamingSheet)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/19 18:17
 */
public class ExcelColumnBatchSplit {

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
    private List<String[]> headers;

    /**
     * 列
     */
    private List<int[]> columns;

    /**
     * Workbook 输出流
     */
    private List<OutputStream> dist;

    /**
     * rows
     */
    private List<List<String>> rows = new ArrayList<>();

    public ExcelColumnBatchSplit(Sheet sheet, List<int[]> columns) {
        Valid.notNull(sheet, "split sheet is null");
        Valid.notEmpty(columns, "split columns is null");
        for (int[] column : columns) {
            Valid.isFalse(Arrays1.isEmpty(column), "columns is null");
        }
        this.sheet = sheet;
        this.columns = columns;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelColumnBatchSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelColumnBatchSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 每个文件的表头
     * @return ignore
     */
    public ExcelColumnBatchSplit header(List<String[]> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 文件的表头
     * @return ignore
     */
    public ExcelColumnBatchSplit header(String... headers) {
        if (this.headers == null) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(headers);
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelColumnBatchSplit execute() {
        Valid.notEmpty(dist, "dist file is empty");
        int index = 0;
        for (Row row : this.sheet) {
            if (index++ < skip) {
                continue;
            }
            List<String> rowList = new ArrayList<>();
            for (Cell cell : row) {
                rowList.add(Excels.getCellValue(cell));
            }
            rows.add(rowList);
        }
        for (int i = 0; i < columns.size(); i++) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            int[] fileColumns = columns.get(i);
            boolean empty = Arrays1.isEmpty(fileColumns);
            if (empty) {
                continue;
            }
            if (dist.size() <= i) {
                return this;
            }
            OutputStream out = dist.get(i);
            XSSFSheet sheet = workbook.createSheet();
            int fileRowIndex = 0;
            if (headers != null && headers.size() > i) {
                String[] header = headers.get(i);
                if (header != null) {
                    XSSFRow row = sheet.createRow(fileRowIndex++);
                    for (int hci = 0; hci < header.length; hci++) {
                        XSSFCell cell = row.createCell(hci);
                        cell.setCellValue(header[hci]);
                    }
                }
            }
            int rowCellIndex = 0;
            for (List<String> rowData : rows) {
                XSSFRow row = sheet.createRow(fileRowIndex++);
                for (int fileColumn : fileColumns) {
                    XSSFCell cell = row.createCell(rowCellIndex++);
                    if (rowData.size() > fileColumn && fileColumn >= 0) {
                        cell.setCellValue(rowData.get(fileColumn));
                    } else {
                        cell.setCellValue(Strings.EMPTY);
                    }
                }
                rowCellIndex = 0;
            }
            this.safeWrite(workbook, out);
        }
        return this;
    }

    /**
     * 设置拆分文件输出流
     *
     * @param dist dist
     * @return this
     */
    public ExcelColumnBatchSplit dist(OutputStream... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        this.dist = Lists.of(dist);
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param dist dist
     * @return this
     */
    public ExcelColumnBatchSplit dist(File... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        List<OutputStream> out = new ArrayList<>();
        for (File file : dist) {
            Files1.touch(file);
            try {
                out.add(Files1.openOutputStreamSafe(file));
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        this.dist = out;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param dist dist
     * @return this
     */
    public ExcelColumnBatchSplit dist(String... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        Valid.eq(dist.length, columns.size(), "dist length must eq columns size");
        List<OutputStream> out = new ArrayList<>();
        for (String file : dist) {
            Files1.touch(file);
            out.add(Files1.openOutputStreamSafe(file));
        }
        this.dist = out;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @return this
     */
    public ExcelColumnBatchSplit distPath(String pathDir, String baseName) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        List<OutputStream> out = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String path = Files1.getPath(pathDir + "/" + baseName + "_column_split_" + (i + 1) + ".xlsx");
            Files1.touch(path);
            out.add(Files1.openOutputStreamSafe(path));
        }
        this.dist = out;
        return this;
    }

    /**
     * 关闭流
     */
    public void close() {
        for (OutputStream out : dist) {
            Streams.close(out);
        }
    }

    /**
     * 写入workbook到流
     *
     * @param workbook workbook
     * @param out      流
     */
    private void safeWrite(Workbook workbook, OutputStream out) {
        try {
            workbook.write(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        Streams.close(workbook);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public List<List<String>> getRows() {
        return rows;
    }

}

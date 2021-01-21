package com.orion.excel.split;

import com.orion.able.SafeCloseable;
import com.orion.excel.Excels;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Excel 行拆分器 能拆分多个文件一个sheet 不支持复杂类型
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/21 16:01
 */
public class ExcelRowSplit implements SafeCloseable {

    /**
     * sheet
     */
    public Sheet sheet;

    /**
     * 拆分文件最大行数
     */
    private int limit;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 是否是流式读取 (样式)
     */
    private boolean streaming;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 拆分输出对流
     */
    private List<OutputStream> dest;

    /**
     * 自动生成的文件目录
     */
    private String generatorPathDir;

    /**
     * 自动生成的文件名称
     */
    private String generatorBaseName;

    /**
     * 自动生成的文件名称后缀
     */
    private String generatorNameSuffix;

    /**
     * 列
     */
    private int[] column;

    /**
     * 列数
     */
    private int columnSize = 32;

    private String password;

    private Workbook currentWorkbook;

    private Sheet currentSheet;

    private int currentIndex;

    private int currentWorkbookIndex;

    private boolean end;

    public ExcelRowSplit(Sheet sheet, int limit) {
        Valid.notNull(sheet, "split sheet is null");
        Valid.lte(0, limit, "limit not be lte 0");
        this.sheet = sheet;
        this.limit = limit;
        this.streaming = Excels.isStreamingSheet(sheet);
    }

    /**
     * 设置拆分文件输出流
     *
     * @param dist dist
     * @return this
     */
    public ExcelRowSplit dest(OutputStream... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        this.dest = Lists.of(dist);
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件
     *
     * @param dist dist
     * @return this
     */
    public ExcelRowSplit dest(File... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        OutputStream[] streams = Arrays.stream(dist)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.dest(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param dist dist
     * @return this
     */
    public ExcelRowSplit dest(String... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        OutputStream[] streams = Arrays.stream(dist)
                .map(Files1::openOutputStreamSafe)
                .toArray(OutputStream[]::new);
        return this.dest(streams);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称
     * @return this
     */
    public ExcelRowSplit destPath(String pathDir, String baseName) {
        return this.destPath(pathDir, baseName, null);
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir    目标文件目录
     * @param baseName   文件名称
     * @param nameSuffix 文件名称后缀
     * @return this
     */
    public ExcelRowSplit destPath(String pathDir, String baseName, String nameSuffix) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        this.dest = null;
        this.generatorPathDir = pathDir;
        this.generatorBaseName = baseName;
        this.generatorNameSuffix = Strings.def(nameSuffix);
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelRowSplit protect(String password) {
        this.password = password;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelRowSplit skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelRowSplit skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置列
     *
     * @param column 列
     * @return this
     */
    public ExcelRowSplit column(int... column) {
        if (!Arrays1.isEmpty(column)) {
            this.column = column;
            this.columnSize = Arrays1.max(column) + 1;
        }
        return this;
    }

    /**
     * 设置列数 用于设置列宽
     *
     * @param columnSize 列数
     * @return this
     */
    public ExcelRowSplit columnSize(int columnSize) {
        if (Arrays1.isEmpty(column)) {
            this.columnSize = columnSize;
        }
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public ExcelRowSplit header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelRowSplit split() {
        Iterator<Row> iterator = sheet.iterator();
        for (int j = 0; j < skip; j++) {
            if (iterator.hasNext()) {
                // skip
                iterator.next();
            } else {
                end = true;
            }
        }
        this.initNext();
        while (iterator.hasNext()) {
            this.addRow(currentIndex++, iterator.next());
            if (currentIndex == limit && !end) {
                this.initNext();
            }
            if (end) {
                break;
            }
        }
        end = true;
        if (currentIndex == 0 || (currentIndex == 1 && !Arrays1.isEmpty(header))) {
            Streams.close(currentWorkbook);
        } else {
            this.initNext();
        }
        return this;
    }

    /**
     * 设置列
     *
     * @param rowIndex index
     * @param row      row
     */
    private void addRow(int rowIndex, Row row) {
        Row target = currentSheet.createRow(rowIndex);
        if (!streaming) {
            target.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
        }
        int i = 0;
        if (Arrays1.isEmpty(column)) {
            for (Cell cell : row) {
                this.setCellValue(cell, target.createCell(i++));
            }
        } else {
            for (int col : column) {
                this.setCellValue(row.getCell(col), target.createCell(i++));
            }
        }
    }

    /**
     * 设置单元格的值
     *
     * @param sourceCell source
     * @param targetCell target
     */
    private void setCellValue(Cell sourceCell, Cell targetCell) {
        if (!streaming) {
            CellStyle targetStyle = currentWorkbook.createCellStyle();
            targetStyle.cloneStyleFrom(sourceCell.getCellStyle());
            targetCell.setCellStyle(targetStyle);
        }
        Excels.copyCellValue(sourceCell, targetCell);
    }

    /**
     * 设置下一个workbook
     */
    private void initNext() {
        if (currentWorkbook != null) {
            OutputStream out;
            if (dest == null) {
                out = generatorOutputStream(currentWorkbookIndex++);
            } else {
                int size = dest.size();
                if (size > currentWorkbookIndex) {
                    out = dest.get(currentWorkbookIndex++);
                    if (size == currentWorkbookIndex) {
                        end = true;
                        currentIndex = 0;
                    }
                } else {
                    end = true;
                    return;
                }
            }
            if (password != null && !streaming) {
                currentSheet.protectSheet(password);
            }
            this.write(currentWorkbook, out);
            Streams.close(currentWorkbook);
        }
        if (!end) {
            currentWorkbook = new SXSSFWorkbook();
            currentSheet = currentWorkbook.createSheet(sheet.getSheetName());
            if (!streaming) {
                for (int i = 0; i < columnSize; i++) {
                    currentSheet.setColumnWidth(i, sheet.getColumnWidth(i));
                    currentSheet.setDefaultColumnStyle(i, sheet.getColumnStyle(i));
                }
                currentSheet.setDefaultColumnWidth(sheet.getDefaultColumnWidth());
                currentSheet.setDefaultRowHeightInPoints(sheet.getDefaultRowHeightInPoints());
            }
            if (!Arrays1.isEmpty(header)) {
                currentIndex = 1;
                Row headerRow = currentSheet.createRow(0);
                for (int headerIndex = 0; headerIndex < header.length; headerIndex++) {
                    Cell headerRowCell = headerRow.createCell(headerIndex);
                    headerRowCell.setCellValue(header[headerIndex]);
                }
            } else {
                currentIndex = 0;
            }
        }
    }

    @Override
    public void close() {
        if (dest != null) {
            for (OutputStream outputStream : dest) {
                Streams.close(outputStream);
            }
        }
    }

    /**
     * 写入 Workbook 到 流
     *
     * @param wb  wb
     * @param out out
     */
    private void write(Workbook wb, OutputStream out) {
        try {
            wb.write(out);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 生成OutputStream
     *
     * @param i index
     * @return ignore
     */
    private OutputStream generatorOutputStream(int i) {
        String path = Files1.getPath(generatorPathDir + "/" + generatorBaseName + generatorNameSuffix + (i + 1) + ".xlsx");
        Files1.touch(path);
        return Files1.openOutputStreamSafe(path);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getLimit() {
        return limit;
    }

}

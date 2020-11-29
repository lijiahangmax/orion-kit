package com.orion.excel.split;

import com.orion.excel.Excels;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
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
 * Excel 行拆分器
 * <p>
 * 只是拆分 不可获取源文件数据 不支持样式 速度快 占用内存少 (最好使用StreamingSheet)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/21 16:01
 */
public class ExcelRowSplitStreaming {

    /**
     * sheet
     */
    public Sheet sheet;

    /**
     * 拆分文件最大行数
     */
    private int rowSize;

    /**
     * 最大行数
     */
    private int maxCount;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    /**
     * 拆分输出对流
     */
    private List<OutputStream> dist;

    /**
     * 自动生成的文件目录
     */
    private String generatorPathDir;

    /**
     * 自动生成的文件名称
     */
    private String generatorBaseName;

    public ExcelRowSplitStreaming(Sheet sheet, int rowSize) {
        Valid.notNull(sheet, "split sheet is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.sheet = sheet;
        this.rowSize = rowSize;
    }

    /**
     * 设置拆分文件输出流
     *
     * @param dist dist
     * @return this
     */
    public ExcelRowSplitStreaming dist(OutputStream... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        this.dist = Lists.of(dist);
        this.maxCount = rowSize * dist.length;
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
    public ExcelRowSplitStreaming dist(File... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        List<OutputStream> out = new ArrayList<>();
        for (File file : dist) {
            Files1.touch(file);
            out.add(Files1.openOutputStreamSafe(file));
        }
        this.dist = out;
        this.maxCount = rowSize * dist.length;
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param dist dist
     * @return this
     */
    public ExcelRowSplitStreaming dist(String... dist) {
        Valid.notEmpty(dist, "dist file is empty");
        List<OutputStream> out = new ArrayList<>();
        for (String file : dist) {
            Files1.touch(file);
            out.add(Files1.openOutputStreamSafe(file));
        }
        this.dist = out;
        this.maxCount = rowSize * dist.length;
        this.generatorPathDir = null;
        this.generatorBaseName = null;
        return this;
    }

    /**
     * 设置拆分文件输出文件路径
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @return this
     */
    public ExcelRowSplitStreaming distPath(String pathDir, String baseName) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        this.dist = null;
        this.generatorPathDir = pathDir;
        this.generatorBaseName = baseName;
        this.maxCount = Integer.MAX_VALUE;
        return this;
    }

    /**
     * 跳过头部一行
     *
     * @return this
     */
    public ExcelRowSplitStreaming skip() {
        this.skip += 1;
        return this;
    }

    /**
     * 跳过头部多行
     *
     * @param skip 行数
     * @return this
     */
    public ExcelRowSplitStreaming skip(int skip) {
        this.skip += skip;
        return this;
    }

    /**
     * 设置表头
     *
     * @param header 表头
     * @return ignore
     */
    public ExcelRowSplitStreaming header(String... header) {
        this.header = header;
        return this;
    }

    /**
     * 执行拆分
     *
     * @return this
     */
    public ExcelRowSplitStreaming execute() {
        if (generatorPathDir == null) {
            Valid.notNull(dist, "split file dist is null");
        }
        int i = 0, index = 0, blockIndex = 0, rowIndex = 0, blockRowIndex = 0;
        OutputStream out;
        if (generatorPathDir != null) {
            out = generatorOutputStream(0);
        } else {
            out = dist.get(0);
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        if (this.header != null) {
            setHeader(sheet);
            blockRowIndex++;
        }
        for (Row row : this.sheet) {
            if (i++ < skip) {
                continue;
            }
            if (index++ >= maxCount) {
                break;
            }
            List<String> rowList = new ArrayList<>();
            for (Cell cell : row) {
                rowList.add(Excels.getValue(cell));
            }
            if (rowIndex++ >= rowSize) {
                rowIndex = 1;
                blockRowIndex = 0;
                this.write(wb, out);
                if (generatorPathDir != null) {
                    Streams.close(out);
                    out = generatorOutputStream(++blockIndex);
                } else {
                    out = dist.get(++blockIndex);
                }
                Streams.close(wb);
                wb = new XSSFWorkbook();
                sheet = wb.createSheet();
                if (this.header != null) {
                    setHeader(sheet);
                    blockRowIndex++;
                }
            }
            int createCellIndex = 0;
            Row createRow = sheet.createRow(blockRowIndex++);
            for (String s : rowList) {
                Cell createCell = createRow.createCell(createCellIndex++);
                createCell.setCellValue(s);
            }
        }
        this.write(wb, out);
        Streams.close(wb);
        return this;
    }

    /**
     * 关闭流
     */
    public void close() {
        if (dist != null) {
            for (OutputStream outputStream : dist) {
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
        String path = Files1.getPath(generatorPathDir + "/" + generatorBaseName + "_row_split_" + (i + 1) + ".xlsx");
        Files1.touch(path);
        return Files1.openOutputStreamSafe(path);
    }

    /**
     * 设置头
     *
     * @param sheet sheet
     */
    private void setHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int headerRowIndex = 0; headerRowIndex < header.length; headerRowIndex++) {
            Cell headerRowCell = headerRow.createCell(headerRowIndex);
            headerRowCell.setCellValue(header[headerRowIndex]);
        }
    }

    public Sheet getSheet() {
        return sheet;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getMaxCount() {
        return maxCount;
    }

}

package com.orion.excel.split;

import com.orion.excel.Excels;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
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
 * Excel 行拆分器
 * <p>
 * 只是拆分 可以获取行数据 不支持样式 速度慢 占用内存多 (最好使用StreamingSheet)
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/19 18:17
 */
public class ExcelRowSplit {

    /**
     * sheet
     */
    public Sheet sheet;

    /**
     * 每个文件的数据行数
     */
    private int rowSize;

    /**
     * 头部跳过行数
     */
    private int skip;

    /**
     * 表头
     */
    private String[] header;

    /**
     * Workbook
     */
    private List<Workbook> workbooks = new ArrayList<>();

    /**
     * rows
     */
    private List<List<String>> rows = new ArrayList<>();

    public ExcelRowSplit(Sheet sheet, int rowSize) {
        Valid.notNull(sheet, "split sheet is null");
        Valid.lte(0, rowSize, "row size not be lte 0");
        this.sheet = sheet;
        this.rowSize = rowSize;
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
    public ExcelRowSplit execute() {
        int i = 0;
        for (Row row : sheet) {
            if (i++ < skip) {
                continue;
            }
            List<String> rowList = new ArrayList<>();
            for (Cell cell : row) {
                rowList.add(Excels.getCellValue(cell));
            }
            rows.add(rowList);
        }
        int size = rows.size(), loop = size / rowSize, mod = size % rowSize;
        if (mod == 0) {
            loop++;
        }
        for (int j = 0; j < loop; j++) {
            int start = j * rowSize;
            int end = start + rowSize;
            if (j == loop - 1) {
                // last
                end = size;
            }
            boolean addHeader = header != null;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            for (int is = 0; start < end; start++, is++) {
                if (addHeader) {
                    XSSFRow row = sheet.createRow(is);
                    for (int m = 0; m < header.length; m++) {
                        if (header[m] != null) {
                            XSSFCell cell = row.createCell(m);
                            cell.setCellValue(Strings.def(header[m]));
                        }
                    }
                    start--;
                    addHeader = false;
                    continue;
                }
                XSSFRow row = sheet.createRow(is);
                List<String> rowData = rows.get(start);
                for (int m = 0; m < rowData.size(); m++) {
                    XSSFCell cell = row.createCell(m);
                    cell.setCellValue(rowData.get(m));
                }
            }
            workbooks.add(workbook);
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param files files
     * @return this
     */
    public ExcelRowSplit write(String... files) {
        Valid.notEmpty(files, "write file is empty");
        for (int i = 0; i < workbooks.size(); i++) {
            if (i < files.length) {
                write(new File(files[i]), i);
            }
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件
     *
     * @param files files
     * @return this
     */
    public ExcelRowSplit write(File... files) {
        Valid.notEmpty(files, "write file is empty");
        for (int i = 0; i < workbooks.size(); i++) {
            if (i < files.length) {
                write(files[i], i);
            }
        }
        return this;
    }

    /**
     * 将数据写入到拆分文件流
     *
     * @param outs 流
     * @return this
     */
    public ExcelRowSplit write(OutputStream... outs) {
        Valid.notEmpty(outs, "write stream is empty");
        for (int i = 0; i < workbooks.size(); i++) {
            if (i < outs.length) {
                Workbook workbook = workbooks.get(i);
                OutputStream out = outs[i];
                try {
                    workbook.write(out);
                } catch (Exception e) {
                    throw Exceptions.ioRuntime(e);
                }
            }
        }
        return this;
    }

    /**
     * 写入数据到文件
     *
     * @param pathDir  目标文件目录
     * @param baseName 文件名称 不包含后缀
     * @return this
     */
    public ExcelRowSplit writeToPath(String pathDir, String baseName) {
        Valid.notNull(pathDir, "dist path dir is null");
        Valid.notNull(baseName, "dist file base name is null");
        for (int i = 0; i < workbooks.size(); i++) {
            String path = Files1.getPath(pathDir + "/" + baseName + "_row_split_" + (i + 1) + ".xlsx");
            write(new File(path), i);
        }
        return this;
    }

    /**
     * 写入数据到文件
     *
     * @param file          文件
     * @param workbookIndex workbook索引
     */
    private void write(File file, int workbookIndex) {
        Excels.write(workbooks.get(workbookIndex), file);
    }

    /**
     * 关闭 workbooks
     */
    public void close() {
        for (Workbook workbook : workbooks) {
            Streams.close(workbook);
        }
    }

    /**
     * 获取表格数据
     *
     * @return 数据
     */
    public List<List<String>> getRows() {
        return rows;
    }

    /**
     * 获取sheet
     *
     * @return sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * 获取 workbooks
     *
     * @return workbooks
     */
    public List<Workbook> getWorkbooks() {
        return workbooks;
    }

}

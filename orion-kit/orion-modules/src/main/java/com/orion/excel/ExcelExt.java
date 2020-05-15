package com.orion.excel;

import com.orion.dom.DomExt;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.file.Files1;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Excel 提取器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/6 12:51
 */
public class ExcelExt {

    private Workbook workbook;

    public ExcelExt(File file) throws IOException {
        this(Files1.openInputStream(file));
    }

    public ExcelExt(String file) throws IOException {
        this(DomExt.class.getClassLoader().getResourceAsStream(file));
    }

    public ExcelExt(InputStream in) throws IOException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 4 * 1024);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            this.workbook = new HSSFWorkbook(in);
        } else if (POIXMLDocument.hasOOXMLHeader(in)) {
            try {
                this.workbook = new XSSFWorkbook(OPCPackage.open(in));
            } catch (Exception e) {
                throw Exceptions.parse("parse excel error: " + e.getMessage());
            }
        }
        Valid.notNull(workbook, "workbook is null");
    }

    public ExcelExt(Workbook workbook) {
        Valid.notNull(workbook, "workbook is null");
        this.workbook = workbook;
    }

    /**
     * 获取第一个表格的行流
     *
     * @return 流
     */
    public ExcelStream stream() {
        return new ExcelStream(workbook.getSheetAt(0));
    }

    /**
     * 获取第一个表格的行流
     *
     * @param columns 读取的列
     * @return 流
     */
    public ExcelStream stream(int[] columns) {
        return new ExcelStream(workbook.getSheetAt(0), columns);
    }

    /**
     * 获取表格的行流
     *
     * @param sheetIndex 表格索引
     * @return 流
     */
    public ExcelStream stream(int sheetIndex) {
        return new ExcelStream(workbook.getSheetAt(sheetIndex));
    }

    /**
     * 获取表格的行流
     *
     * @param sheetIndex 表格索引
     * @param columns    读取的列
     * @return 流
     */
    public ExcelStream stream(int sheetIndex, int[] columns) {
        return new ExcelStream(workbook.getSheetAt(sheetIndex), columns);
    }

}

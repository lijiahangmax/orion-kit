package com.orion.office.excel.writer;

import com.orion.able.SafeCloseable;
import com.orion.office.excel.Excels;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.OutputStream;

/**
 * Excel Writer 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/19 18:14
 */
public class BaseExcelWriteable implements SafeCloseable {

    protected Workbook workbook;

    public BaseExcelWriteable(Workbook workbook) {
        Valid.notNull(workbook, "workbook is null");
        this.workbook = workbook;
    }

    public BaseExcelWriteable write(String file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    public BaseExcelWriteable write(File file) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file);
        return this;
    }

    public BaseExcelWriteable write(OutputStream out) {
        Valid.notNull(out, "stream is null");
        Excels.write(workbook, out);
        return this;
    }

    public BaseExcelWriteable write(String file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    public BaseExcelWriteable write(File file, String password) {
        Valid.notNull(file, "file is null");
        Excels.write(workbook, file, password);
        return this;
    }

    public BaseExcelWriteable write(OutputStream out, String password) {
        Valid.notNull(out, "stream is null");
        Excels.write(workbook, out, password);
        return this;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

}

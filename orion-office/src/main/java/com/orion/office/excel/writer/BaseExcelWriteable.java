package com.orion.office.excel.writer;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.OutputStream;

/**
 * excel writer 基类
 *
 * @author Jiahang Li
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
        return this.write(Files1.openOutputStreamSafe(file), null, true);
    }

    public BaseExcelWriteable write(File file) {
        Valid.notNull(file, "file is null");
        return this.write(Files1.openOutputStreamSafe(file), null, true);
    }

    public BaseExcelWriteable write(OutputStream out) {
        Valid.notNull(out, "stream is null");
        return this.write(out, null, true);
    }

    public BaseExcelWriteable write(String file, String password) {
        Valid.notNull(file, "file is null");
        return this.write(Files1.openOutputStreamSafe(file), password, true);
    }

    public BaseExcelWriteable write(File file, String password) {
        Valid.notNull(file, "file is null");
        return this.write(Files1.openOutputStreamSafe(file), password, true);
    }

    public BaseExcelWriteable write(OutputStream out, String password) {
        Valid.notNull(out, "stream is null");
        return this.write(out, password, false);
    }

    protected BaseExcelWriteable write(OutputStream out, String password, boolean close) {
        // 设置默认属性
        Excels.setDefaultProperties(workbook);
        // 写入
        Excels.write(workbook, out, password, close);
        return this;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    @Override
    public void close() {
        Streams.close(workbook);
    }

}

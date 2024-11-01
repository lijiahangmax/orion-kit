/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.office.excel.writer;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.office.excel.Excels;
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
        return this.write(out, null, false);
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

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
package com.orion.office.excel.split;

import com.orion.office.excel.Excels;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/19 10:24
 */
public class RowSplitTests {

    private final Workbook w1 = Excels.openWorkbook("C:\\Users\\lijiahang\\Desktop\\2.xlsx");
    private final Workbook w2 = Excels.openStreamingWorkbook("C:\\Users\\lijiahang\\Desktop\\2.xlsx");
    private final Sheet sheet1 = w1.getSheetAt(2);
    private final Sheet sheet2 = w2.getSheetAt(2);

    @Test
    public void rowSplit1() {
        ExcelRowSplit s = new ExcelRowSplit(sheet1, 5);
        s.targetPath("C:\\Users\\lijiahang\\Desktop\\split1", "sp");
        s.header("1", "3", "4")
                .columns(1, 3, 4)
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit2() {
        ExcelRowSplit s = new ExcelRowSplit(sheet2, 5);
        s.targetPath("C:\\Users\\lijiahang\\Desktop\\split2", "sp");
        s.header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

    @Test
    public void rowSplit3() {
        ExcelRowSplit s = new ExcelRowSplit(sheet2, 5);
        s.target("C:\\Users\\lijiahang\\Desktop\\split3\\1.xlsx");
        s.header("1", "2", "3")
                .skip(1)
                .split()
                .close();
    }

}

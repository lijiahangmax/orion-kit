/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.office.excel.merge;

import cn.orionsec.kit.office.excel.Excels;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/18 11:34
 */
public class MergeTests {

    private Workbook w = Excels.openWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet s1 = w.getSheetAt(0);
    private Sheet s2 = w.getSheetAt(1);
    private Sheet s3 = w.getSheetAt(2);

    private Workbook w1 = Excels.openStreamingWorkbook("C:\\Users\\ljh15\\Desktop\\2.xlsx");
    private Sheet s11 = w1.getSheetAt(0);
    private Sheet s12 = w1.getSheetAt(1);
    private Sheet s13 = w1.getSheetAt(2);

    @Test
    public void mergeTest1() {
        new ExcelMerge(w, s2)
                .skip()
                .skipRows(3)
                .merge(s1)
                .skip()
                .skipRows(2)
                .merge(s3)
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void mergeTest2() {
        new ExcelMerge()
                .merge(s1)
                .skip()
                .merge(s2)
                .skip()
                .merge(s3)
                .write("C:\\Users\\ljh15\\Desktop\\3.xlsx")
                .close();
    }

    @Test
    public void mergeTest3() {
        new ExcelMerge()
                .merge(s11)
                .skip()
                .merge(s12)
                .skip()
                .merge(s13)
                .write("C:\\Users\\ljh15\\Desktop\\4.xlsx")
                .close();
    }

}

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
package cn.orionsec.kit.office.excel;

import cn.orionsec.kit.lang.define.Console;
import cn.orionsec.kit.lang.utils.Systems;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/15 18:28
 */
public class ExcelExtTests {

    /**
     * 文件不存在则跳过测试
     */
    private static void assumeExists(String... files) {
        for (String file : files) {
            Assume.assumeTrue(new File(file).exists());
        }
    }

    @Test
    public void openHss() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xls",
                Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xls");
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xls", null));
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xls", "123"));
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xls", "1234"));
    }

    @Test
    public void openXss() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx",
                Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx");
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx", null));
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx", "123"));
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx", "1234"));
    }

    @Test
    public void openStream() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx",
                Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx");
        System.out.println(Excels.openStreamingWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx", null));
        System.out.println(Excels.openStreamingWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx", "123"));
        System.out.println(Excels.openStreamingWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx", "1234"));
    }

    @Test
    public void open() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\1.xlsx",
                Systems.HOME_DIR + "\\orion-kit-test\\1.xls");
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\1.xlsx", null).getClass());
        System.out.println(Excels.openWorkbook(Systems.HOME_DIR + "\\orion-kit-test\\1.xls", null).getClass());
    }

    @Test
    public void openExt() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx",
                Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx");
        System.out.println(new ExcelExt(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx"));
        System.out.println(new ExcelExt(Systems.HOME_DIR + "\\orion-kit-test\\data\\user_password.xlsx"));
    }

    @Test
    public void openExtReader() {
        assumeExists(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx");
        ExcelExt x = new ExcelExt(Systems.HOME_DIR + "\\orion-kit-test\\data\\user.xlsx", true);
        x.arrayReader(0, Console::trace).read().close();
    }

    @Test
    public void writePassword() {
        String s = Systems.HOME_DIR + "\\orion-kit-test\\data\\1.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Excels.write(workbook, s, "123");
    }

}

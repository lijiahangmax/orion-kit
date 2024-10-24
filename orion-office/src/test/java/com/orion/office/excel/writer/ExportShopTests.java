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
package com.orion.office.excel.writer;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.codec.Base64s;
import com.orion.lang.utils.identity.CreditCodes;
import com.orion.lang.utils.io.FileReaders;
import com.orion.lang.utils.random.Randoms;
import com.orion.lang.utils.time.Dates;
import com.orion.office.excel.Excels;
import com.orion.office.excel.writer.exporting.ExcelExport;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/30 9:44
 */
public class ExportShopTests {

    @Test
    public void export1() {
        List<ExportShop> shopList = IntStream.rangeClosed(1, 15).mapToObj(i -> {
            ExportShop shop = new ExportShop();
            shop.setShopId(10000L + i);
            shop.setShopName("          " + Strings.randomChars(5) + "的店铺        ");
            shop.setCreateDate(Dates.date(System.currentTimeMillis() + Randoms.randomLong(10000000)));
            shop.setBusinessCode(CreditCodes.random());
            File picFile = new File("C:\\Users\\Administrator\\Desktop\\data\\pic\\" + i + ".jpg");
            if (picFile.exists()) {
                shop.setBusinessPicture(Base64s.imgEncode(FileReaders.readAllBytesFast(picFile)));
            }
            shop.setBusinessFile("C:/Users/Administrator/Desktop/export/index.html");
            shop.setMargin(BigDecimal.valueOf(Randoms.randomDouble(0, 10)));
            if (i % 10 == 0) {
                return null;
            }
            return shop;
        }).collect(Collectors.toList());

        new ExcelExport<>(ExportShop.class)
                .init()
                .skipNullRows(false)
                .addRows(shopList)
                .write("C:\\Users\\Administrator\\Desktop\\shop.xlsx")
                .close();
    }

    @Test
    public void export2() {
        List<ExportShop> shopList = IntStream.rangeClosed(1, 15).mapToObj(i -> {
            ExportShop shop = new ExportShop();
            shop.setShopId(10000L + i);
            shop.setShopName(Strings.randomChars(5));
            shop.setCreateDate(Dates.date(System.currentTimeMillis() + Randoms.randomLong(10000000)));
            shop.setBusinessCode(CreditCodes.random());
            shop.setBusinessFile("C:/Users/Administrator/Desktop/export/index.html");
            shop.setMargin(BigDecimal.valueOf(Randoms.randomDouble(0, 10)));
            return shop;
        }).collect(Collectors.toList());

        ExcelExport<ExportShop> exporter = new ExcelExport<>(ExportShop.class)
                .init();
        // 获取列样式的克隆对象
        XSSFCellStyle redStyle = (XSSFCellStyle) exporter.cloneCellStyle(0);
        redStyle.setFillPattern(FillPatternType.forInt(FillPatternType.SOLID_FOREGROUND.getCode()));
        redStyle.setFillForegroundColor(Excels.getColor("#a60300"));
        XSSFCellStyle blueStyle = (XSSFCellStyle) exporter.cloneCellStyle(0);
        blueStyle.setFillPattern(FillPatternType.forInt(FillPatternType.SOLID_FOREGROUND.getCode()));
        blueStyle.setFillForegroundColor(Excels.getColor("#0b00a6"));
        // 设置样式选择器
        exporter.getSheetConfig().setColumnStyle(0, s -> {
            if (s.getShopId() % 3 == 0) {
                return redStyle;
            } else if (s.getShopId() % 4 == 0) {
                return blueStyle;
            }
            return null;
        });

        exporter.addRows(shopList)
                .write("C:\\Users\\Administrator\\Desktop\\shop-1.xlsx")
                .close();
    }

}

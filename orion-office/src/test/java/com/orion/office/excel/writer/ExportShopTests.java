package com.orion.office.excel.writer;

import com.orion.office.excel.writer.exporting.ExcelExport;
import com.orion.utils.Strings;
import com.orion.utils.codec.Base64s;
import com.orion.utils.identity.CreditCodes;
import com.orion.utils.io.FileReaders;
import com.orion.utils.random.Randoms;
import com.orion.utils.time.Dates;

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

    public static void main(String[] args) {
        List<ExportShop> shopList = IntStream.rangeClosed(1, 15).mapToObj(i -> {
            ExportShop shop = new ExportShop();
            shop.setShopId(10000L + i);
            shop.setShopName("          " + Strings.randomChars(5) + "的店铺        ");
            shop.setCreateDate(Dates.date(System.currentTimeMillis() + Randoms.randomLong(10000000)));
            shop.setBusinessCode(CreditCodes.random());
            File picFile = new File("C:\\Users\\ljh15\\Desktop\\data\\pic\\" + i + ".jpg");
            if (picFile.exists()) {
                shop.setBusinessPicture(Base64s.img64Encode(FileReaders.readFast(picFile)));
            }
            shop.setBusinessFile("C:/Users/ljh15/Desktop/export/index.html");
            shop.setMargin(BigDecimal.valueOf(Randoms.randomDouble(0, 10)));
            return shop;
        }).collect(Collectors.toList());

        new ExcelExport<>(ExportShop.class)
                .init()
                .addRows(shopList)
                .write("C:\\Users\\ljh15\\Desktop\\data\\shop.xlsx")
                .close();
    }

}

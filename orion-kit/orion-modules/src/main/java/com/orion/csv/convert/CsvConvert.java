package com.orion.csv.convert;

import com.orion.csv.CsvExt;
import com.orion.csv.convert.adapter.ExcelAdapter;
import com.orion.csv.importing.CsvStream;

/**
 * CSV 转换器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/18 2:39
 */
public class CsvConvert {

    /**
     * Excel 适配器
     *
     * @param csvExt csvExt
     * @return adapter
     */
    public static ExcelAdapter excelAdapter(CsvExt csvExt) {
        return new ExcelAdapter(csvExt);
    }

    /**
     * Excel 适配器
     *
     * @param csvStream csvStream
     * @return adapter
     */
    public static ExcelAdapter excelAdapter(CsvStream csvStream) {
        return new ExcelAdapter(csvStream);
    }

}

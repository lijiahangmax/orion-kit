package com.orion.office.csv.convert;

import com.orion.office.csv.CsvExt;
import com.orion.office.csv.convert.adapter.ExcelAdapter;
import com.orion.office.csv.reader.CsvArrayReader;

/**
 * csv 转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/18 2:39
 */
public class CsvConvert {

    private CsvConvert() {
    }

    /**
     * excel 适配器
     *
     * @param csvExt csvExt
     * @return adapter
     */
    public static ExcelAdapter excelAdapter(CsvExt csvExt) {
        return new ExcelAdapter(csvExt);
    }

    /**
     * excel 适配器
     *
     * @param reader reader
     * @return adapter
     */
    public static ExcelAdapter excelAdapter(CsvArrayReader reader) {
        return new ExcelAdapter(reader);
    }

}

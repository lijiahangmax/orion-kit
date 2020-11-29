package com.orion.excel.convert;

import com.orion.csv.exporting.CsvExport;
import com.orion.excel.convert.adapter.CsvAdapter;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel 转换器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/18 1:54
 */
public class ExcelConvert {

    private ExcelConvert() {
    }

    /**
     * CSV适配器
     *
     * @param sheet  excelSheet
     * @param export csvExport
     * @return adapter
     */
    public static CsvAdapter csvAdapter(Sheet sheet, CsvExport export) {
        return new CsvAdapter(sheet, export);
    }

}

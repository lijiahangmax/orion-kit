package com.orion.office.excel.convert;

import com.orion.office.csv.writer.CsvArrayWriter;
import com.orion.office.excel.convert.adapter.CsvAdapter;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel 转换器
 *
 * @author Jiahang Li
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
     * @param writer CsvArrayWriter
     * @return adapter
     */
    public static CsvAdapter csvAdapter(Sheet sheet, CsvArrayWriter writer) {
        return new CsvAdapter(sheet, writer);
    }

}

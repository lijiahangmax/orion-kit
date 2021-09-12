package com.orion.office.excel.writer;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.Function;

/**
 * Excel lambda 写入器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/11 12:59
 */
public class ExcelLambdaWriter<T> extends BaseExcelWriter<Function<T, ?>, T> {

    public ExcelLambdaWriter(Workbook workbook, Sheet sheet) {
        super(workbook, sheet);
    }

    @Override
    protected Object getValue(T row, Function<T, ?> key) {
        return key.apply(row);
    }

}

package com.orion.office.excel.writer;

import com.orion.utils.Valid;
import com.orion.utils.reflect.Fields;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel Bean 写入器 不支持注解
 * <p>
 * 如果要使用注解需要用 {@link com.orion.office.excel.writer.exporting.ExcelExport} 导出
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/13 14:22
 */
public class ExcelBeanWriter<T> extends BaseExcelWriter<String, T> {

    /**
     * getter
     */
    private Map<String, Method> getter;

    public ExcelBeanWriter(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        super(workbook, sheet);
        Valid.notNull(targetClass, "target class is null");
        this.getter = Methods.getGetterMethodsByCache(targetClass).stream().collect(Collectors.toMap(Fields::getFieldNameByMethod, m -> m));
    }

    @Override
    protected Object getValue(T row, String key) {
        Method method = getter.get(key);
        if (method == null) {
            return null;
        }
        return Methods.invokeMethod(row, method);
    }

}

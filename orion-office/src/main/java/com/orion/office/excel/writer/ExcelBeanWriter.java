package com.orion.office.excel.writer;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.reflect.Fields;
import com.orion.lang.utils.reflect.Methods;
import com.orion.office.excel.option.WriteFieldOption;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * excel bean 写入器 不支持注解
 * <p>
 * 如果要使用注解需要用 {@link com.orion.office.excel.writer.exporting.ExcelExport} 导出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/13 14:22
 */
public class ExcelBeanWriter<T> extends BaseExcelWriter<String, T> {

    private Class<T> targetClass;

    /**
     * getter
     */
    private Map<String, Method> getters;

    public ExcelBeanWriter(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        super(workbook, sheet);
        this.targetClass = Valid.notNull(targetClass, "target class is null");
        this.getters = Methods.getGetterMethodsByCache(targetClass).stream()
                .collect(Collectors.toMap(Fields::getFieldNameByMethod, Function.identity()));
    }

    @Override
    protected void addOption(String field, WriteFieldOption option, Object defaultValue) {
        Valid.notNull(getters.get(field), "not found getter method ({}) in {}", field, targetClass);
        super.addOption(field, option, defaultValue);
    }

    @Override
    protected Object getValue(T row, String key) {
        return Methods.invokeMethod(row, getters.get(key));
    }

}

package com.orion.excel.reader;

import com.orion.excel.Excels;
import com.orion.excel.option.ImportFieldOption;
import com.orion.excel.picture.PictureParser;
import com.orion.excel.type.ExcelReadType;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.codec.Base64s;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Excel 对象读取器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/6 17:10
 */
public class ExcelImport<T> extends ExcelReader<T> {

    private Class<T> targetClass;

    private Constructor<T> constructor;

    /**
     * 如果列为null是否调用setter(null)
     */
    private boolean nullInvoke;

    /**
     * 如果行为null是否添加实例对象
     */
    private boolean nullAddEmptyBean;

    /**
     * 配置信息
     * key: setter
     * value: 配置
     */
    protected Map<Method, ImportFieldOption> options = new HashMap<>();

    /**
     * 图片解析器
     */
    private PictureParser pictureParser;

    /**
     * 解析器
     */
    private ReaderColumnAnalysis analysis;

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        this(workbook, sheet, targetClass, new ArrayList<>(), null);
    }

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> store) {
        this(workbook, sheet, targetClass, store, null);
    }

    public ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, Consumer<T> consumer) {
        this(workbook, sheet, targetClass, null, consumer);
    }

    protected ExcelImport(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        super(workbook, sheet, rows, consumer);
        Valid.notNull(targetClass, "target class is null");
        this.targetClass = targetClass;
        Constructor<T> constructor = Constructors.getDefaultConstructor(targetClass);
        if (constructor == null) {
            throw Exceptions.argument("target class not found default constructor");
        }
        this.constructor = constructor;
        this.analysis = new ReaderColumnAnalysis(targetClass, streaming, options);
        this.analysis.analysis();
    }

    /**
     * 如果列为null是否调用setter(null)
     *
     * @return this
     */
    public ExcelImport<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 如果行为null是否添加实例对象
     *
     * @return this
     */
    public ExcelImport<T> nullAddEmptyBean() {
        this.nullAddEmptyBean = true;
        return this;
    }

    /**
     * 添加配置
     *
     * @param field  field
     * @param option 配置
     * @return this
     */
    public ExcelImport<T> option(String field, ImportFieldOption option) {
        this.addOption(field, option);
        return this;
    }

    /**
     * 添加配置
     *
     * @param field  field
     * @param column 列
     * @param type   类型
     * @return this
     */
    public ExcelImport<T> option(String field, int column, ExcelReadType type) {
        this.addOption(field, new ImportFieldOption(column, type));
        return this;
    }

    /**
     * 添加配置
     *
     * @param field  field
     * @param option 配置
     */
    protected void addOption(String field, ImportFieldOption option) {
        Valid.notNull(option, "field option is null");
        Method setter = Methods.getSetterMethodByCache(targetClass, field);
        analysis.analysisColumn(option, field, setter);
    }

    @Override
    public ExcelImport<T> init() {
        boolean havePicture = options.values().stream()
                .map(ImportFieldOption::getType)
                .anyMatch(ExcelReadType.PICTURE::equals);
        if (havePicture) {
            pictureParser = new PictureParser(workbook, sheet);
            pictureParser.analysis();
        }
        return this;
    }

    @Override
    protected T parserRow(Row row) {
        if (row == null) {
            if (nullAddEmptyBean) {
                return Constructors.newInstance(constructor);
            }
            return null;
        }
        T t = Constructors.newInstance(constructor);
        options.forEach((k, v) -> {
            int index = v.getIndex();
            Cell cell = row.getCell(index);
            Object value;
            if (v.getType().equals(ExcelReadType.PICTURE)) {
                // 图片
                value = this.getPicture(k, row.getRowNum(), index);
            } else {
                value = Excels.getCellValue(cell, v.getType(), v.getCellOption());
            }
            if (value != null) {
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                try {
                    Methods.invokeSetterInfer(t, k, value);
                } catch (Exception e) {
                    // ignore
                }
            } else if (nullInvoke) {
                Methods.invokeMethod(t, k, (Object) null);
            }
        });
        return t;
    }

    /**
     * 读取图片
     *
     * @param setter setter
     * @param row    row
     * @param col    col
     * @return value
     */
    private Object getPicture(Method setter, int row, int col) {
        if (pictureParser == null) {
            return null;
        }
        PictureData picture = pictureParser.getPicture(row, col);
        if (picture == null) {
            return null;
        }
        Class<?> parameterType = setter.getParameterTypes()[0];
        if (parameterType == String.class) {
            return Base64s.img64Encode(picture.getData(), picture.getMimeType());
        } else if (parameterType == byte[].class) {
            return picture.getData();
        } else if (parameterType == OutputStream.class || parameterType == ByteArrayOutputStream.class) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                out.write(picture.getData());
            } catch (Exception e) {
                throw Exceptions.ioRuntime(e);
            }
            return out;
        }
        return null;
    }

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public Map<Method, ImportFieldOption> getOptions() {
        return options;
    }

}

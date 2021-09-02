package com.orion.office.excel.reader;

import com.orion.office.excel.Excels;
import com.orion.office.excel.option.ImportFieldOption;
import com.orion.office.excel.picture.PictureParser;
import com.orion.office.excel.type.ExcelReadType;
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
 * Excel Bean 读取器
 * <p>
 * 支持高级数据类型
 * <p>
 * {@link Excels#getCellValue(Cell, ExcelReadType, com.orion.office.excel.option.CellOption)}
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/6 17:10
 */
public class ExcelBeanReader<T> extends BaseExcelReader<T> {

    private Class<T> targetClass;

    private Constructor<T> constructor;

    /**
     * 如果列为null是否调用setter(null)
     */
    private boolean nullInvoke;

    /**
     * 如果行为null是否添加一个新的实例对象
     */
    private boolean nullAddEmptyBean;

    /**
     * 配置信息
     * key: setter
     * value: 配置
     */
    protected Map<Method, ImportFieldOption> options;

    /**
     * 图片解析器
     */
    private PictureParser pictureParser;

    /**
     * 解析器
     */
    private ReaderColumnAnalysis analysis;

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass) {
        this(workbook, sheet, targetClass, new ArrayList<>(), null);
    }

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> store) {
        this(workbook, sheet, targetClass, store, null);
    }

    public ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, Consumer<T> consumer) {
        this(workbook, sheet, targetClass, null, consumer);
    }

    protected ExcelBeanReader(Workbook workbook, Sheet sheet, Class<T> targetClass, List<T> rows, Consumer<T> consumer) {
        super(workbook, sheet, rows, consumer);
        this.targetClass = Valid.notNull(targetClass, "target class is null");
        this.constructor = Valid.notNull(Constructors.getDefaultConstructor(targetClass), "target class not found default constructor");
        this.options = new HashMap<>();
        this.analysis = new ReaderColumnAnalysis(targetClass, streaming, options);
        this.analysis.analysis();
        this.init = false;
    }

    /**
     * 如果列为null是否调用setter(null)
     *
     * @return this
     */
    public ExcelBeanReader<T> nullInvoke() {
        this.nullInvoke = true;
        return this;
    }

    /**
     * 如果行为null是否添加实例对象
     *
     * @return this
     */
    public ExcelBeanReader<T> nullAddEmptyBean() {
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
    public ExcelBeanReader<T> option(String field, ImportFieldOption option) {
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
    public ExcelBeanReader<T> option(String field, int column, ExcelReadType type) {
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
    public ExcelBeanReader<T> init() {
        if (init) {
            // 已初始化
            return this;
        }
        this.init = true;
        boolean havePicture = options.values().stream()
                .map(ImportFieldOption::getType)
                .anyMatch(ExcelReadType.PICTURE::equals);
        if (havePicture) {
            this.pictureParser = new PictureParser(workbook, sheet);
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
        options.forEach((setter, option) -> {
            int index = option.getIndex();
            Cell cell = row.getCell(index);
            Object value;
            if (option.getType().equals(ExcelReadType.PICTURE)) {
                // 图片
                value = this.getPicture(setter, row.getRowNum(), index);
            } else {
                // 值
                value = Excels.getCellValue(cell, option.getType(), option.getCellOption());
            }
            if (value != null) {
                if (trim && value instanceof String) {
                    value = ((String) value).trim();
                }
                // 调用setter
                try {
                    Methods.invokeSetterInfer(t, setter, value);
                } catch (Exception e) {
                    // ignore
                }
            } else if (nullInvoke) {
                Methods.invokeMethod(t, setter, (Object) null);
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
        // 获取图片
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

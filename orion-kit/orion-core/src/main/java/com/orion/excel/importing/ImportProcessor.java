package com.orion.excel.importing;

import com.orion.excel.Excels;
import com.orion.excel.option.ImportFieldOption;
import com.orion.excel.option.ImportSheetOption;
import com.orion.excel.picture.PictureParser;
import com.orion.excel.type.ExcelReadType;
import com.orion.utils.codec.Base64s;
import com.orion.utils.reflect.Constructors;
import com.orion.utils.reflect.Methods;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Import 处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 0:52
 */
public class ImportProcessor<T> {

    private Workbook workbook;

    private Sheet sheet;

    private ImportSheetOption<T> sheetOption;

    private Map<Method, ImportFieldOption> fieldOptions;

    private Iterator<Row> iterator;

    /**
     * 迭代器索引
     */
    private int currentIndex;

    /**
     * 当前索引
     */
    protected int readRow;

    /**
     * 是否已经读取完毕
     */
    protected boolean end;

    protected ImportProcessor(Workbook workbook, Sheet sheet, ImportSheetOption<T> sheetOption, Map<Method, ImportFieldOption> fieldOptions) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.iterator = sheet.rowIterator();
        this.sheetOption = sheetOption;
        this.fieldOptions = fieldOptions;
    }

    /**
     * 初始化
     */
    protected void init() {
        if (sheetOption.isHavePicture()) {
            PictureParser pictureParser = new PictureParser(workbook, sheet);
            pictureParser.analysis();
            sheetOption.setPictureParser(pictureParser);
        }
    }

    /**
     * 读取一行
     *
     * @return read
     */
    protected T read() {
        if (!iterator.hasNext()) {
            end = true;
            return null;
        }
        Row row = iterator.next();
        if (currentIndex++ == readRow) {
            readRow++;
            return read(row);
        }
        return read();
    }

    /**
     * row -> bean
     *
     * @param row row
     */
    private T read(Row row) {
        if (row == null) {
            return null;
        }
        T t = Constructors.newInstance(sheetOption.getConstructor());
        fieldOptions.forEach((k, v) -> {
            int index = v.getIndex();
            Cell cell = row.getCell(index);
            Object value;
            if (v.getType().equals(ExcelReadType.PICTURE)) {
                // 图片
                value = this.readPicture(k, row.getRowNum(), index);
            } else {
                value = Excels.getCellValue(cell, v.getType(), v.getCellOption());
            }
            if (value != null) {
                try {
                    Methods.invokeSetterInfer(t, k, value);
                } catch (Exception e) {
                    // ignore
                }
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
    private Object readPicture(Method setter, int row, int col) {
        PictureData picture = sheetOption.getPictureParser().getPicture(row, col);
        if (picture == null) {
            return null;
        }
        Class<?> parameterType = setter.getParameterTypes()[0];
        if (parameterType == String.class) {
            return Base64s.img64Encode(picture.getData(), picture.getMimeType());
        } else if (parameterType == byte[].class) {
            return picture.getData();
        } else if (parameterType == OutputStream.class || parameterType == ByteArrayOutputStream.class) {
            return new ByteArrayInputStream(picture.getData());
        }
        return null;
    }

}

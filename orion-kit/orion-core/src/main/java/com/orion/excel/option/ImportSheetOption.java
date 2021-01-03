package com.orion.excel.option;

import com.orion.excel.picture.PictureParser;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Import 表格参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/2 1:06
 */
public class ImportSheetOption<T> implements Serializable {

    /**
     * bean constructor
     */
    private Constructor<T> constructor;

    /**
     * 是否跳过空行
     */
    private boolean skipNullRows = true;

    /**
     * 是否为流式读取
     */
    private boolean streaming;

    /**
     * 行数
     */
    private int rowNum;

    /**
     * 是否包含图片
     */
    private boolean havePicture;

    /**
     * 图片解析器
     */
    private PictureParser pictureParser;

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public boolean isSkipNullRows() {
        return skipNullRows;
    }

    public void setSkipNullRows(boolean skipNullRows) {
        this.skipNullRows = skipNullRows;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public boolean isHavePicture() {
        return havePicture;
    }

    public void setHavePicture(boolean havePicture) {
        this.havePicture = havePicture;
    }

    public PictureParser getPictureParser() {
        return pictureParser;
    }

    public void setPictureParser(PictureParser pictureParser) {
        this.pictureParser = pictureParser;
    }

}

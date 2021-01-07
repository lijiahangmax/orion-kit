package com.orion.excel.option;

import com.orion.excel.picture.PictureParser;

import java.io.Serializable;

/**
 * Reader sheet配置
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/4 17:33
 */
public class ReaderSheetOption implements Serializable {

    /**
     * 读取的列
     */
    private int[] columns;

    /**
     * 是否跳过空行
     */
    private boolean skipNullRows = true;

    /**
     * 是否为流式读取
     */
    private boolean streaming;

    /**
     * 是否包含图片
     */
    private boolean havePicture;

    /**
     * 图片解析器
     */
    private PictureParser pictureParser;

    public int[] getColumns() {
        return columns;
    }

    public void setColumns(int[] columns) {
        this.columns = columns;
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

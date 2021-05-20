package com.orion.office.csv.type;

/**
 * 转义类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/25 11:54
 */
public enum CsvEscapeMode {

    /**
     * 双限定文本
     */
    DOUBLE_QUALIFIER(1),

    /**
     * 反斜杠
     */
    BACKSLASH(2);

    int mode;

    CsvEscapeMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

}

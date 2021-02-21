package com.orion.constant;

/**
 * 常量
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/8 11:05
 */
public class Const {

    private Const() {
    }

    // --------------- array ---------------

    /**
     * 英文字母 数组
     */
    public static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};


    // --------------- project ---------------

    public static final String ORION = "orion";

    public static final String ORION_DISPLAY = ".orion";

    public static final String ORION_VERSION = "1.0.0";

    public static final String ORION_AUTHOR = "Jiahang Li";

    public static final String ORION_EMAIL = "li1553488@aliyun.com";

    // --------------- charset ---------------

    public static final String ASCII = "US-ASCII";

    public static final String GBK = "GBK";

    public static final String GB_2312 = "GB2312";

    public static final String UTF_8 = "UTF-8";

    public static final String UTF_16BE = "UTF-16BE";

    public static final String UTF_16LE = "UTF-16LE";

    public static final String ISO_8859_1 = "ISO-8859-1";

    // --------------- buffer size ---------------

    public static final int BUFFER_KB_1 = 1024;

    public static final int BUFFER_KB_2 = 1024 * 2;

    public static final int BUFFER_KB_4 = 1024 * 4;

    public static final int BUFFER_KB_8 = 1024 * 8;

    public static final int BUFFER_KB_16 = 1024 * 16;

    public static final int BUFFER_KB_32 = 1024 * 32;

    // --------------- ms ---------------

    public static final int MS_S_1 = 1000;

    public static final int MS_S_3 = 1000 * 3;

    public static final int MS_S_5 = 1000 * 5;

    public static final int MS_S_10 = 1000 * 10;

    public static final int MS_S_60 = 1000 * 60;

    // --------------- io ---------------

    public static final String STREAM_CLOSE = "Stream closed";

}

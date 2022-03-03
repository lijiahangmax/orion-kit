package com.orion.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/8 11:05
 */
public abstract class Const {

    // -------------------- array --------------------

    /**
     * 英文字母 数组
     */
    public static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    // -------------------- project --------------------

    public static final String ORION = "orion";

    public static final String ORION_DISPLAY = ".orion";

    public static final String ORION_VERSION = "1.0.0";

    public static final String ORION_AUTHOR = "Jiahang Li";

    public static final String ORION_EMAIL = "li1553488@aliyun.com";

    public static final String ORION_GITHUB = "https://github.com/lijiahangmax";

    public static final String ORION_GITEE = "https://gitee.com/lijiahangmax";

    public static final String ORION_BLOG = "https://blog.csdn.net/qq_41011894";

    // -------------------- letter --------------------

    public static final String CR = "\r";

    public static final String LF = "\n";

    public static final String CR_LF = "\r\n";

    public static final String TAB = "\t";

    public static final String DOT = ".";

    public static final String POUND = "#";

    public static final String DOLLAR = "$";

    public static final String DASHED = "-";

    public static final String SLASH = "/";

    public static final String BACKSLASH = "\\";

    public static final String EMPTY = "";

    public static final String SPACE = " ";

    public static final String SPACE_2 = "  ";

    public static final String SPACE_4 = "    ";

    public static final String OMIT = "...";

    // -------------------- charset --------------------

    public static final String ASCII = "US-ASCII";

    public static final String GBK = "GBK";

    public static final String GB_2312 = "GB2312";

    public static final String UTF_8 = "UTF-8";

    public static final String UTF_16BE = "UTF-16BE";

    public static final String UTF_16LE = "UTF-16LE";

    public static final String ISO_8859_1 = "ISO-8859-1";

    // -------------------- buffer size --------------------

    public static final int BUFFER_KB_1 = 1024;

    public static final int BUFFER_KB_2 = 1024 * 2;

    public static final int BUFFER_KB_4 = 1024 * 4;

    public static final int BUFFER_KB_8 = 1024 * 8;

    public static final int BUFFER_KB_16 = 1024 * 16;

    public static final int BUFFER_KB_32 = 1024 * 32;

    // -------------------- capacity --------------------

    public static final int CAPACITY_1 = 1;

    public static final int CAPACITY_2 = 2;

    public static final int CAPACITY_4 = 4;

    public static final int CAPACITY_8 = 8;

    public static final int CAPACITY_16 = 16;

    public static final int CAPACITY_32 = 32;

    public static final int CAPACITY_64 = 64;

    public static final int CAPACITY_128 = 128;

    public static final int CAPACITY_256 = 256;

    public static final int CAPACITY_512 = 512;

    public static final int CAPACITY_1024 = 1024;

    // -------------------- ms --------------------

    public static final int MS_100 = 100;

    public static final int MS_300 = 300;

    public static final int MS_500 = 500;

    public static final int MS_S_1 = 1000;

    public static final int MS_S_2 = 1000 * 2;

    public static final int MS_S_3 = 1000 * 3;

    public static final int MS_S_5 = 1000 * 5;

    public static final int MS_S_10 = 1000 * 10;

    public static final int MS_S_15 = 1000 * 15;

    public static final int MS_S_30 = 1000 * 30;

    public static final int MS_S_60 = 1000 * 60;

    // -------------------- file path --------------------

    public static final String ROOT = SLASH;

    public static final String SEPARATOR = SLASH;

    // -------------------- io --------------------

    public static final String STREAM_CLOSE = "Stream closed";

    public static final String ACCESS_R = "r";

    public static final String ACCESS_RW = "rw";

    public static final String ACCESS_RWS = "rws";

    public static final String ACCESS_RWD = "rwd";

    // -------------------- num --------------------

    public static final int N_N_1 = -1;

    public static final int N_0 = 0;

    public static final int N_1 = 1;

    public static final int N_2 = 2;

    public static final int N_3 = 3;

    public static final int N_4 = 4;

    public static final int N_5 = 5;

    public static final int N_6 = 6;

    public static final int N_7 = 7;

    public static final int N_8 = 8;

    public static final int N_9 = 9;

    public static final int N_10 = 10;

    public static final int N_100 = 100;

    public static final int N_1000 = 1000;

    public static final int N_10000 = 10000;

    public static final int N_100000 = 100000;

    // -------------------- suffix --------------------

    public static final String SUFFIX_CSV = "csv";

    public static final String SUFFIX_XLS = "xls";

    public static final String SUFFIX_XLSX = "xlsx";

    public static final String SUFFIX_DOC = "doc";

    public static final String SUFFIX_DOCX = "docx";

    public static final String SUFFIX_PDF = "pdf";

    public static final String SUFFIX_JAVA = "java";

    public static final String SUFFIX_CLASS = "class";

    public static final String SUFFIX_PNG = "png";

    public static final String SUFFIX_JAR = "jar";

    public static final String SUFFIX_WAR = "war";

    public static final String SUFFIX_ZIP = "zip";

    public static final String SUFFIX_7Z = "7z";

    public static final String SUFFIX_TAR = "tar";

    public static final String SUFFIX_GZ = "gz";

    public static final String SUFFIX_BZ2 = "bz2";

    public static final String SUFFIX_TAR_GZ = "tar.gz";

    public static final String SUFFIX_TAR_BZ2 = "tar.bz2";

    public static final String SUFFIX_LOG = "log";

    public static final String SUFFIX_XML = "xml";

    public static final String SUFFIX_JSON = "json";

    public static final String SUFFIX_YML = "yml";

    public static final String SUFFIX_TXT = "txt";

    public static final String SUFFIX_PROPERTIES = "properties";

    public static final String SUFFIX_FILE = "file";

    // -------------------- protocol --------------------

    public static final String PROTOCOL_HTTP = "http";

    public static final String PROTOCOL_HTTPS = "https";

    public static final String PROTOCOL_FTP = "ftp";

    public static final String PROTOCOL_FILE = "file";

    public static final String PROTOCOL_JAR = "jar";

    public static final String PROTOCOL_SSH = "ssh";

    // -------------------- font --------------------

    public static final String FONT_MICROSOFT_ELEGANT_BLACK = "微软雅黑";

    // -------------------- sql --------------------

    public static final String LIMIT_1 = "LIMIT 1";

    public static final String ENTITY = "entity";

    public static final String UPDATE = "update";

    public static final String LIST = "list";

    public static final String PAGER = "pager";

    // -------------------- others --------------------

    public static final String BR = "<br/>";

    public static final String NULL = "null";

    public static final String UNKNOWN = "unknown";

    public static final String DEFAULT = "default";

    public static final String LOCALHOST = "localhost";

    public static final String LOCALHOST_IP_V4 = "127.0.0.1";

    public static final String LOCALHOST_IP_V6 = "0:0:0:0:0:0:0:1";

    public static final Integer ENABLE = 1;

    public static final Integer DISABLE = 2;

    public static final Integer INCREMENT = 1;

    public static final Integer DECREMENT = 2;

    public static final Integer NOT_DELETED = 1;

    public static final Integer IS_DELETED = 2;

    public static final String EMPTY_OBJECT = "{}";

    public static final String EMPTY_ARRAY = "[]";

}

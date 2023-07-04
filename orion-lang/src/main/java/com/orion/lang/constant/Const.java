package com.orion.lang.constant;

/**
 * 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/8 11:05
 */
public interface Const extends OrionConst {

    // -------------------- array --------------------

    /**
     * 英文字母 数组
     */
    String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    // -------------------- letter --------------------

    String CR = "\r";

    String LF = "\n";

    String CR_LF = "\r\n";

    String TAB = "\t";

    String DOT = ".";

    String DOT_2 = "..";

    String POUND = "#";

    String DOLLAR = "$";

    String DASHED = "-";

    String SLASH = "/";

    String BACKSLASH = "\\";

    String QUOTE = "\"";

    String SINGLE_QUOTE = "'";

    String UNDERLINE = "_";

    String COLON = ":";

    String COMMA = ",";

    String AMP = "&";

    String AT = "@";

    String EMPTY = "";

    String SPACE = " ";

    String SPACE_2 = "  ";

    String SPACE_4 = "    ";

    String OMIT = "...";

    // -------------------- charset --------------------

    String ASCII = "US-ASCII";

    String GBK = "GBK";

    String GB_2312 = "GB2312";

    String UTF_8 = "UTF-8";

    String UTF_16BE = "UTF-16BE";

    String UTF_16LE = "UTF-16LE";

    String ISO_8859_1 = "ISO-8859-1";

    // -------------------- buffer size --------------------

    int BUFFER_KB_1 = 1024;

    int BUFFER_KB_2 = 1024 * 2;

    int BUFFER_KB_4 = 1024 * 4;

    int BUFFER_KB_8 = 1024 * 8;

    int BUFFER_KB_16 = 1024 * 16;

    int BUFFER_KB_32 = 1024 * 32;

    int MBP = 1024 * 128;

    // -------------------- capacity --------------------

    int CAPACITY_1 = 1;

    int CAPACITY_2 = 2;

    int CAPACITY_4 = 4;

    int CAPACITY_8 = 8;

    int CAPACITY_16 = 16;

    int CAPACITY_32 = 32;

    int CAPACITY_64 = 64;

    int CAPACITY_128 = 128;

    int CAPACITY_256 = 256;

    int CAPACITY_512 = 512;

    int CAPACITY_1024 = 1024;

    // -------------------- ms --------------------

    int MS_100 = 100;

    int MS_300 = 300;

    int MS_500 = 500;

    int MS_S_1 = 1000;

    int MS_S_2 = 1000 * 2;

    int MS_S_3 = 1000 * 3;

    int MS_S_5 = 1000 * 5;

    int MS_S_10 = 1000 * 10;

    int MS_S_15 = 1000 * 15;

    int MS_S_30 = 1000 * 30;

    int MS_S_60 = 1000 * 60;

    // -------------------- file path --------------------

    String ROOT_PATH = SLASH;

    String SEPARATOR = SLASH;

    // -------------------- io --------------------

    String STREAM_CLOSE = "Stream closed";

    String ACCESS_R = "r";

    String ACCESS_RW = "rw";

    String ACCESS_RWS = "rws";

    String ACCESS_RWD = "rwd";

    // -------------------- num --------------------

    Integer N_N_1 = -1;

    Integer N_0 = 0;

    Integer N_1 = 1;

    Integer N_2 = 2;

    Integer N_3 = 3;

    Integer N_4 = 4;

    Integer N_5 = 5;

    Integer N_6 = 6;

    Integer N_7 = 7;

    Integer N_8 = 8;

    Integer N_9 = 9;

    Integer N_10 = 10;

    Integer N_100 = 100;

    Integer N_1000 = 1000;

    Integer N_10000 = 10000;

    Integer N_100000 = 100000;

    Long L_N_1 = -1L;

    Long L_0 = 0L;

    Long L_1 = 1L;

    Double D_N_1 = -1D;

    Double D_0 = 0D;

    Double D_1 = 1D;

    // -------------------- suffix --------------------

    String SUFFIX_CSV = "csv";

    String SUFFIX_XLS = "xls";

    String SUFFIX_XLSX = "xlsx";

    String SUFFIX_DOC = "doc";

    String SUFFIX_DOCX = "docx";

    String SUFFIX_PDF = "pdf";

    String SUFFIX_JAVA = "java";

    String SUFFIX_CLASS = "class";

    String SUFFIX_PNG = "png";

    String SUFFIX_JAR = "jar";

    String SUFFIX_WAR = "war";

    String SUFFIX_ZIP = "zip";

    String SUFFIX_7Z = "7z";

    String SUFFIX_TAR = "tar";

    String SUFFIX_GZ = "gz";

    String SUFFIX_BZ2 = "bz2";

    String SUFFIX_TAR_GZ = "tar.gz";

    String SUFFIX_TAR_BZ2 = "tar.bz2";

    String SUFFIX_LOG = "log";

    String SUFFIX_XML = "xml";

    String SUFFIX_JSON = "json";

    String SUFFIX_YML = "yml";

    String SUFFIX_TXT = "txt";

    String SUFFIX_PROPERTIES = "properties";

    String SUFFIX_FILE = "file";

    // -------------------- protocol --------------------

    String PROTOCOL_HTTP = "http";

    String PROTOCOL_HTTPS = "https";

    String PROTOCOL_FTP = "ftp";

    String PROTOCOL_FILE = "file";

    String PROTOCOL_JAR = "jar";

    String PROTOCOL_SSH = "ssh";

    // -------------------- font --------------------

    String FONT_MICROSOFT_ELEGANT_BLACK = "微软雅黑";

    // -------------------- sql --------------------

    String LIMIT = "LIMIT";

    String LIMIT_1 = "LIMIT 1";

    String ENTITY = "entity";

    String UPDATE = "update";

    String LIST = "list";

    String PAGER = "pager";

    // -------------------- http --------------------

    Integer HTTP_OK_CODE = 200;

    Integer HTTP_BAD_REQUEST_CODE = 400;

    Integer HTTP_NOT_FOUND_CODE = 404;

    Integer HTTP_ERROR_CODE = 500;

    // -------------------- others --------------------

    String BEARER = "Bearer";

    String BASIC = "Basic";

    String OK = "ok";

    String SUCCESS = "success";

    String ERROR = "error";

    String FAILED = "failed";

    String NULL = "null";

    String ROOT = "root";

    String TOKEN = "token";

    String ADMIN = "admin";

    String TIMEOUT = "timeout";

    String UNKNOWN = "unknown";

    String DEFAULT = "default";

    String LOCALHOST = "localhost";

    String LOCALHOST_IP_V4 = "127.0.0.1";

    String LOCALHOST_IP_V6 = "0:0:0:0:0:0:0:1";

    Integer ENABLE = 1;

    Integer DISABLE = 2;

    Integer INCREMENT = 1;

    Integer DECREMENT = 2;

    Integer NOT_DELETED = 1;

    Integer IS_DELETED = 2;

    String EMPTY_OBJECT = "{}";

    String EMPTY_ARRAY = "[]";

    // -------------------- others --------------------

    String BR = "<br/>";

    String HTML_NBSP = "&nbsp;";

    String HTML_AMP = "&amp;";

    String HTML_QUOTE = "&quot;";

    String HTML_APOS = "&apos;";

    String HTML_LT = "&lt;";

    String HTML_GT = "&gt;";

}

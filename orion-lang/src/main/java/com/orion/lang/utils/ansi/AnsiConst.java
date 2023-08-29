package com.orion.lang.utils.ansi;

/**
 * ANSI 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 17:36
 */
public interface AnsiConst {

    String CSI_PREFIX = "\033[";

    String SGR_SUFFIX = "m";

    String CSI_RESET = CSI_PREFIX + 0 + SGR_SUFFIX;

    String JOIN = ";";

    // -------------------- color --------------------

    byte COLOR_FG = 38;

    byte COLOR_BG = 48;

    byte COLOR_BIT8 = 5;

    byte COLOR_BIT24 = 2;

    // -------------------- cursor --------------------

    String CURSOR_UP = "A";

    String CURSOR_DOWN = "B";

    String CURSOR_RIGHT = "C";

    String CURSOR_LEFT = "D";

    String CURSOR_NEXT_LINE = "E";

    String CURSOR_PREV_LINE = "F";

    String CURSOR_COLUMN = "G";

    String CURSOR_MOVE = "H";

    String CURSOR_FORWARD_TAB = "I";

    String CURSOR_BACKWARD_TAB = "Z";

    String CURSOR_MARK = "s";

    String CURSOR_RESUME = "u";

    String CURSOR_REPORT = "6n";

    // -------------------- erase --------------------

    String ERASE_DISPLAY_END = "0J";

    String ERASE_DISPLAY_START = "1J";

    String ERASE_DISPLAY_ALL = "2J";

    String ERASE_LINE_END = "0K";

    String ERASE_LINE_START = "1K";

    String ERASE_LINE_ALL = "2K";

    String ERASE_CHARACTER = "X";

    // -------------------- scroll --------------------

    String SCROLL_UP = "S";

    String SCROLL_DOWN = "T";

    String SCROLL_RIGHT = " A";

    String SCROLL_LEFT = " @";

}

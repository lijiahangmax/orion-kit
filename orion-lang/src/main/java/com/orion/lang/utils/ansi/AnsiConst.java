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

   String JOIN = ";";

   int RESET = 1;

   byte EXTENDED_FG = 38;

   byte EXTENDED_BG = 48;

   byte EXTENDED_BIT8 = 5;

   byte EXTENDED_BIT24 = 2;

   String CURSOR_TOP = "A";

   String CURSOR_BOTTOM = "B";

   String CURSOR_RIGHT = "C";

   String CURSOR_LEFT = "D";

   String CURSOR_NEXT_LINE = "E";

   String CURSOR_PREV_LINE = "F";

   String CURSOR_COLUMN = "G";

   String CURSOR_MOVE = "H";

   String CURSOR_MARK = "s";

   String CURSOR_RESUME = "u";

   String CURSOR_REPORT = "6n";

}

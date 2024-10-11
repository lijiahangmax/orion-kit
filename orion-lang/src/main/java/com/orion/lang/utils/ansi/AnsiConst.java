/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.lang.utils.ansi;

/**
 * ANSI 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 17:36
 */
public interface AnsiConst {

    String ESC = "\033";

    String CSI_PREFIX = "\033[";

    String SGR_SUFFIX = "m";

    String CSI_RESET = CSI_PREFIX + 0 + SGR_SUFFIX;

    String JOIN = ";";

    // -------------------- color --------------------

    byte COLOR_FG = 38;

    byte COLOR_BG = 48;

    byte COLOR_BIT8 = 5;

    byte COLOR_BIT24 = 2;

    // -------------------- char --------------------

    String INSERT_LINE = "L";

    String DELETE_LINE = "M";

    String INSERT_COLUMNS = "'}";

    String DELETE_COLUMNS = "'~";

    String INSERT_BLANK_CHARS = "@";

    String DELETE_CHARS = "P";

    String REPEAT_CHAR = "b";

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

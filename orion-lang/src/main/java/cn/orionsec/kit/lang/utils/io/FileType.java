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
package cn.orionsec.kit.lang.utils.io;

/**
 * 文件类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/2 11:25
 */
public enum FileType {

    /**
     * 普通文件
     */
    REGULAR_FILE("-", "普通文件"),

    /**
     * 目录
     */
    DIRECTORY("d", "目录"),

    /**
     * 链接文件
     */
    SYMLINK("l", "链接文件"),

    /**
     * 管理文件
     */
    MANAGE_FILE("p", "管理文件"),

    /**
     * 块设备文件
     */
    BLOCK_DEVICE_FILE("b", "块设备文件"),

    /**
     * 字符设备文件
     */
    CHARACTER_DEVICE_FILE("c", "字符设备文件"),

    /**
     * 套接字文件
     */
    SOCKET_FILE("s", "套接字文件"),

    ;

    /**
     * 标识符
     */
    private final String symbol;

    /**
     * 类型名称
     */
    private final String typeName;

    FileType(String symbol, String typeName) {
        this.symbol = symbol;
        this.typeName = typeName;
    }

    /**
     * 是否匹配文件类型
     *
     * @param entity entity
     * @return 是否匹配
     */
    public boolean isMatch(String entity) {
        if (entity == null) {
            return false;
        }
        return entity.startsWith(symbol);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTypeName() {
        return typeName;
    }

    public static FileType of(String entity) {
        if (entity == null) {
            return null;
        }
        for (FileType value : values()) {
            if (entity.startsWith(value.symbol)) {
                return value;
            }
        }
        return null;
    }

}

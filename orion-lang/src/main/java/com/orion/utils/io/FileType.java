package com.orion.utils.io;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/2 11:25
 */
public enum FileType {

    /**
     * 普通文件
     */
    NORMAL_FILE("-", "普通文件"),

    /**
     * 目录
     */
    DIRECTORY("d", "目录"),

    /**
     * 链接文件
     */
    LINK_FILE("l", "链接文件"),

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

    FileType(String symbol, String typeName) {
        this.symbol = symbol;
        this.typeName = typeName;
    }

    /**
     * 标识符
     */
    private final String symbol;

    /**
     * 类型名称
     */
    private final String typeName;

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

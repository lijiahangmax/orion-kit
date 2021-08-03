package com.orion.remote.channel.sftp;

/**
 * SFTP 错误信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/10 16:45
 */
public enum SftpErrorMessage {

    /**
     * 未找到文件
     */
    NO_SUCH_FILE("no such file"),

    BAD_MESSAGE("bad message");

    private final String message;

    SftpErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

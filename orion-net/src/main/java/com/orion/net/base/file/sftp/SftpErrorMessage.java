package com.orion.net.base.file.sftp;

import com.orion.lang.utils.Strings;

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

    /**
     * 是否为此错误原因
     *
     * @param e e
     * @return isCause
     */
    public boolean isCause(Exception e) {
        String message = e.getMessage();
        if (Strings.isEmpty(message)) {
            return false;
        }
        return message.toLowerCase().contains(this.message);
    }

}

package com.orion.utils.crypto.enums;

/**
 * 工作模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/3 10:08
 */
public enum WorkingMode {

    /**
     * None
     */
    NONE("None"),

    /**
     * 电子密码本模式 AES DES 3DES
     */
    ECB("ECB"),

    /**
     * 密码分组连接模式 AES DES 3DES
     */
    CBC("CBC"),

    /**
     * 密文反馈模式 AES DES 3DES
     */
    CFB("CFB"),

    /**
     * 输出反馈模式 AES DES 3DES
     */
    OFB("OFB"),

    /**
     * 计数器模式 AES DES 3DES
     */
    FTP("FTP"),

    /**
     * 加密认证模式 AES
     */
    GCM("GCM");

    WorkingMode(String mode) {
        this.mode = mode;
    }

    private final String mode;

    public String getMode() {
        return mode;
    }

}

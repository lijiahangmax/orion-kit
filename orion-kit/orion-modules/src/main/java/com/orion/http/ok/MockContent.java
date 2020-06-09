package com.orion.http.ok;

/**
 * Mock Content-Type
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:02
 */
public enum MockContent {

    /**
     * HTML格式
     */
    TEXT_HTML("text/html"),

    /**
     * 纯文本格式
     */
    TEXT_PLAIN("text/plain"),

    /**
     * XML格式
     */
    TEXT_XML("text/xml"),

    /**
     * gif图片格式
     */
    IMAGE_GIF("image/gif"),

    /**
     * jpg图片格式
     */
    IMAGE_JPEG("image/jpeg"),

    /**
     * png图片格式
     */
    IMAGE_PNG("image/png"),

    /**
     * XML数据格式
     */
    APPLICATION_XML("application/xml"),

    /**
     * JSON数据格式
     */
    APPLICATION_JSON("application/json"),

    /**
     * pdf格式
     */
    APPLICATION_PDF("application/pdf"),

    /**
     * Word文档格式
     */
    APPLICATION_MSWORD("application/msword"),

    /**
     * 二进制流数据
     */
    APPLICATION_STREAM("application/octet-stream"),

    /**
     * FORM表单
     */
    APPLICATION_FORM("application/x-www-form-urlencoded"),

    /**
     * FORM上传文件
     */
    MULTIPART_FORM("multipart/form-data");

    String type;

    MockContent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

package com.orion.lang.constant;

/**
 * 标准 ContentType
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/8 11:05
 */
public interface StandardContentType {

    String CONTENT_TYPE = StandardHttpHeader.CONTENT_TYPE;

    // -------------------- text --------------------

    /**
     * html 格式
     */
    String TEXT_HTML = "text/html";

    /**
     * 纯文本格式
     */
    String TEXT_PLAIN = "text/plain";

    /**
     * xml 格式
     */
    String TEXT_XML = "text/xml";

    // -------------------- image --------------------

    /**
     * gif 图片格式
     */
    String IMAGE_GIF = "image/gif";

    /**
     * jpg jpeg 图片格式
     */
    String IMAGE_JPEG = "image/jpeg";

    /**
     * png 图片格式
     */
    String IMAGE_PNG = "image/png";

    // -------------------- application --------------------

    /**
     * xml 数据格式
     */
    String APPLICATION_XML = "application/xml";

    /**
     * json 数据格式
     */
    String APPLICATION_JSON = "application/json";

    /**
     * pdf 格式
     */
    String APPLICATION_PDF = "application/pdf";

    /**
     * zip 格式
     */
    String APPLICATION_ZIP = "application/zip";

    /**
     * word 格式
     */
    String APPLICATION_MS_WORD = "application/msword";

    /**
     * javascript 格式
     */
    String APPLICATION_JAVASCRIPT = "application/javascript";

    /**
     * 二进制流数据
     */
    String APPLICATION_STREAM = "application/octet-stream";

    // -------------------- form --------------------

    /**
     * form表单
     */
    String APPLICATION_FORM = "application/x-www-form-urlencoded";

    /**
     * form表单上传文件
     */
    String MULTIPART_FORM = "multipart/form-data";

}

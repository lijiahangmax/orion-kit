/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.http.support;

import cn.orionsec.kit.lang.constant.StandardContentType;

/**
 * HTTP Content-Type
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/8 10:02
 */
public enum HttpContentType {

    /**
     * html 格式
     */
    TEXT_HTML(StandardContentType.TEXT_HTML),

    /**
     * 纯文本格式
     */
    TEXT_PLAIN(StandardContentType.TEXT_PLAIN),

    /**
     * xml 格式
     */
    TEXT_XML(StandardContentType.TEXT_XML),

    /**
     * gif 图片格式
     */
    IMAGE_GIF(StandardContentType.IMAGE_GIF),

    /**
     * jpg jpeg 图片格式
     */
    IMAGE_JPEG(StandardContentType.IMAGE_JPEG),

    /**
     * png 图片格式
     */
    IMAGE_PNG(StandardContentType.IMAGE_PNG),

    /**
     * xml 数据格式
     */
    APPLICATION_XML(StandardContentType.APPLICATION_XML),

    /**
     * json 数据格式
     */
    APPLICATION_JSON(StandardContentType.APPLICATION_JSON),

    /**
     * pdf 格式
     */
    APPLICATION_PDF(StandardContentType.APPLICATION_PDF),

    /**
     * zip 格式
     */
    APPLICATION_ZIP(StandardContentType.APPLICATION_ZIP),

    /**
     * word 文档格式
     */
    APPLICATION_MS_WORD(StandardContentType.APPLICATION_MS_WORD),

    /**
     * javascript 文档格式
     */
    APPLICATION_JAVASCRIPT(StandardContentType.APPLICATION_JAVASCRIPT),

    /**
     * 二进制流数据
     */
    APPLICATION_STREAM(StandardContentType.APPLICATION_STREAM),

    /**
     * FORM表单
     */
    APPLICATION_FORM(StandardContentType.APPLICATION_FORM),

    /**
     * FORM上传文件
     */
    MULTIPART_FORM(StandardContentType.MULTIPART_FORM);

    private final String type;

    HttpContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

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
package cn.orionsec.kit.lang.constant;

/**
 * 标准 ContentType
 * <p>
 * type/subtype;key=value
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/8 11:05
 */
public interface StandardContentType {

    String CONTENT_TYPE = StandardHttpHeader.CONTENT_TYPE;

    /**
     *
     */
    String ALL = "*/*";

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

    String TEXT_EVENT_STREAM = "text/event-stream";

    String TEXT_MARKDOWN = "text/markdown";

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
     * json 数据格式
     */
    String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

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

    /**
     * form表单
     */
    String APPLICATION_FORM = "application/x-www-form-urlencoded";

    String APPLICATION_ATOM_XML = "application/atom+xml";

    String APPLICATION_XHTML_XML = "application/xhtml+xml";

    String APPLICATION_CBOR = "application/cbor";

    String APPLICATION_GRAPHQL = "application/graphql+json";

    String APPLICATION_PROBLEM_JSON = "application/problem+json";

    String APPLICATION_PROBLEM_XML = "application/problem+xml";

    String APPLICATION_RSS_XML = "application/rss+xml";

    String APPLICATION_NDJSON = "application/x-ndjson";

    // -------------------- multipart --------------------

    /**
     * form 表单上传文件
     */
    String MULTIPART_FORM = "multipart/form-data";

    String MULTIPART_MIXED = "multipart/mixed";

    String MULTIPART_RELATED = "multipart/related";

}

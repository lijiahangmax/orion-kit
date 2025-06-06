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
import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;
import java.io.InputStream;

/**
 * Http 上传文件实体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/14 11:47
 */
public class HttpUploadPart {

    /**
     * server param
     */
    private String param;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 文件名称
     * <p>
     * file 默认文件名
     * 其他默认 uuid
     */
    private String fileName;

    /**
     * contentType
     */
    private String contentType = StandardContentType.APPLICATION_STREAM;

    private File file;

    private InputStream in;

    private byte[] bytes;

    /**
     * HttpClient 无效
     */
    private int off;

    /**
     * HttpClient 无效
     */
    private int len;

    public HttpUploadPart(String param) {
        this.param = param;
    }

    public HttpUploadPart(String param, String suffix) {
        this.param = param;
        this.suffix = suffix;
    }

    public HttpUploadPart(String param, File file) {
        this.param = param;
        this.file = file;
        this.suffix = Files1.getFileNameSuffix(file);
    }

    public HttpUploadPart(String param, byte[] bytes) {
        this.param = param;
        this.bytes = bytes;
        this.len = bytes.length;
    }

    public HttpUploadPart(String param, byte[] bytes, String suffix) {
        this.param = param;
        this.bytes = bytes;
        this.len = bytes.length;
        this.suffix = suffix;
    }

    public HttpUploadPart(String param, byte[] bytes, int off, int len) {
        this.param = param;
        this.bytes = bytes;
        this.off = off;
        this.len = len;
    }

    public HttpUploadPart(String param, byte[] bytes, int off, int len, String suffix) {
        this.param = param;
        this.bytes = bytes;
        this.off = off;
        this.len = len;
        this.suffix = suffix;
    }

    public HttpUploadPart(String param, InputStream in) {
        this.param = param;
        this.in = in;
    }

    public HttpUploadPart(String param, InputStream in, String suffix) {
        this.param = param;
        this.in = in;
        this.suffix = suffix;
    }

    public String getParam() {
        return param;
    }

    public String getFileName() {
        if (fileName == null) {
            if (file != null) {
                this.fileName = Files1.getFileName(file);
            } else {
                this.fileName = UUIds.random32();
                if (suffix != null) {
                    this.fileName += suffix;
                }
            }
        }
        return fileName;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setBytes(byte[] bytes, int off, int len) {
        this.bytes = bytes;
        this.off = off;
        this.len = len;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.off = 0;
        this.len = bytes.length;
    }

    public String getContentType() {
        return contentType;
    }

    public String getSuffix() {
        return suffix;
    }

    public File getFile() {
        return file;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getOff() {
        return off;
    }

    public int getLen() {
        return len;
    }

    public InputStream getIn() {
        return in;
    }

}

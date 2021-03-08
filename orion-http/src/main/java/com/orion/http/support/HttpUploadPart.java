package com.orion.http.support;

import com.orion.constant.StandardContentType;
import com.orion.id.UUIds;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.InputStream;

/**
 * Http 上传文件实体
 *
 * @author ljh15
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
     * file默认文件名
     * 其他默认uuid
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
     * HttpClient无效
     */
    private int off;

    /**
     * HttpClient无效
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
        this.suffix = Files1.getSuffix(file);
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
            if (this.file != null) {
                fileName = Files1.getFileName(file);
            } else {
                fileName = UUIds.random32();
                if (suffix != null) {
                    fileName += suffix;
                }
            }
        }
        return fileName;
    }

    public HttpUploadPart setParam(String param) {
        this.param = param;
        return this;
    }

    public HttpUploadPart setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public HttpUploadPart setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public HttpUploadPart setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpUploadPart setIn(InputStream in) {
        this.in = in;
        return this;
    }

    public HttpUploadPart setFile(File file) {
        this.file = file;
        return this;
    }

    public HttpUploadPart setBytes(byte[] bytes, int off, int len) {
        this.bytes = bytes;
        this.off = off;
        this.len = len;
        return this;
    }

    public HttpUploadPart setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.len = bytes.length;
        return this;
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

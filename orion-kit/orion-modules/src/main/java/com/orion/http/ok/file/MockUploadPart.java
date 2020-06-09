package com.orion.http.ok.file;

import com.orion.id.UUIds;
import com.orion.utils.Streams;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Mock 上传文件实体
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/14 11:47
 */
public class MockUploadPart {

    /**
     * serverKey
     */
    private String key;

    /**
     * 文件名称
     * file默认文件名
     * 其他默认uuid
     */
    private String fileName;

    /**
     * contentType 默认null
     */
    private String contentType;

    private File file;

    private byte[] bytes;

    private int off;

    private int len;

    private InputStream in;

    public MockUploadPart(String key) {
        this.key = key;
    }

    public MockUploadPart(String key, File file) {
        this.key = key;
        this.file = file;
    }

    public MockUploadPart(String key, byte[] bytes) {
        this.key = key;
        this.bytes = bytes;
        this.len = bytes.length;
    }

    public MockUploadPart(String key, byte[] bytes, int off, int len) {
        this.key = key;
        this.bytes = bytes;
        this.off = off;
        this.len = len;
    }

    public MockUploadPart(String key, InputStream in) {
        this.key = key;
        this.in = in;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        if (fileName == null) {
            if (this.file != null) {
                fileName = Files1.getFileName(file);
            } else {
                fileName = UUIds.random32();
            }
        }
        return fileName;
    }

    public String getContentType() {
        return contentType;
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

    public MockUploadPart setKey(String key) {
        this.key = key;
        return this;
    }

    public MockUploadPart setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public MockUploadPart setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public MockUploadPart setFile(File file) {
        this.file = file;
        return this;
    }

    public MockUploadPart setBytes(byte[] bytes, int off, int len) {
        this.bytes = bytes;
        this.off = off;
        this.len = len;
        return this;
    }

    public MockUploadPart setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.len = bytes.length;
        return this;
    }

    public MockUploadPart setIn(InputStream in) throws IOException {
        this.in = in;
        this.bytes = Streams.toByteArray(in);
        this.len = bytes.length;
        return this;
    }

}

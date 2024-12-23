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
package cn.orionsec.kit.http.apache.file;

import cn.orionsec.kit.http.apache.ApacheRequest;
import cn.orionsec.kit.http.apache.ApacheResponse;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Apache 文件下载
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/30 23:32
 */
public class ApacheDownload {

    private final ApacheRequest request;

    private ApacheResponse response;

    public ApacheDownload(String url) {
        this(new ApacheRequest(url), null);
    }

    public ApacheDownload(String url, CloseableHttpClient client) {
        this(new ApacheRequest(url), client);
    }

    public ApacheDownload(ApacheRequest request) {
        this(request, null);
    }

    public ApacheDownload(ApacheRequest request, CloseableHttpClient client) {
        this.request = request;
        if (client != null) {
            this.request.client(client);
        }
    }

    public ApacheDownload client(CloseableHttpClient client) {
        request.client(client);
        return this;
    }

    public ApacheDownload download(String file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public ApacheDownload download(File file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public ApacheDownload download(OutputStream out) throws IOException {
        this.download(out, false);
        return this;
    }

    public ApacheDownload download(OutputStream out, boolean autoClose) throws IOException {
        this.response = request.await();
        out.write(response.getBody());
        if (autoClose) {
            Streams.close(out);
        }
        return this;
    }

    public ApacheRequest getRequest() {
        return request;
    }

    public ApacheResponse getResponse() {
        return response;
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.http.ok.file;

import cn.orionsec.kit.http.ok.OkRequest;
import cn.orionsec.kit.http.ok.OkResponse;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * OkHttp 下载文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/25 0:15
 */
public class OkDownload {

    private final OkRequest request;

    private OkResponse response;

    public OkDownload(String url) {
        this(new OkRequest(url), null);
    }

    public OkDownload(String url, OkHttpClient client) {
        this(new OkRequest(url), client);
    }

    public OkDownload(OkRequest request) {
        this(request, null);
    }

    public OkDownload(OkRequest request, OkHttpClient client) {
        this.request = request;
        if (client != null) {
            this.request.client(client);
        }
    }

    public OkDownload client(OkHttpClient client) {
        request.client(client);
        return this;
    }

    public OkDownload download(String file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public OkDownload download(File file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public OkDownload download(OutputStream out) throws IOException {
        this.download(out, false);
        return this;
    }

    public OkDownload download(OutputStream out, boolean autoClose) throws IOException {
        this.response = request.await();
        out.write(response.getBody());
        if (autoClose) {
            Streams.close(out);
        }
        return this;
    }

    public OkRequest getRequest() {
        return request;
    }

    public OkResponse getResponse() {
        return response;
    }

}

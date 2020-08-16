package com.orion.http.ok.file;

import com.orion.http.ok.MockRequest;
import com.orion.http.ok.MockResponse;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Mock 下载文件
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/25 0:15
 */
public class MockDownload {

    private MockRequest request;

    private MockResponse response;

    public MockDownload(String url) {
        this.request = new MockRequest(url).client(null);
    }

    public MockDownload(String url, OkHttpClient client) {
        this.request = new MockRequest(url).client(client);
    }

    public MockDownload(MockRequest request) {
        this.request = request.client(null);
    }

    public MockDownload(MockRequest request, OkHttpClient client) {
        this.request = request.client(client);
    }

    public MockDownload client(OkHttpClient client) {
        this.request.client(client);
        return this;
    }

    public MockDownload download(String file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public MockDownload download(File file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public MockDownload download(OutputStream out) throws IOException {
        this.download(out, false);
        return this;
    }

    public MockDownload download(OutputStream out, boolean autoClose) throws IOException {
        this.response = this.request.await();
        out.write(this.response.getBody());
        if (autoClose) {
            Streams.close(out);
        }
        return this;
    }

    public MockRequest getRequest() {
        return request;
    }

    public MockResponse getResponse() {
        return response;
    }

}

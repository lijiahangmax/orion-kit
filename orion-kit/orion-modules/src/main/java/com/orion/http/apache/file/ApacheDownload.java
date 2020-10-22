package com.orion.http.apache.file;

import com.orion.http.apache.ApacheRequest;
import com.orion.http.apache.ApacheResponse;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Hyper 文件下载
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/30 23:32
 */
public class ApacheDownload {

    private ApacheRequest request;

    private ApacheResponse response;

    public ApacheDownload(String url) {
        this.request = new ApacheRequest(url).client(null);
    }

    public ApacheDownload(String url, CloseableHttpClient client) {
        this.request = new ApacheRequest(url).client(client);
    }

    public ApacheDownload(ApacheRequest request) {
        this.request = request.client(null);
    }

    public ApacheDownload(ApacheRequest request, CloseableHttpClient client) {
        this.request = request.client(client);
    }

    public ApacheDownload client(CloseableHttpClient client) {
        this.request.client(client);
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
        this.response = this.request.await();
        out.write(this.response.getBody());
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

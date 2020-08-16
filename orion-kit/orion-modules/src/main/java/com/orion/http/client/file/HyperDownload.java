package com.orion.http.client.file;

import com.orion.http.client.HyperRequest;
import com.orion.http.client.HyperResponse;
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
 * @date 2020/6/30 23:32
 */
public class HyperDownload {

    private HyperRequest request;

    private HyperResponse response;

    public HyperDownload(String url) {
        this.request = new HyperRequest(url).client(null);
    }

    public HyperDownload(String url, CloseableHttpClient client) {
        this.request = new HyperRequest(url).client(client);
    }

    public HyperDownload(HyperRequest request) {
        this.request = request.client(null);
    }

    public HyperDownload(HyperRequest request, CloseableHttpClient client) {
        this.request = request.client(client);
    }

    public HyperDownload client(CloseableHttpClient client) {
        this.request.client(client);
        return this;
    }

    public HyperDownload download(String file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public HyperDownload download(File file) throws IOException {
        Files1.touch(file);
        this.download(Files1.openOutputStream(file), true);
        return this;
    }

    public HyperDownload download(OutputStream out) throws IOException {
        this.download(out, false);
        return this;
    }

    public HyperDownload download(OutputStream out, boolean autoClose) throws IOException {
        this.response = this.request.await();
        out.write(this.response.getBody());
        if (autoClose) {
            Streams.close(out);
        }
        return this;
    }

    public HyperRequest getRequest() {
        return request;
    }

    public HyperResponse getResponse() {
        return response;
    }

}

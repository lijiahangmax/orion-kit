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
package cn.orionsec.kit.http.tests;

import cn.orionsec.kit.http.apache.ApacheRequest;
import cn.orionsec.kit.http.apache.ApacheResponse;
import cn.orionsec.kit.http.apache.file.ApacheDownload;
import cn.orionsec.kit.http.apache.file.ApacheUpload;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.support.HttpUploadPart;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Apache HttpClient 请求测试 (基于 mockwebserver 自包含)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/4 18:42
 */
public class ApacheTests {

    private MockWebServer server;

    private String baseUrl;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        baseUrl = server.url("/").toString();
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    @Test
    public void testGet() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("hello"));
        ApacheResponse resp = new ApacheRequest(baseUrl + "http/req").await();
        Assert.assertEquals(200, resp.getCode());
        Assert.assertEquals("hello", resp.getBodyString());
    }

    @Test
    public void testPostBody() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));
        ApacheRequest req = new ApacheRequest(baseUrl + "http/req");
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("body-content");
        ApacheResponse resp = req.await();
        Assert.assertEquals("ok", resp.getBodyString());
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        Assert.assertEquals("B", recorded.getHeader("A"));
        Assert.assertEquals("body-content", recorded.getBody().readUtf8());
    }

    @Test
    public void testFormPost() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));
        ApacheRequest req = new ApacheRequest(baseUrl + "http/form");
        req.method(HttpMethod.POST);
        req.formPart("name", "whh");
        req.formPart("age", "18");
        req.await();
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        String body = recorded.getBody().readUtf8();
        Assert.assertTrue(body.contains("name=whh"));
        Assert.assertTrue(body.contains("age=18"));
    }

    @Test
    public void testDelete() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(204));
        ApacheRequest req = new ApacheRequest(baseUrl + "http/text");
        req.method(HttpMethod.DELETE);
        ApacheResponse resp = req.await();
        Assert.assertEquals(204, resp.getCode());
        Assert.assertEquals("DELETE", server.takeRequest().getMethod());
    }

    @Test
    public void testUpload() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("uploaded"));
        ApacheUpload req = new ApacheUpload(baseUrl + "http/upload");
        req.method(HttpMethod.POST);
        req.part(new HttpUploadPart("file", "文件内容".getBytes(), ".txt"));
        ApacheResponse resp = req.await();
        Assert.assertEquals("uploaded", resp.getBodyString());
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        Assert.assertTrue(recorded.getBody().readUtf8().contains("file"));
    }

    @Test
    public void testDownload() throws IOException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("file-data"));
        ApacheRequest req = new ApacheRequest(baseUrl + "http/download");
        ApacheDownload download = new ApacheDownload(req);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        download.download(out);
        Assert.assertEquals("file-data", out.toString());
    }

}

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

import cn.orionsec.kit.http.ok.OkRequest;
import cn.orionsec.kit.http.ok.OkResponse;
import cn.orionsec.kit.http.ok.file.OkDownload;
import cn.orionsec.kit.http.ok.file.OkUpload;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * OkHttp 请求测试 (基于 mockwebserver 自包含)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/5 16:27
 */
public class OkTests {

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
        OkResponse resp = new OkRequest(baseUrl + "http/req").await();
        Assert.assertEquals(200, resp.getCode());
        Assert.assertEquals("hello", resp.getBodyString());
    }

    @Test
    public void testPostBody() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"));
        OkRequest req = new OkRequest(baseUrl + "http/req");
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("body-content");
        OkResponse resp = req.await();
        Assert.assertEquals("ok", resp.getBodyString());
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        Assert.assertEquals("B", recorded.getHeader("A"));
        Assert.assertEquals("body-content", recorded.getBody().readUtf8());
    }

    @Test
    public void testDelete() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("deleted"));
        OkRequest req = new OkRequest(baseUrl + "http/text");
        req.method(HttpMethod.DELETE);
        OkResponse resp = req.await();
        Assert.assertEquals("deleted", resp.getBodyString());
        Assert.assertEquals("DELETE", server.takeRequest().getMethod());
    }

    @Test
    public void testUpload() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("uploaded"));
        OkUpload req = new OkUpload(baseUrl + "http/upload");
        req.method(HttpMethod.POST);
        req.part(new HttpUploadPart("file", "文件内容".getBytes(), ".txt"));
        OkResponse resp = req.await();
        Assert.assertEquals("uploaded", resp.getBodyString());
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        Assert.assertTrue(recorded.getHeader("Content-Type").startsWith("multipart/form-data"));
    }

    @Test
    public void testDownload() throws IOException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("file-data"));
        OkRequest req = new OkRequest(baseUrl + "http/download");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new OkDownload(req).download(out);
        Assert.assertEquals("file-data", out.toString());
    }

    @Test
    public void testAsync() throws InterruptedException {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("async-body"));
        OkRequest req = new OkRequest(baseUrl + "http/req");
        req.method(HttpMethod.POST);
        req.body("hi");
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> ref = new AtomicReference<>();
        req.async(s -> {
            ref.set(s.getBodyString());
            latch.countDown();
        });
        Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
        Assert.assertEquals("async-body", ref.get());
    }

}

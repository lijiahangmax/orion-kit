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

import cn.orionsec.kit.http.parse.ParseRequest;
import cn.orionsec.kit.http.parse.ParseResponse;
import cn.orionsec.kit.http.support.HttpMethod;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * jsoup parse 请求测试 (基于 mockwebserver 自包含)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 17:37
 */
public class ParseTests {

    private static final String HTML = "<html><head><title>t</title></head>"
            + "<body><div id=\"div1\"><p id=\"p\">hello</p></div></body></html>";

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
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .setBody(HTML));
        ParseResponse resp = new ParseRequest(baseUrl + "http/html").await();
        Assert.assertTrue(resp.getBodyString().contains("hello"));
    }

    @Test
    public void testParseHtml() {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .setBody(HTML));
        ParseRequest req = new ParseRequest(baseUrl + "http/html");
        req.method(HttpMethod.GET);
        ParseResponse resp = req.await();
        Document doc = resp.parse().getDocument();
        Assert.assertEquals("hello", doc.select("#p").text());
        Assert.assertEquals("hello", doc.select("#div1 p").text());
    }

    @Test
    public void testPostBody() throws InterruptedException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "text/html; charset=utf-8")
                .setBody(HTML));
        ParseRequest req = new ParseRequest(baseUrl + "http/req");
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("body-content");
        req.await();
        RecordedRequest recorded = server.takeRequest();
        Assert.assertEquals("POST", recorded.getMethod());
        Assert.assertEquals("B", recorded.getHeader("A"));
    }

}

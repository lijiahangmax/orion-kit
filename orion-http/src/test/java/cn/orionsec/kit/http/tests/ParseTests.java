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
import cn.orionsec.kit.http.parse.ParseRequestConfig;
import cn.orionsec.kit.http.parse.ParseResponse;
import cn.orionsec.kit.http.support.HttpCookie;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.useragent.UserAgentGenerators;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 17:37
 */
public class ParseTests {

    private static final String REQ = "http://localhost:8080/http/req";
    private static final String SLEEP = "http://localhost:8080/http/sleep";
    private static final String TEXT = "http://localhost:8080/http/text";
    private static final String HTML = "http://localhost:8080/http/html";
    private static final String NULL = "http://localhost:8080/http/null";
    private static final String TIMEOUT = "http://localhost:8081/http/download";

    @Test
    public void testReq1() {
        System.out.println(new ParseRequest(TEXT).await().getBodyString());
    }

    @Test
    public void testReq2() {
        ParseRequest req = new ParseRequest(TEXT);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("身体哦");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq3() {
        ParseRequest req = new ParseRequest(TEXT);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.DELETE);
        req.queryParam("name", "12");
        req.header("A", "B");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq4() {
        ParseRequest req = new ParseRequest(TEXT);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.formPart("name", "whh");
        req.formPart("age", "18");
        req.formPart("sex", "女");
        req.cookie(new HttpCookie().addValue("uid", "1"));
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq5() {
        ParseRequest req = new ParseRequest(SLEEP);
        req.method(HttpMethod.POST);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq6() {
        ParseRequest req = new ParseRequest(TEXT);
        req.method(HttpMethod.DELETE);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq7() {
        ParseRequest req = new ParseRequest(HTML);
        req.method(HttpMethod.GET);
        ParseResponse await = req.await();
        System.out.println(await.getBodyString());
        Document doc = await.parse().getDocument();
        System.out.println(doc.select("button").attr("onclick"));
        System.out.println(doc.select("#p"));
        System.out.println(doc.select("#div1 p"));
    }

    @Test
    public void testReq8() {
        ParseRequest req = new ParseRequest(NULL);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq9() {
        ParseRequest req = new ParseRequest(TIMEOUT);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq10() {
        ParseRequest req = new ParseRequest(REQ);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq11() {
        ParseRequest req = new ParseRequest(REQ);
        ParseRequestConfig config = new ParseRequestConfig();
        config.ignoreContentType(false);
        req.config(config);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

}

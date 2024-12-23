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
import cn.orionsec.kit.http.ok.file.OkAsyncDownload;
import cn.orionsec.kit.http.ok.file.OkAsyncUpload;
import cn.orionsec.kit.http.ok.file.OkDownload;
import cn.orionsec.kit.http.ok.file.OkUpload;
import cn.orionsec.kit.http.support.HttpCookie;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.support.HttpUploadPart;
import cn.orionsec.kit.http.useragent.UserAgentGenerators;
import cn.orionsec.kit.lang.utils.Threads;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/5 16:27
 */
public class OkTests {

    private static final String REQ = "http://localhost:8080/http/req";
    private static final String REQ1 = "http://localhost:8080/http/req1";
    private static final String SLEEP = "http://localhost:8080/http/sleep";
    private static final String TEXT = "http://localhost:8080/http/text";
    private static final String HTML = "http://localhost:8080/http/html";
    private static final String UP = "http://localhost:8080/http/upload";
    private static final String DOWN = "http://localhost:8080/http/download";
    private static final String NULL = "http://localhost:8080/http/null";
    private static final String TIMEOUT = "http://localhost:8081/http/download";

    @Test
    public void testReq1() {
        System.out.println(new OkRequest(REQ).await().getBodyString());
    }

    @Test
    public void testReq2() {
        OkRequest req = new OkRequest(REQ);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("身体哦");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq3() {
        OkRequest req = new OkRequest(REQ1);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.DELETE);
        req.queryParam("name", "12");
        req.header("A", "B");
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq4() {
        OkRequest req = new OkRequest(REQ1);
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
        OkRequest req = new OkRequest(SLEEP);
        req.method(HttpMethod.POST);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq6() {
        OkRequest req = new OkRequest(TEXT);
        req.method(HttpMethod.DELETE);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq7() {
        OkRequest req = new OkRequest(HTML);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq8() {
        OkRequest req = new OkRequest(NULL);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq9() {
        OkRequest req = new OkRequest(TIMEOUT);
        req.method(HttpMethod.GET);
        System.out.println(req.await().getBodyString());
    }

    @Test
    public void testReq10() {
        OkUpload req = new OkUpload(UP);
        req.method(HttpMethod.PATCH);
        req.part(new HttpUploadPart("file", "文件内容1".getBytes(), ".txt"));
        System.out.println(req.await());
    }

    @Test
    public void testReq11() {
        OkAsyncUpload req = new OkAsyncUpload(UP);
        req.method(HttpMethod.PATCH);
        req.part(new HttpUploadPart("file", "文件内容2".getBytes(), ".txt"));
        req.async(s -> {
            System.out.println(s);
            System.out.println(s.getBodyString());
        });
        Threads.sleep(2000);
    }

    @Test
    public void testReq12() throws IOException {
        OkRequest req = new OkRequest(DOWN + "?name={}");
        req.format("哈哈");
        new OkDownload(req).download(System.out);
    }

    @Test
    public void testReq13() {
        OkAsyncDownload req = new OkAsyncDownload(DOWN + "?name={}");
        req.format("哈哈");
        req.download(System.out);
        req.async(System.out::println);
        Threads.sleep(2000);
    }

    @Test
    public void testReq14() {
        OkRequest req = new OkRequest(REQ);
        req.userAgent(UserAgentGenerators.generator());
        req.method(HttpMethod.POST);
        req.header("A", "B");
        req.body("身体哦");
        req.async(s -> System.out.println(s.getBodyString()));
        Threads.sleep(2000);
    }

}

package com.orion.http.tests;

import com.orion.http.ok.OkRequest;
import com.orion.http.ok.file.OkAsyncDownload;
import com.orion.http.ok.file.OkAsyncUpload;
import com.orion.http.ok.file.OkDownload;
import com.orion.http.ok.file.OkUpload;
import com.orion.http.support.HttpCookie;
import com.orion.http.support.HttpMethod;
import com.orion.http.support.HttpUploadPart;
import com.orion.http.useragent.UserAgentGenerators;
import com.orion.utils.Threads;
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
        System.out.println(req.getUseDate());
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

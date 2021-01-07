package com.orion.test.http;

import com.orion.http.apache.ApacheRequests;
import com.orion.http.apache.ApacheResponse;
import com.orion.lang.thread.ExecutorBuilder;
import com.orion.utils.Strings;
import com.orion.utils.Threads;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/4 18:42
 */
public class ApacheTests {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor e = ExecutorBuilder.create().build();
        IntStream.rangeClosed(1, 8).forEach((__) -> {
            e.execute(() -> {
                ApacheResponse apacheResponse = ApacheRequests.get("http://localhost:8080/test/sleep?s=" + Strings.randomChars(3));
                System.out.println(apacheResponse.getBodyString());
            });
        });
        System.out.println("submit");
        Threads.shutdownPool(e, 100, TimeUnit.SECONDS);
        System.out.println(System.currentTimeMillis() - start);
    }

}

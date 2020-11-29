package com.orion.test.http;

import com.orion.http.ok.OkRequests;
import com.orion.http.ok.OkResponse;
import com.orion.lang.thread.ExecutorBuilder;
import com.orion.utils.Strings;
import com.orion.utils.Threads;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/5 16:27
 */
public class OkTests {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor e = ExecutorBuilder.create().build();
        IntStream.rangeClosed(1, 10).parallel().forEach((__) -> {
            e.execute(() -> {
                try (OkResponse apacheResponse = OkRequests.get("http://localhost:8080/test/sleep?s=" + Strings.randomChars(3))) {
                    System.out.println(apacheResponse.getBodyString());
                }
            });
        });
        System.out.println("submit");
        Threads.shutdownPool(e, 100, TimeUnit.SECONDS);
        System.out.println(System.currentTimeMillis() - start);
    }

}

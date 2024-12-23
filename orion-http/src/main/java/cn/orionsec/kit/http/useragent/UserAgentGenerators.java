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
package cn.orionsec.kit.http.useragent;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.random.Randoms;

/**
 * UA 生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 0:06
 */
public class UserAgentGenerators {

    /**
     * 浏览器特性
     */
    private static final String[] BROWSER_FEATURES = {"Mozilla/4.0", "Mozilla/5.0"};

    /**
     * 浏览器特性
     * N: 表示无安全加密
     * I: 表示弱安全加密
     * U: 表示强安全加密
     */
    private static final String[] SYSTEM_GROUP = {"Windows; U; Windows NT 6.1",
            "Windows NT 6.1; rv:2.0.1",
            "Windows NT 6.1; WOW64",
            "Windows NT 10.0; WOW64",
            "Windows NT 10.0; WOW64; x64",
            "iPhone; CPU iPhone OS 11_0 like Mac OS X",
            "iPhone; CPU iPhone OS 13_2_3 like Mac OS X",
            "iPad; CPU OS 8_1_3 like Mac OS X",
            "iPad; CPU OS 11_0 like Mac OS X",
            "Macintosh; Intel Mac OS X 10.6.8; U",
            "Macintosh; Intel Mac OS X 10.6",
            "Macintosh; Intel Mac OS X; rv:10.0",
            "X11; U; Linux x86_64",
            "X11; Linux x86_64; rv:10.0",
            "Android; Mobile; rv:40.0",
            "Android; Tablet; rv:40.0"};

    /**
     * 渲染引擎
     * Gecko WebKit KHTML
     */
    private static final String[] BROWSER_ENGINE = {"AppleWebKit/537.36 (KHTML, like Gecko)",
            "AppleWebKit/600.1.4 (KHTML, like Gecko)",
            "AppleWebKit/533.20.25 (KHTML, like Gecko)",
            "Gecko/20100101",
            "Gecko/20130331",
            "Gecko/14.0",
            "Gecko/40.0",
            "Gecko/44.0"};

    /**
     * 浏览器版本
     */
    private static final String[] BROWSER_VERSION = {"Chrome/78.0.3904.108",
            "Chrome/46.0.2486.0",
            "Chrome/86.0.4240.111",
            "Chrome/88.0.4324.182",
            "Safari/537.36",
            "Safari/604.1",
            "Safari/7534.48.3",
            "Version/10.1",
            "Version/11.0",
            "Version/13.0.3",
            "Mobile/15A5341f",
            "Mobile/15E148",
            "Edge/88.0.705.81",
            "Edge/13.10586",
            "Mobile Safari/533.1",
            "Firefox/21.0",
            "Firefox/40.0",
            "Firefox/44.0",
            "Firefox/72.0"};

    private static final int FIRE_FOX_VERSION_NUM = 4;

    private static final String GECKO = "Gecko";

    private UserAgentGenerators() {
    }

    /**
     * 生成UA
     *
     * @return UA
     */
    public static String generator() {
        StringBuilder ua = new StringBuilder();
        // feat
        int feat = Randoms.randomInt(0, 10);
        if (feat == 0) {
            ua.append(BROWSER_FEATURES[0]);
        } else {
            ua.append(BROWSER_FEATURES[1]);
        }
        // group
        ua.append(Strings.SPACE)
                .append("(")
                .append(Arrays1.random(SYSTEM_GROUP))
                .append(")")
                .append(Strings.SPACE);
        // engine
        String engine = Arrays1.random(BROWSER_ENGINE);
        ua.append(engine).append(Strings.SPACE);
        if (engine.startsWith(GECKO)) {
            ua.append(BROWSER_VERSION[Randoms.randomInt(BROWSER_VERSION.length - FIRE_FOX_VERSION_NUM, BROWSER_VERSION.length)]);
            return ua.toString();
        }
        // version
        int versionNum = Randoms.randomInt(2);
        if (versionNum == 0) {
            // 1个
            ua.append(BROWSER_VERSION[Randoms.randomInt(0, BROWSER_VERSION.length - FIRE_FOX_VERSION_NUM)]);
        } else if (versionNum == 1) {
            // 2个
            ua.append(BROWSER_VERSION[Randoms.randomInt(0, BROWSER_VERSION.length - FIRE_FOX_VERSION_NUM)])
                    .append(Strings.SPACE)
                    .append(BROWSER_VERSION[Randoms.randomInt(0, BROWSER_VERSION.length - FIRE_FOX_VERSION_NUM)]);
        }
        return ua.toString();
    }

}

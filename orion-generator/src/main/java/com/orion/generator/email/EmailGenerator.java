/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.generator.email;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.random.Randoms;

/**
 * 邮箱生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 11:52
 */
public class EmailGenerator {

    private static final String[] REAL_EMAIL_SUFFIX;

    private static final String[] DOMAIN_SUFFIX;

    private EmailGenerator() {
    }

    static {
        REAL_EMAIL_SUFFIX = new String[]{
                "qq.com", "163.com", "139.com",
                "aliyun.com", "Outlook.com", "gmail.com",
                "icloud.com", "yahoo.com", "yeah.com",
                "ymail.com", "126.com", "189.com",
                "hotmail.com", "sina.com", "live.com",
        };

        DOMAIN_SUFFIX = new String[]{
                "com", "cn", "com.cn", "net", "org", "io", "co", "cc", "top"
        };
    }

    public static String generatorEmail() {
        return Randoms.randomBoolean(5) ? generatorRandomEmail() : generatorRealEmail();
    }

    /**
     * 随机生成一个真实的邮箱地址
     *
     * @return 邮箱
     */
    public static String generatorRealEmail() {
        return generatorContent()
                .append('@')
                .append(Arrays1.random(REAL_EMAIL_SUFFIX))
                .toString();
    }

    /**
     * 随机生成一个邮箱地址
     *
     * @return 邮箱
     */
    public static String generatorRandomEmail() {
        return generatorContent()
                .append('@')
                .append(Randoms.randomLetter(Randoms.randomInt(3, 6)).toLowerCase())
                .append('.')
                .append(Arrays1.random(DOMAIN_SUFFIX))
                .toString();
    }

    /**
     * 随机获取一个邮箱主体
     *
     * @return 邮箱
     */
    private static StringBuilder generatorContent() {
        StringBuilder sb = new StringBuilder();
        int i = Randoms.randomInt(4);
        if (i == 0) {
            sb.append(Randoms.randomNumber(Randoms.randomInt(6, 15)));
        } else if (i == 1) {
            sb.append(Randoms.randomLetter(Randoms.randomInt(6, 15)));
        } else if (i == 2) {
            sb.append(Randoms.randomAscii(Randoms.randomInt(6, 15)));
        } else {
            sb.append(Randoms.randomLetter(Randoms.randomInt(1, 4)))
                    .append(Randoms.randomNumber(Randoms.randomInt(6, 15)));
        }
        return sb;
    }

}

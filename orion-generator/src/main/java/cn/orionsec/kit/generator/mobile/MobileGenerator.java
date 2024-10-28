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
package cn.orionsec.kit.generator.mobile;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.random.Randoms;

/**
 * 手机号生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 12:19
 */
public class MobileGenerator {

    private static final int[] MOBILE_PREFIX;

    private MobileGenerator() {
    }

    static {
        MOBILE_PREFIX = new int[]{
                130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                145, 147,
                150, 151, 152, 153, 155, 156, 157, 158, 159,
                170, 176, 177, 178,
                180, 181, 182, 183, 184, 185, 186, 187, 188, 189
        };
    }

    /**
     * 随机获取前缀
     *
     * @return 前缀
     */
    public static String getMobilePrefix() {
        return Arrays1.random(MOBILE_PREFIX) + Strings.EMPTY;
    }

    /**
     * 生成手机号
     *
     * @return mobile
     */
    public static String generateMobile() {
        return generateMobile(getMobilePrefix());
    }

    /**
     * 生成手机号
     *
     * @param prefix 3位前缀
     * @return mobile
     */
    public static String generateMobile(String prefix) {
        return prefix + Strings.leftPad(Strings.EMPTY + Randoms.RANDOM.nextInt(99999999), 8, "0");
    }

}

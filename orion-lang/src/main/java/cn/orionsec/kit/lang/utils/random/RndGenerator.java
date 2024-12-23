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
package cn.orionsec.kit.lang.utils.random;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Systems;
import cn.orionsec.kit.lang.utils.Valid;

import java.util.regex.Pattern;

/**
 * 伪随机数发生器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/23 18:38
 */
public class RndGenerator {

    /**
     * 种子
     */
    private long seed;

    /**
     * 原始种子
     */
    private final long originSeed;

    /**
     * 生成的位数
     */
    private final int bit;

    /**
     * 检测重复 正则
     */
    private final Pattern reg;

    public RndGenerator() {
        this((System.currentTimeMillis() >> 16 | Systems.getProcessCode()) * 32, 4);
    }

    public RndGenerator(long seed) {
        this(seed, 4);
    }

    public RndGenerator(int bit) {
        this((System.currentTimeMillis() >> 16 | Systems.getProcessCode()) * 32, bit);
    }

    public RndGenerator(long seed, int bit) {
        Valid.gte((seed + Strings.EMPTY).length(), bit, "bit len must less than or equal seed len");
        this.seed = seed;
        this.originSeed = seed;
        this.bit = bit;
        this.reg = Pattern.compile("^1[0]{" + (bit - 1) + ",}$");
    }

    public long next() {
        String bs = seed + Strings.EMPTY;
        this.seed *= seed;
        String as = seed + Strings.EMPTY;
        int el = (as.length() - bs.length()) >> 1;
        char c = as.charAt(el);
        String ns;
        if (c == '0') {
            ns = "1" + as.substring(el + 1, bs.length() + el);
        } else {
            ns = as.substring(el, bs.length() + el);
        }
        this.seed = Long.parseLong(ns);
        if (reg.matcher(ns).matches()) {
            ns = originSeed + Strings.EMPTY;
            this.seed = originSeed;
        }
        int i = (bs.length() - bit) >> 1;
        return Long.parseLong(ns.substring(i, bit + i));
    }

}

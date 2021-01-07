package com.orion.utils.random;

import com.orion.utils.Strings;
import com.orion.utils.Systems;
import com.orion.utils.Valid;

import java.util.regex.Pattern;

/**
 * 伪随机数发生器
 *
 * @author ljh15
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
    private long originSeed;

    /**
     * 生成的位数
     */
    private int bit;

    /**
     * 检测重复 正则
     */
    private Pattern rep;

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
        this.rep = Pattern.compile("^1[0]{" + (bit - 1) + ",}$");
    }

    public long next() {
        String bs = seed + Strings.EMPTY;
        seed *= seed;
        String as = seed + Strings.EMPTY;
        int el = (as.length() - bs.length()) >> 1;
        char c = as.charAt(el);
        String ns;
        if (c == '0') {
            ns = "1" + as.substring(el + 1, bs.length() + el);
        } else {
            ns = as.substring(el, bs.length() + el);
        }
        seed = Long.parseLong(ns);
        if (rep.matcher(ns).matches()) {
            ns = originSeed + Strings.EMPTY;
            seed = originSeed;
        }
        int i = (bs.length() - bit) >> 1;
        return Long.parseLong(ns.substring(i, bit + i));
    }

}

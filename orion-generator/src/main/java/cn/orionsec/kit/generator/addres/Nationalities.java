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
package cn.orionsec.kit.generator.addres;

import cn.orionsec.kit.lang.define.collect.WeightRandomMap;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * 民族区域划分
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/8 11:19
 */
public class Nationalities {

    private static final String DEFAULT_NATION = "汉族";

    /**
     * 少数民族比例
     */
    private static final double MINORITY_RATE = 0.066D;

    /**
     * 少数民族省份分布
     * key: 省
     * value: 民族权重
     */
    private static final Map<Integer, WeightRandomMap<String>> MINORITY_PROVINCE;

    /**
     * 少数民族分布
     */
    private static final WeightRandomMap<String> MINORITY;

    private Nationalities() {
    }

    static {
        TreeMap<Integer, WeightRandomMap<String>> tempMinorityProvince = new TreeMap<>();
        tempMinorityProvince.put(11, new WeightRandomMap<String>() {{
            put("回族", 10);
            put("满族", 7);
        }});
        tempMinorityProvince.put(12, new WeightRandomMap<String>() {{
            put("回族", 10);
        }});
        tempMinorityProvince.put(13, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 8);
            put("满族", 7);
        }});
        tempMinorityProvince.put(15, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("满族", 7);
            put("达斡尔族", 2);
            put("鄂温克族", 1);
            put("鄂伦春族", 1);
        }});
        tempMinorityProvince.put(21, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 8);
            put("朝鲜族", 7);
            put("满族", 7);
            put("锡伯族", 1);
        }});
        tempMinorityProvince.put(22, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 8);
            put("朝鲜族", 7);
            put("满族", 7);
            put("锡伯族", 1);
        }});
        tempMinorityProvince.put(23, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 8);
            put("朝鲜族", 7);
            put("满族", 7);
            put("柯尔克孜族", 2);
            put("达斡尔族", 1);
            put("鄂温克族", 1);
            put("鄂伦春族", 1);
            put("赫哲族", 1);
        }});
        tempMinorityProvince.put(33, new WeightRandomMap<String>() {{
            put("畲族", 2);
        }});
        tempMinorityProvince.put(34, new WeightRandomMap<String>() {{
            put("回族", 8);
            put("畲族", 2);
        }});
        tempMinorityProvince.put(35, new WeightRandomMap<String>() {{
            put("畲族", 2);
            put("高山族", 2);
        }});
        tempMinorityProvince.put(36, new WeightRandomMap<String>() {{
            put("畲族", 2);
        }});
        tempMinorityProvince.put(37, new WeightRandomMap<String>() {{
            put("回族", 10);
        }});
        tempMinorityProvince.put(41, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 10);
        }});
        tempMinorityProvince.put(42, new WeightRandomMap<String>() {{
            put("苗族", 9);
            put("土家族", 7);
        }});
        tempMinorityProvince.put(43, new WeightRandomMap<String>() {{
            put("维吾尔族", 9);
            put("苗族", 9);
            put("侗族", 7);
            put("瑶族", 7);
            put("土家族", 7);
        }});
        tempMinorityProvince.put(44, new WeightRandomMap<String>() {{
            put("苗族", 9);
            put("壮族", 9);
            put("瑶族", 7);
            put("畲族", 2);
        }});
        tempMinorityProvince.put(45, new WeightRandomMap<String>() {{
            put("苗族", 9);
            put("彝族", 9);
            put("壮族", 9);
            put("侗族", 7);
            put("瑶族", 7);
            put("水族", 2);
            put("仫佬族", 1);
            put("毛难族", 1);
            put("仡佬族", 1);
            put("京族", 1);
        }});
        tempMinorityProvince.put(46, new WeightRandomMap<String>() {{
            put("黎族", 5);
        }});
        tempMinorityProvince.put(51, new WeightRandomMap<String>() {{
            put("藏族", 10);
            put("苗族", 9);
            put("彝族", 9);
            put("瑶族", 7);
            put("土家族", 7);
            put("傈僳族", 5);
            put("纳西族", 4);
            put("羌族", 3);
        }});
        tempMinorityProvince.put(52, new WeightRandomMap<String>() {{
            put("苗族", 9);
            put("彝族", 9);
            put("壮族", 9);
            put("布依族", 7);
            put("侗族", 7);
            put("瑶族", 7);
            put("白族", 7);
            put("水族", 2);
            put("仡佬族", 1);
        }});
        tempMinorityProvince.put(53, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 10);
            put("藏族", 10);
            put("苗族", 9);
            put("彝族", 9);
            put("壮族", 9);
            put("瑶族", 7);
            put("白族", 7);
            put("哈尼族", 6);
            put("傣族", 6);
            put("傈僳族", 5);
            put("佤族", 5);
            put("拉祜族", 5);
            put("纳西族", 4);
            put("景颇族", 4);
            put("布朗族", 1);
            put("仡佬族", 1);
            put("阿昌族", 1);
            put("普米族", 1);
            put("怒族", 1);
            put("德昂族", 1);
            put("独龙族", 1);
            put("基诺族", 1);
        }});
        tempMinorityProvince.put(54, new WeightRandomMap<String>() {{
            put("藏族", 10);
            put("门巴族", 1);
            put("珞巴族", 1);
        }});
        tempMinorityProvince.put(61, new WeightRandomMap<String>() {{
            put("回族", 10);
        }});
        tempMinorityProvince.put(62, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 10);
            put("藏族", 10);
            put("哈萨克族", 7);
            put("东乡族", 4);
            put("土族", 3);
            put("撒拉族", 2);
            put("保安族", 1);
            put("裕固族", 1);
        }});
        tempMinorityProvince.put(63, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 10);
            put("藏族", 10);
            put("土族", 3);
            put("撒拉族", 2);
        }});
        tempMinorityProvince.put(64, new WeightRandomMap<String>() {{
            put("回族", 10);
        }});
        tempMinorityProvince.put(65, new WeightRandomMap<String>() {{
            put("蒙古族", 10);
            put("回族", 10);
            put("维吾尔族", 9);
            put("哈萨克族", 7);
            put("东乡族", 4);
            put("柯尔克孜族", 2);
            put("达斡尔族", 2);
            put("锡伯族", 1);
            put("塔吉克族", 1);
            put("乌孜别克族", 1);
            put("俄罗斯族", 1);
            put("塔塔尔族", 1);
        }});
        MINORITY_PROVINCE = Collections.unmodifiableMap(tempMinorityProvince);
    }

    static {
        MINORITY = new WeightRandomMap<>();
        MINORITY.put("回族", 10);
        MINORITY.put("藏族", 10);
        MINORITY.put("蒙古族", 10);
        MINORITY.put("苗族", 9);
        MINORITY.put("维吾尔族", 9);
        MINORITY.put("壮族", 9);
        MINORITY.put("彝族", 9);
        MINORITY.put("朝鲜族", 7);
        MINORITY.put("满族", 7);
        MINORITY.put("土家族", 7);
        MINORITY.put("侗族", 7);
        MINORITY.put("瑶族", 7);
        MINORITY.put("布依族", 7);
        MINORITY.put("白族", 7);
        MINORITY.put("哈萨克族", 7);
        MINORITY.put("哈尼族", 6);
        MINORITY.put("傣族", 6);
        MINORITY.put("傈僳族", 5);
        MINORITY.put("黎族", 5);
        MINORITY.put("佤族", 5);
        MINORITY.put("拉祜族", 5);
        MINORITY.put("纳西族", 4);
        MINORITY.put("景颇族", 4);
        MINORITY.put("东乡族", 4);
        MINORITY.put("土族", 3);
        MINORITY.put("羌族", 3);
        MINORITY.put("撒拉族", 2);
        MINORITY.put("水族", 2);
        MINORITY.put("达斡尔族", 2);
        MINORITY.put("柯尔克孜族", 2);
        MINORITY.put("畲族", 2);
        MINORITY.put("高山族", 2);
        MINORITY.put("鄂温克族", 1);
        MINORITY.put("鄂伦春族", 1);
        MINORITY.put("锡伯族", 1);
        MINORITY.put("赫哲族", 1);
        MINORITY.put("仫佬族", 1);
        MINORITY.put("毛难族", 1);
        MINORITY.put("仡佬族", 1);
        MINORITY.put("京族", 1);
        MINORITY.put("布朗族", 1);
        MINORITY.put("阿昌族", 1);
        MINORITY.put("普米族", 1);
        MINORITY.put("怒族", 1);
        MINORITY.put("德昂族", 1);
        MINORITY.put("独龙族", 1);
        MINORITY.put("基诺族", 1);
        MINORITY.put("门巴族", 1);
        MINORITY.put("珞巴族", 1);
        MINORITY.put("保安族", 1);
        MINORITY.put("裕固族", 1);
        MINORITY.put("塔吉克族", 1);
        MINORITY.put("乌孜别克族", 1);
        MINORITY.put("俄罗斯族", 1);
        MINORITY.put("塔塔尔族", 1);
    }

    /**
     * 随机获取一个民族
     *
     * @param provinceCode 省编码
     * @return 民族
     */
    public static String getNation(Integer provinceCode) {
        WeightRandomMap<String> nations = MINORITY_PROVINCE.get(provinceCode);
        if (nations == null) {
            return DEFAULT_NATION;
        }
        if (Randoms.randomBoolean(MINORITY_RATE)) {
            return nations.next();
        }
        return DEFAULT_NATION;
    }

    /**
     * 随机获取一个少数民族
     *
     * @return 少数民族
     */
    public static String getMinority() {
        return MINORITY.next();
    }

}

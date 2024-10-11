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
package com.orion.generator.education;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.random.Randoms;

/**
 * 高校名称生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 15:25
 */
public class UniversityGenerator {

    private static final String DEFAULT;

    private static final String[] UNIVERSITY_PREFIX;

    private static final String[] UNIVERSITY_HIGH_SUFFIX;

    private static final String[] UNIVERSITY_LOW_SUFFIX;

    private static final String[] UNIVERSITY_HIGH_EDUCATION;

    private static final String[] UNIVERSITY_LOW_EDUCATION;

    private UniversityGenerator() {
    }

    static {
        DEFAULT = "其他";

        UNIVERSITY_PREFIX = new String[]{
                "中国", "首都", "东方", "南方", "西方", "北方", "东南", "东北", "西南", "西北",
                "北京", "上海", "天津", "重庆", "黑龙江", "吉林", "辽宁", "内蒙古", "河北", "新疆",
                "甘肃", "青海", "陕西", "宁夏", "河南", "山东", "山西", "安徽", "湖北", "湖南", "江苏",
                "四川", "贵州", "云南", "广西", "西藏", "浙江", "江西", "广东", "福建", "海南", "西安",
                "厦门", "香港", "澳门"
        };

        UNIVERSITY_HIGH_SUFFIX = new String[]{
                "理工大学", "航天大学", "艺术大学", "体育大学", "医药大学", "化工大学",
                "师范大学", "交通大学", "传媒大学", "医药大学", "政法大学", "财经大学",
                "经贸大学", "农业大学", "科技大学", "大学"
        };

        UNIVERSITY_LOW_SUFFIX = new String[]{
                "职业技术学院", "工程职业学院", "职业学院", "高等专科学校", "职业大学", "高等专科学校"
        };

        UNIVERSITY_HIGH_EDUCATION = new String[]{
                "本科", "硕士", "博士", "研究生"
        };

        UNIVERSITY_LOW_EDUCATION = new String[]{
                "专科"
        };
    }

    /**
     * 生成大学名字
     *
     * @param education 学历
     * @return 名字
     */
    public static String generatorUniversity(String education) {
        if (Arrays1.contains(UNIVERSITY_LOW_EDUCATION, education)) {
            return generatorUniversityLow();
        } else if (Arrays1.contains(UNIVERSITY_HIGH_EDUCATION, education)) {
            return generatorUniversityHigh();
        }
        if (DEFAULT.equals(education) && Randoms.randomBoolean(3)) {
            return generatorUniversity();
        }
        return Strings.EMPTY;
    }

    /**
     * 生成大学名字
     *
     * @return 大学名字
     */
    public static String generatorUniversity() {
        return Arrays1.random(UNIVERSITY_PREFIX) + Arrays1.random(Randoms.randomBoolean(3) ? UNIVERSITY_LOW_SUFFIX : UNIVERSITY_HIGH_SUFFIX);
    }

    /**
     * 生成大学名字
     *
     * @return 大学名字
     */
    public static String generatorUniversityHigh() {
        return Arrays1.random(UNIVERSITY_PREFIX) + Arrays1.random(UNIVERSITY_HIGH_SUFFIX);
    }

    /**
     * 生成大专名字
     *
     * @return 大专名字
     */
    public static String generatorUniversityLow() {
        return Arrays1.random(UNIVERSITY_PREFIX) + Arrays1.random(UNIVERSITY_LOW_SUFFIX);
    }

}

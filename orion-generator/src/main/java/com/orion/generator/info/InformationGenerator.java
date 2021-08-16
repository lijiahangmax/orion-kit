package com.orion.generator.info;

import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;

/**
 * 信息生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 17:13
 */
public class InformationGenerator {

    private static final String[] EDUCATION;

    private static final String[] INDUSTRY;

    private InformationGenerator() {
    }

    static {
        EDUCATION = new String[]{"其他", "小学", "初中", "高中", "职高", "中技", "中专", "专科", "本科", "硕士", "博士", "研究生"};
    }

    static {
        INDUSTRY = new String[]{"计算机", "互联网", "通信", "生产", "工艺", "制造", "医疗", "护理", "制药", "金融", "银行", "投资", "保险", "商业", "服务业", "个体经营", "文化", "广告", "传媒", "娱乐", "艺术", "表演", "律师", "法务", "培训", "教育", "公务员", "行政", "事业单位", "模特", "学生", "其他"};
    }

    /**
     * 随机生成一个学历
     *
     * @param age age
     * @return 学历
     */
    public static String generatorEducation(int age) {
        if (age <= 3) {
            return Strings.EMPTY;
        } else if (age <= 9) {
            return EDUCATION[1];
        } else if (age <= 14) {
            return EDUCATION[2];
        } else if (age <= 18) {
            return EDUCATION[Randoms.randomInt(3, 6)];
        } else if (age <= 24) {
            if (Randoms.randomBoolean(3)) {
                return EDUCATION[Randoms.randomInt(9)];
            } else {
                // 跳过小学 初中
                return EDUCATION[Randoms.randomInt(3, 9)];
            }
        } else {
            if (Randoms.randomBoolean(3)) {
                return Arrays1.random(EDUCATION);
            } else {
                // 跳过小学 初中
                return EDUCATION[Randoms.randomInt(3, EDUCATION.length)];
            }
        }
    }

    /**
     * 随机生成一个学历
     *
     * @return 学历
     */
    public static String generatorEducation() {
        return Arrays1.random(EDUCATION);
    }

    /**
     * 随机生成一个行业
     *
     * @param age age
     * @return 行业
     */
    public static String generatorIndustry(int age) {
        if (age <= 18) {
            return Strings.EMPTY;
        }
        return Arrays1.random(INDUSTRY);
    }

    /**
     * 随机生成一个行业
     *
     * @return 行业
     */
    public static String generatorIndustry() {
        return Arrays1.random(INDUSTRY);
    }

}

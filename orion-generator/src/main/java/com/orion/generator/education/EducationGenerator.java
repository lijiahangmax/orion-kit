package com.orion.generator.education;

import com.orion.lang.collect.WeightRandomMap;
import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;

/**
 * 学历生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 17:13
 */
public class EducationGenerator {

    private static final String[] EDUCATION;

    private static final WeightRandomMap<String> EDUCATION_WEIGHT;

    private EducationGenerator() {
    }

    static {
        EDUCATION = new String[]{"其他", "小学", "初中", "高中", "职高", "中技", "中专", "专科", "本科", "硕士", "博士", "研究生"};
    }

    static {
        EDUCATION_WEIGHT = new WeightRandomMap<>();
        EDUCATION_WEIGHT.put(EDUCATION[0], 5);
        EDUCATION_WEIGHT.put(EDUCATION[1], 20);
        EDUCATION_WEIGHT.put(EDUCATION[2], 40);
        EDUCATION_WEIGHT.put(EDUCATION[3], 30);
        EDUCATION_WEIGHT.put(EDUCATION[4], 20);
        EDUCATION_WEIGHT.put(EDUCATION[5], 10);
        EDUCATION_WEIGHT.put(EDUCATION[6], 10);
        EDUCATION_WEIGHT.put(EDUCATION[7], 15);
        EDUCATION_WEIGHT.put(EDUCATION[8], 10);
        EDUCATION_WEIGHT.put(EDUCATION[9], 5);
        EDUCATION_WEIGHT.put(EDUCATION[10], 5);
        EDUCATION_WEIGHT.put(EDUCATION[11], 5);
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
        return EDUCATION_WEIGHT.next();
    }

}

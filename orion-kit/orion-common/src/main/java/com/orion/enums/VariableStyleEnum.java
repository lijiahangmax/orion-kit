package com.orion.enums;

import com.orion.utils.Strings;

/**
 * 变量风格枚举对象
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/22 21:53
 */
public enum VariableStyleEnum {

    /**
     * 小驼峰命名法
     */
    SMALL_HUMP(1),

    /**
     * 大驼峰命名法
     */
    BIG_HUMP(2),

    /**
     * 蛇形命名法
     */
    SERPENTINE(3),

    /**
     * 脊柱命名法
     */
    THE_SPINE(4);

    /**
     * 风格类型
     */
    int styleType;

    /**
     * 脊柱分隔符
     */
    private static final String SPINE_TOKENIZER = "-";

    /**
     * 蛇形分隔符
     */
    private static final String SERPENTINE_TOKENIZER = "_";

    VariableStyleEnum(int styleType) {
        this.styleType = styleType;
    }

    public int getStyleType() {
        return styleType;
    }

    public VariableStyleEnum setStyleType(int styleType) {
        this.styleType = styleType;
        return this;
    }

    /**
     * 将类型转化为枚举
     *
     * @param type 类型
     * @return 类型枚举
     */
    public static VariableStyleEnum convert(Integer type) {
        switch (type) {
            case 1:
                return VariableStyleEnum.SMALL_HUMP;
            case 2:
                return VariableStyleEnum.BIG_HUMP;
            case 3:
                return VariableStyleEnum.SERPENTINE;
            case 4:
                return VariableStyleEnum.THE_SPINE;
            default:
                return VariableStyleEnum.SMALL_HUMP;
        }

    }

    /**
     * 获取变量的命名风格
     *
     * @param variable 变量
     * @return 代码风格
     */
    public static VariableStyleEnum getType(String variable) {
        if (Strings.isBlank(variable)) {
            return SMALL_HUMP;
        }
        if (variable.contains(SERPENTINE_TOKENIZER)) {
            return SERPENTINE;
        } else if (variable.contains(SPINE_TOKENIZER)) {
            return THE_SPINE;
        } else {
            char f = variable.charAt(0);
            if (f >= 65 && f <= 90) {
                return BIG_HUMP;
            } else {
                return SMALL_HUMP;
            }
        }
    }

}

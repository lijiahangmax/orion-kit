package com.orion.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 变量风格枚举对象
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/22 21:53
 */
public enum VariableStyles {

    /**
     * 小驼峰命名法
     */
    SMALL_HUMP {
        @Override
        public String toSpine(String variable) {
            return styleConvert(variable, "-");
        }

        @Override
        public String toSerpentine(String variable) {
            return styleConvert(variable, "_");
        }

        @Override
        public String toBigHump(String variable) {
            return styleConvert(variable, 0);
        }

        @Override
        public String toSmallHump(String variable) {
            return variable;
        }
    },

    /**
     * 大驼峰命名法
     */
    BIG_HUMP {
        @Override
        public String toSpine(String variable) {
            return styleConvert(variable, "-");
        }

        @Override
        public String toSerpentine(String variable) {
            return styleConvert(variable, "_");
        }

        @Override
        public String toBigHump(String variable) {
            return variable;
        }

        @Override
        public String toSmallHump(String variable) {
            return styleConvert(variable, 1);
        }
    },

    /**
     * 蛇形命名法
     */
    SERPENTINE {
        @Override
        public String toSpine(String variable) {
            return styleConvert(variable, "_", "-");
        }

        @Override
        public String toSerpentine(String variable) {
            return variable;
        }

        @Override
        public String toBigHump(String variable) {
            return styleConvert(variable, false);
        }

        @Override
        public String toSmallHump(String variable) {
            return styleConvert(variable, true);
        }
    },

    /**
     * 脊柱命名法
     */
    SPINE {
        @Override
        public String toSpine(String variable) {
            return variable;
        }

        @Override
        public String toSerpentine(String variable) {
            return styleConvert(variable, "-", "_");
        }

        @Override
        public String toBigHump(String variable) {
            return styleConvert(variable, false);
        }

        @Override
        public String toSmallHump(String variable) {
            return styleConvert(variable, true);
        }
    };

    /**
     * 脊柱分隔符
     */
    private static final String SPINE_TOKENIZER = "-";

    /**
     * 蛇形分隔符
     */
    private static final String SERPENTINE_TOKENIZER = "_";

    /**
     * 大驼峰正则
     */
    private static final Pattern BIG_HUMP_PATTERN = Pattern.compile("[A-Z]([a-z\\d]+)?");

    /**
     * 蛇形正则
     */
    private static final Pattern SERPENTINE_PATTERN = Pattern.compile("([A-Za-z\\d]+)(_)?");

    /**
     * 脊柱正则
     */
    private static final Pattern SPINE_PATTERN = Pattern.compile("([A-Za-z\\d]+)(-)?");

    /**
     * 转化变量命名风格
     *
     * @param variable 变量
     * @param e        命名风格
     * @return 变量
     */
    public static String convert(String variable, VariableStyles e) {
        if (Strings.isBlank(variable)) {
            return variable;
        }
        VariableStyles styleType = VariableStyles.getType(variable);
        if (e == styleType) {
            return variable;
        }
        switch (styleType) {
            case SMALL_HUMP:
                switch (e) {
                    case BIG_HUMP:
                        return styleConvert(variable, 0);
                    case SERPENTINE:
                        return styleConvert(variable, "_");
                    case SPINE:
                        return styleConvert(variable, "-");
                    default:
                        return variable;
                }
            case BIG_HUMP:
                switch (e) {
                    case SMALL_HUMP:
                        return styleConvert(variable, 1);
                    case SERPENTINE:
                        return styleConvert(variable, "_");
                    case SPINE:
                        return styleConvert(variable, "-");
                    default:
                        return variable;
                }
            case SERPENTINE:
                switch (e) {
                    case SMALL_HUMP:
                        return styleConvert(variable, true);
                    case BIG_HUMP:
                        return styleConvert(variable, false);
                    case SPINE:
                        return styleConvert(variable, "_", "-");
                    default:
                        return variable;
                }
            case SPINE:
                switch (e) {
                    case SMALL_HUMP:
                        return styleConvert(variable, true);
                    case BIG_HUMP:
                        return styleConvert(variable, false);
                    case SERPENTINE:
                        return styleConvert(variable, "-", "_");
                    default:
                        return variable;
                }
            default:
                return variable;
        }
    }

    // -------------------- 转化格式私有方法 --------------------

    private static String styleConvert(String variable, String before, String after) {
        return variable.toLowerCase().replaceAll(before, after);
    }

    private static String styleConvert(String variable, String tokenizer) {
        variable = String.valueOf(variable.charAt(0)).toUpperCase().concat(variable.substring(1));
        StringBuilder sb = new StringBuilder();
        Matcher matcher = BIG_HUMP_PATTERN.matcher(variable);
        while (matcher.find()) {
            sb.append(matcher.group().toLowerCase())
                    .append(matcher.end() == variable.length() ? Strings.EMPTY : tokenizer);
        }
        return sb.toString();
    }

    private static String styleConvert(String variable, boolean small) {
        String tokenizer;
        Pattern pattern;
        if (variable.contains(SPINE_TOKENIZER)) {
            tokenizer = SPINE_TOKENIZER;
            pattern = SPINE_PATTERN;
        } else {
            tokenizer = SERPENTINE_TOKENIZER;
            pattern = SERPENTINE_PATTERN;
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pattern.matcher(variable);
        int i = 0;
        while (matcher.find()) {
            String word = matcher.group();
            if (++i == 1 && small) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }
            int index = word.lastIndexOf(tokenizer);
            if (index > 0) {
                sb.append(word, 1, index);
            } else {
                sb.append(word.substring(1));
            }
        }
        return sb.toString();
    }

    private static String styleConvert(String variable, int t) {
        if (t == 0) {
            return Character.toUpperCase(variable.charAt(0)) + variable.substring(1);
        } else {
            return Character.toLowerCase(variable.charAt(0)) + variable.substring(1);
        }
    }

    /**
     * 获取变量的命名风格
     *
     * @param variable 变量
     * @return 代码风格
     */
    private static VariableStyles getType(String variable) {
        if (Strings.isBlank(variable)) {
            return SMALL_HUMP;
        }
        if (variable.contains(SERPENTINE_TOKENIZER)) {
            return SERPENTINE;
        } else if (variable.contains(SPINE_TOKENIZER)) {
            return SPINE;
        } else {
            char f = variable.charAt(0);
            if (f >= 65 && f <= 90) {
                return BIG_HUMP;
            } else {
                return SMALL_HUMP;
            }
        }
    }

    /**
     * 转为小驼峰命名
     *
     * @param variable 变量
     * @return ignore
     */
    public abstract String toSmallHump(String variable);

    /**
     * 转为大驼峰命名
     *
     * @param variable 变量
     * @return ignore
     */
    public abstract String toBigHump(String variable);

    /**
     * 转为蛇形命名
     *
     * @param variable 变量
     * @return ignore
     */
    public abstract String toSerpentine(String variable);

    /**
     * 转为脊柱命名
     *
     * @param variable 变量
     * @return ignore
     */
    public abstract String toSpine(String variable);

}


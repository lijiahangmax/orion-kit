package com.orion.lang.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对金额的处理
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/3 10:22
 */
public class Moneys {

    private static final char[] CN_UPPER_NUMBER = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final char[] CN_UPPER_UNIT = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '兆', '拾', '佰', '仟'};
    private static final char[] CN_UNIT = {'分', '角', '元', '拾', '佰', '仟', '万', '亿', '兆'};
    private static final char CN_FULL = '整';
    private static final char CN_NEGATIVE = '负';
    private static final String CN_ZERO_FULL = "零元整";

    private Moneys() {
    }

    /**
     * BigDecimal 转 大写金额
     *
     * @param m BigDecimal 如果有精度, 则不能超过2位, 否则会抛出异常
     * @return 大写金额
     */
    public static String toCurrency(BigDecimal m) {
        StringBuilder sb = new StringBuilder();
        int minus = m.signum();
        if (minus == 0) {
            return CN_ZERO_FULL;
        }
        long number = m.movePointRight(2).setScale(0, RoundingMode.UNNECESSARY).abs().longValue();
        long scale = number % 100;
        int numUnit;
        int numIndex = 0;
        boolean getZero = false;
        if (scale <= 0) {
            numIndex = 2;
            number /= 100;
            getZero = true;
        }
        if ((scale > 0) && (scale % 10 == 0)) {
            numIndex = 1;
            number /= 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (number > 0) {
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!getZero) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    sb.insert(0, CN_UPPER_UNIT[numIndex]);
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_UNIT[numIndex]);
                }
                getZero = true;
            }
            number = number / 10;
            ++numIndex;
        }
        if (minus == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        if (scale <= 0) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    /**
     * 大写金额 转 BigDecimal
     *
     * @param m 大写金额
     * @return 精度为2的BigDecimal
     */
    public static BigDecimal toDecimal(String m) {
        boolean negate = false;
        char[] chars = m.toCharArray();
        BigDecimal res = BigDecimal.ZERO;
        BigDecimal t = BigDecimal.ZERO;
        BigDecimal tmp = BigDecimal.ZERO;
        boolean lastIsUnit = true;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i == 0 && c == CN_NEGATIVE) {
                negate = true;
                continue;
            }
            boolean isUnit = true;
            for (int j = 0; j < CN_UPPER_NUMBER.length; j++) {
                if (c == CN_UPPER_NUMBER[j]) {
                    tmp = BigDecimal.valueOf(j);
                    isUnit = false;
                    lastIsUnit = false;
                    break;
                }
            }
            if (isUnit) {
                if (lastIsUnit) {
                    tmp = BigDecimal.ZERO;
                }
                for (int j = 0; j < CN_UNIT.length; j++) {
                    if (c == CN_UNIT[j]) {
                        switch (j) {
                            case 0:
                                t = t.add(tmp.multiply(BigDecimal.valueOf(0.01)));
                                tmp = BigDecimal.ZERO;
                                break;
                            case 1:
                                t = t.add(tmp.multiply(BigDecimal.valueOf(0.1)));
                                tmp = BigDecimal.ZERO;
                                break;
                            case 2:
                                t = t.add(tmp);
                                tmp = BigDecimal.ZERO;
                                break;
                            case 3:
                                t = t.add(tmp.multiply(BigDecimal.valueOf(10)));
                                break;
                            case 4:
                                t = t.add(tmp.multiply(BigDecimal.valueOf(100)));
                                break;
                            case 5:
                                t = t.add(tmp.multiply(BigDecimal.valueOf(1000)));
                                break;
                            case 6:
                                res = res.add(t.add(tmp).multiply(BigDecimal.valueOf(10000)));
                                t = BigDecimal.ZERO;
                                tmp = BigDecimal.ZERO;
                                break;
                            case 7:
                                res = res.add(t.add(tmp).multiply(BigDecimal.valueOf(100000000)));
                                t = BigDecimal.ZERO;
                                tmp = BigDecimal.ZERO;
                                break;
                            case 8:
                                res = res.add(t.add(tmp).multiply(BigDecimal.valueOf(1000000000000L)));
                                t = BigDecimal.ZERO;
                                tmp = BigDecimal.ZERO;
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
                lastIsUnit = true;
            }
        }
        res = res.add(t).add(tmp).setScale(2, RoundingMode.UNNECESSARY);
        return negate ? res.negate() : res;
    }

}

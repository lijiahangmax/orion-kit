package com.orion.generator.bank;

import com.orion.lang.utils.Strings;

/**
 * 银行卡工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 10:04
 */
public class BankCardSupport {

    private BankCardSupport() {
    }

    /**
     * 校验卡号是否合法
     *
     * @param cardNo 银行卡号
     * @return 是否合法
     */
    public static boolean valid(String cardNo) {
        if (Strings.isBlank(cardNo)) {
            return false;
        }
        if (!Strings.isInteger(cardNo)) {
            return false;
        }
        if (cardNo.length() > 19 || cardNo.length() < 16) {
            return false;
        }
        char cardCheckCode = getCheckCode(cardNo.substring(0, cardNo.length() - 1).toCharArray());
        return cardNo.charAt(cardNo.length() - 1) == cardCheckCode;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     * 1. 从卡号最后一位数字开始 逆向将奇数位(1 3 5等等)相加
     * 2. 从卡号最后一位数字开始 逆向将偶数位数字 先乘以2(如果乘积为两位数 则将其减去9) 再求和
     * 3. 将奇数位总和加上偶数位总和 结果应该可以被10整除
     *
     * @param chs char
     * @return luhnSum
     */
    public static int getLuhnSum(char[] chs) {
        int luhnSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhnSum += k;
        }
        return luhnSum;
    }

    /**
     * 获取银行卡检查位
     *
     * @param chs char
     * @return char
     */
    public static char getCheckCode(char[] chs) {
        int luhnSum = getLuhnSum(chs);
        return luhnSum % 10 == 0 ? '0' : (char) (10 - luhnSum % 10 + '0');
    }

}

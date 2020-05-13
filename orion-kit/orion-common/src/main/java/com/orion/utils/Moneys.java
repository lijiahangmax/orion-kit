package com.orion.utils;

import com.orion.utils.math.Decimals;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 对金额的处理
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/3 10:22
 */
public class Moneys {

    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    private static final String[] CN_UPPER_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};
    private static final String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final int MONEY_PRECISION = 2;
    private static final String CN_ZERO_FULL = "零元整";

    private Moneys() {
    }

    // https://www.oschina.net/question/930697_2287851?sort=default
    // https://blog.csdn.net/Mouldyworld/article/details/55254713

    public static void main(String[] args) {
        BigDecimal decimal = new BigDecimal("102030405060.91");
        String s = moneyStream(decimal).toCurrency();
        System.out.println(s);
    }

    /**
     * 金额转大写
     *
     * @param m 金额
     * @return 大写金额
     */
    public static String toCurrency(BigDecimal m) {
        StringBuilder sb = new StringBuilder();
        int minus = m.signum();
        if (minus == 0) {
            return CN_ZERO_FULL;
        }
        long number = m.movePointRight(MONEY_PRECISION).setScale(0, 4).abs().longValue();
        long scale = number % 100;
        int numUnit = 0;
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
                if (!(getZero)) {
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

    public static BigDecimal toDecimal(String m) {
        return null;
    }

    /**
     * 返回流式操作对象
     *
     * @param money ignore
     * @return 流ignore
     */
    public static MoneyStream moneyStream(Object money) {
        return new MoneyStream(Decimals.toDecimal(money));
    }

    /**
     * 拼接到开头
     *
     * @param money  ignore
     * @param before ignore
     * @return ignore
     */
    public static String appendBefore(BigDecimal money, String before) {
        if (money == null) {
            money = BigDecimal.ZERO;
        }
        if (Strings.isBlank(before)) {
            before = "";
        }
        return before + money.toString();
    }

    /**
     * 拼接到结尾
     *
     * @param money ignore
     * @param after ignore
     * @return ignore
     */
    public static String appendAfter(BigDecimal money, String after) {
        if (money == null) {
            money = BigDecimal.ZERO;
        }
        if (Strings.isBlank(after)) {
            after = "";
        }
        return money.toString() + after;
    }

    /**
     * 拼接到开头和结尾
     *
     * @param money  ignore
     * @param before ignore
     * @param after  ignore
     * @return ignore
     */
    public static String appendAround(BigDecimal money, String before, String after) {
        if (money == null) {
            money = BigDecimal.ZERO;
        }
        if (Strings.isBlank(before)) {
            before = "";
        }
        if (Strings.isBlank(after)) {
            after = "";
        }
        return before + money.toString() + after;
    }

    /**
     * 去除小数点后的位数 舍入模式: 直接去除
     *
     * @param money ignore
     * @return 去除小数点后的数
     */
    public static BigDecimal skipDecimal(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return money.setScale(0, RoundingMode.DOWN);
    }

    /**
     * 去除小数点后的位数
     *
     * @param money ignore
     * @param mode  舍入模式
     * @return 去除小数点后的数
     */
    public static BigDecimal skipDecimal(BigDecimal money, RoundingMode mode) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        if (mode == null) {
            mode = RoundingMode.DOWN;
        }
        return money.setScale(0, mode);
    }

    /**
     * 设置小数点后的位数 舍入模式: 直接去除
     *
     * @param len   小数点后的的长度
     * @param money ignore
     * @return 去除小数点后的数
     */
    public static BigDecimal decimalLength(BigDecimal money, int len) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return money.setScale(len, RoundingMode.DOWN);
    }

    /**
     * 设置小数点后的位数
     *
     * @param money ignore
     * @param len   小数点后的的长度
     * @param mode  舍入模式
     * @return 去除小数点后的数
     */
    public static BigDecimal decimalLength(BigDecimal money, int len, RoundingMode mode) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        if (mode == null) {
            mode = RoundingMode.DOWN;
        }
        return money.setScale(len, mode);
    }

    /**
     * e.g. 汇率计算 相乘
     * money * rate
     *
     * @param money 乘数
     * @param rate  乘数
     * @return 积
     */
    public static BigDecimal multiplyRate(BigDecimal money, Object rate) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.multiply(money, Decimals.toDecimal(rate));
    }

    /**
     * e.g. 汇率计算 相除
     * money / rate
     *
     * @param money 被除数
     * @param rate  除数
     * @return 商
     */
    public static BigDecimal divideRate(BigDecimal money, Object rate) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.divide(money, Decimals.toDecimal(rate));
    }

    /**
     * 相乘10倍
     * e.g. 角->分 元->角
     *
     * @param money 乘数
     * @return 积
     */
    public static BigDecimal multiplyTen(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.multiply(money, new BigDecimal(10));
    }

    /**
     * 相除10倍
     * e.g. 角->元 分->角
     *
     * @param money 除数
     * @return 商
     */
    public static BigDecimal divideTen(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.divide(money, new BigDecimal(10));
    }

    /**
     * 相乘100倍
     * e.g. 元->分 百元->元
     *
     * @param money 乘数
     * @return 积
     */
    public static BigDecimal multiplyHundred(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.multiply(money, new BigDecimal(100));
    }

    /**
     * 相除100倍
     * e.g. 分->元 元->百元
     *
     * @param money 除数
     * @return 商
     */
    public static BigDecimal divideHundred(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.divide(money, new BigDecimal(100));
    }

    /**
     * 相乘1000倍
     * e.g. 千元->元
     *
     * @param money 乘数
     * @return 积
     */
    public static BigDecimal multiplyThousand(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.multiply(money, new BigDecimal(1000));
    }

    /**
     * 相除1000倍
     * e.g. 元->千元
     *
     * @param money 除数
     * @return 商
     */
    public static BigDecimal divideThousand(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.divide(money, new BigDecimal(1000));
    }

    /**
     * 相乘10000倍
     * e.g. 万元->元
     *
     * @param money 乘数
     * @return 积
     */
    public static BigDecimal multiplyMyriad(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.multiply(money, new BigDecimal(10000));
    }

    /**
     * 相除10000倍
     * e.g. 元->万元
     *
     * @param money 除数
     * @return 商
     */
    public static BigDecimal divideMyriad(BigDecimal money) {
        if (money == null) {
            return BigDecimal.ZERO;
        }
        return Decimals.divide(money, new BigDecimal(10000));
    }

    /**
     * 流式计算
     */
    public static class MoneyStream {

        private BigDecimal money;
        private String result;

        private MoneyStream(BigDecimal money) {
            if (money == null) {
                money = BigDecimal.ZERO;
            }
            this.money = money;
        }

        // --------------- 1 ---------------

        /**
         * e.g. 汇率计算 相乘
         * money * rate
         *
         * @param rate 乘数
         * @return 积
         */
        public MoneyStream multiplyRate(Object rate) {
            money = Decimals.multiply(money, Decimals.toDecimal(rate));
            return this;
        }

        /**
         * e.g. 汇率计算 相除
         * money / rate
         *
         * @param rate 除数
         * @return 商
         */
        public MoneyStream divideRate(Object rate) {
            money = Decimals.divide(money, Decimals.toDecimal(rate));
            return this;
        }

        /**
         * 相乘10倍
         * e.g. 角->分 元->角
         *
         * @return 积
         */
        public MoneyStream multiplyTen() {
            money = Decimals.multiply(money, new BigDecimal(10));
            return this;
        }

        /**
         * 相除10倍
         * e.g. 角->元 分->角
         *
         * @return 商
         */
        public MoneyStream divideTen() {
            money = Decimals.divide(money, new BigDecimal(10));
            return this;
        }

        /**
         * 相乘100倍
         * e.g. 元->分 百元->元
         *
         * @return 积
         */
        public MoneyStream multiplyHundred() {
            money = Decimals.multiply(money, new BigDecimal(100));
            return this;
        }

        /**
         * 相除100倍
         * e.g. 分->元 元->百元
         *
         * @return 商
         */
        public MoneyStream divideHundred() {
            money = Decimals.divide(money, new BigDecimal(100));
            return this;
        }

        /**
         * 相乘1000倍
         * e.g. 千元->元
         *
         * @return 积
         */
        public MoneyStream multiplyThousand() {
            money = Decimals.multiply(money, new BigDecimal(1000));
            return this;
        }

        /**
         * 相除1000倍
         * e.g. 元->千元
         *
         * @return 商
         */
        public MoneyStream divideThousand() {
            money = Decimals.divide(money, new BigDecimal(1000));
            return this;
        }

        /**
         * 相乘10000倍
         * e.g. 万元->元
         *
         * @return 积
         */
        public MoneyStream multiplyMyriad() {
            money = Decimals.multiply(money, new BigDecimal(10000));
            return this;
        }

        /**
         * 相除10000倍
         * e.g. 元->万元
         *
         * @return 商
         */
        public MoneyStream divideMyriad() {
            money = Decimals.divide(money, new BigDecimal(10000));
            return this;
        }

        // --------------- 2 ---------------

        /**
         * 去除小数点后的位数 舍入模式: 直接去除
         *
         * @return 去除小数点后的数
         */
        public MoneyStream skip() {
            money = money.setScale(0, RoundingMode.DOWN);
            return this;
        }

        /**
         * 去除小数点后的位数
         *
         * @param mode 舍入模式
         * @return 去除小数点后的数
         */
        public MoneyStream skip(RoundingMode mode) {
            if (mode == null) {
                mode = RoundingMode.DOWN;
            }
            money = money.setScale(0, mode);
            return this;
        }

        /**
         * 设置小数点后的位数 舍入模式: 直接去除
         *
         * @param len 小数点后的位数
         * @return 去除小数点后的数
         */
        public MoneyStream length(int len) {
            money = money.setScale(len, RoundingMode.DOWN);
            return this;
        }

        /**
         * 设置小数点后的位数
         *
         * @param len  小数点后的位数
         * @param mode 舍入模式
         * @return 去除小数点后的数
         */
        public MoneyStream length(int len, RoundingMode mode) {
            if (mode == null) {
                mode = RoundingMode.DOWN;
            }
            money = money.setScale(len, mode);
            return this;
        }

        // --------------- 3 ---------------

        /**
         * 拼接到开头
         *
         * @param before ignore
         * @return ignore
         */
        public MoneyStream before(String before) {
            if (Strings.isBlank(before)) {
                before = "";
            }
            result = before + money.toString();
            return this;
        }

        /**
         * 拼接到结尾
         *
         * @param after ignore
         * @return ignore
         */
        public MoneyStream after(String after) {
            if (Strings.isBlank(after)) {
                after = "";
            }
            result = money.toString() + after;
            return this;
        }

        /**
         * 拼接到开头和结尾
         *
         * @param before ignore
         * @param after  ignore
         * @return ignore
         */
        public MoneyStream around(String before, String after) {
            if (Strings.isBlank(before)) {
                before = "";
            }
            if (Strings.isBlank(after)) {
                after = "";
            }
            result = before + money.toString() + after;
            return this;
        }

        // --------------- 4 ---------------

        /**
         * 获取结果
         *
         * @return 结果
         */
        public BigDecimal result() {
            return money;
        }

        /**
         * 获取结果
         *
         * @return 结果
         */
        public String resultString() {
            return result == null ? money.toString() : result;
        }

        /**
         * 转为大写金额
         *
         * @return 大写金额
         */
        public String toCurrency() {
            return Moneys.toCurrency(money);
        }

        @Override
        public String toString() {
            return result == null ? money.toString() : result;
        }

    }

}

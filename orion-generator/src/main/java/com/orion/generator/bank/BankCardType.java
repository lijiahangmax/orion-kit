package com.orion.generator.bank;

/**
 * 银行卡类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/10 15:31
 */
public enum BankCardType {

    /**
     * 借记卡/储蓄卡
     */
    DEBIT("借记卡/储蓄卡"),

    /**
     * 信用卡/贷记卡
     */
    CREDIT("信用卡/贷记卡");

    private final String name;

    BankCardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

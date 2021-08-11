package com.orion.generator.bank;

import com.orion.lang.wrapper.Pair;
import com.orion.utils.Strings;
import com.orion.utils.collect.Sets;
import com.orion.utils.random.Randoms;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 银行卡生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 10:04
 */
public class BankCardGenerator {

    private BankCardGenerator() {
    }

    /**
     * 随机生成 借记卡 银行卡号
     *
     * @return card
     */
    public static Pair<BankNameType, String> generatorCard() {
        return generatorCard(BankCardType.DEBIT);
    }

    /**
     * 随机生成银行卡号
     *
     * @param card card
     * @return card
     */
    public static Pair<BankNameType, String> generatorCard(BankCardType card) {
        BankNameType[] values = BankNameType.values();
        BankNameType bankName = values[Randoms.RANDOM.nextInt(values.length)];
        return Pair.of(bankName, generatorCard(bankName, card));
    }

    /**
     * 随机生成 借记卡 银行卡号
     *
     * @param bank bank
     * @return card
     */
    public static String generatorCard(BankNameType bank) {
        return generatorCard(bank, BankCardType.DEBIT);
    }

    /**
     * 随机生成银行卡号
     *
     * @param bank bank
     * @param card card
     * @return card
     */
    public static String generatorCard(BankNameType bank, BankCardType card) {
        Map<String, Integer> prefixMap;
        if (BankCardType.DEBIT.equals(card)) {
            // 借记卡
            prefixMap = bank.getDebitPrefix();
        } else {
            // 信用卡
            prefixMap = bank.getCreditPrefix();
        }
        // 随机前缀
        Set<String> keys = prefixMap.keySet();
        String prefix = Objects.requireNonNull(Sets.get(keys, Randoms.RANDOM.nextInt(keys.size())));
        Integer length = prefixMap.get(prefix);
        // 填充随机数
        int contentLength = length - prefix.length() - 1;
        long randomMax = Long.parseLong(Strings.repeat('9', contentLength));
        String cardNoCheck = prefix + Strings.leftPad(Randoms.randomLong(randomMax) + Strings.EMPTY, contentLength, '0');
        // 拼接检查码
        char checkCode = BankCardSupport.getCheckCode(cardNoCheck.toCharArray());
        return cardNoCheck + checkCode;
    }

}

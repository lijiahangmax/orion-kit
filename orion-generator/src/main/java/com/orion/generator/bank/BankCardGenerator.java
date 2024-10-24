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
package com.orion.generator.bank;

import com.orion.generator.addres.AddressSupport;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Sets;
import com.orion.lang.utils.random.Randoms;

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

    private static final String BRANCH_BANK_NAME = "分行";

    private static final String SUBBRANCH_BANK_NAME = "支行";

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
        BankNameType bank = Arrays1.random(BankNameType.values());
        return Pair.of(bank, generatorCard(bank, card));
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
        String prefix = Objects.requireNonNull(Sets.random(keys));
        Integer length = prefixMap.get(prefix);
        // 填充随机数
        int contentLength = length - prefix.length() - 1;
        long randomMax = Long.parseLong(Strings.repeat('9', contentLength));
        String cardNoCheck = prefix + Strings.leftPad(Randoms.randomLong(randomMax) + Strings.EMPTY, contentLength, '0');
        // 拼接检查码
        char checkCode = BankCardSupport.getCheckCode(cardNoCheck.toCharArray());
        return cardNoCheck + checkCode;
    }

    public static String generatorOpeningBank(BankNameType bank) {
        return generatorOpeningBank(bank, AddressSupport.randomCountyCode());
    }

    /**
     * 获取开户行名称
     *
     * @param bank       bank
     * @param countyCode 县级编码
     * @return 开户行名称
     */
    public static String generatorOpeningBank(BankNameType bank, Integer countyCode) {
        int cityCode = Integer.parseInt(countyCode.toString().substring(0, 4));
        String cityName = AddressSupport.getCityName(cityCode);
        String countyName = AddressSupport.getCountyName(countyCode);
        return bank.getName() + cityName + BRANCH_BANK_NAME + countyName + SUBBRANCH_BANK_NAME;
    }

}

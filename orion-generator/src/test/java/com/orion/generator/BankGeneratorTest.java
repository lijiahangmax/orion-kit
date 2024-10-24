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
package com.orion.generator;

import com.orion.generator.bank.BankCardGenerator;
import com.orion.generator.bank.BankCardSupport;
import com.orion.generator.bank.BankCardType;
import com.orion.generator.bank.BankNameType;
import com.orion.lang.define.wrapper.Pair;
import org.junit.Test;

/**
 * http://www.jsons.cn/bankcheck/
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 11:43
 */
public class BankGeneratorTest {

    @Test
    public void gen1() {
        for (int i = 0; i < 100; i++) {
            Pair<BankNameType, String> p = BankCardGenerator.generatorCard();
            System.out.println(p);
            System.out.println(BankCardGenerator.generatorOpeningBank(p.getKey()));
            assert BankCardSupport.valid(p.getValue());
        }
    }

    @Test
    public void gen2() {
        for (int i = 0; i < 100; i++) {
            Pair<BankNameType, String> p = BankCardGenerator.generatorCard(BankCardType.CREDIT);
            System.out.println(p);
            assert BankCardSupport.valid(p.getValue());
        }
    }

    @Test
    public void gen3() {
        for (int i = 0; i < 100; i++) {
            String p = BankCardGenerator.generatorCard(BankNameType.ICBC, BankCardType.DEBIT);
            System.out.println(p);
            assert BankCardSupport.valid(p);
        }
    }

    @Test
    public void gen4() {
        for (int i = 0; i < 100; i++) {
            String p = BankCardGenerator.generatorCard(BankNameType.ICBC, BankCardType.CREDIT);
            System.out.println(p);
            assert BankCardSupport.valid(p);
        }
    }

}

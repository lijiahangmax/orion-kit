/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.generator;

import cn.orionsec.kit.generator.idcard.IdCardGenerator;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.identity.IdCards;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 10:22
 */
public class IdCardGeneratorTest {

    @Test
    public void generator() {
        for (int i = 0; i < 20; i++) {
            String c = IdCardGenerator.generator(18);
            System.out.print(c + " ");
            System.out.print(IdCards.getAge(c) + " ");
            System.out.print(IdCards.getGender(c) ? "男 " : "女 ");
            System.out.print(IdCardGenerator.getIssueOrg(c) + " ");
            System.out.print(IdCardGenerator.getPeriodString(c) + " ");
            System.out.print(IdCardGenerator.getAddress(c) + " ");
            System.out.println();
            Valid.isTrue(IdCards.isValidCard(c));
            Valid.isTrue(IdCards.getAge(c) == 18);
        }
    }

    @Test
    public void generator1() {
        for (int i = 0; i < 20; i++) {
            String c = IdCardGenerator.generator(5, 50);
            System.out.print(c + " ");
            System.out.print(IdCards.getAge(c) + " ");
            System.out.print(IdCards.getGender(c) ? "男 " : "女 ");
            System.out.print(IdCardGenerator.getIssueOrg(c) + " ");
            System.out.print(IdCardGenerator.getPeriodString(c) + " ");
            System.out.print(IdCardGenerator.getFullAddress(c) + " ");
            System.out.print(IdCardGenerator.getAddressCode(c) + " ");
            System.out.print(Arrays.toString(IdCardGenerator.getAddressCodeExt(c)) + " ");
            System.out.print(Arrays.toString(IdCardGenerator.getAddressExt(c)) + " ");
            System.out.println();
            Valid.isTrue(IdCards.isValidCard(c));
        }
    }

}

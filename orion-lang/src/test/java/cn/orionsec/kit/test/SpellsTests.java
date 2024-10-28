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
package cn.orionsec.kit.test;

import cn.orionsec.kit.lang.utils.Spells;
import cn.orionsec.kit.lang.utils.Strings;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 10:57
 */
public class SpellsTests {

    @Test
    public void getFirstSpellTest() {
        for (int i = 0; i < 15; i++) {
            String s = Strings.randomChars(5) + " 1e绿路";
            System.out.println(s + '\t' + Spells.getFirstSpell(s));
        }
    }

    @Test
    public void getSpellTest() {
        for (int i = 0; i < 15; i++) {
            String s = Strings.randomChars(5) + " 1e绿路";
            System.out.println(s + '\t' + Spells.getSpell(s));
        }
    }

    @Test
    public void getFullSpellTest() {
        for (int i = 0; i < 15; i++) {
            String s = Strings.randomChars(15) + " 1e绿路";
            System.out.println(s + '\t' + Spells.getFullSpell(s));
        }
    }

    @Test
    public void isChineseTest() {
        System.out.println(Spells.isAllChinese("1e绿路"));
        System.out.println(Spells.containsChinese("1e绿路"));
        for (int i = 0; i < 5; i++) {
            String s = Strings.randomChars(15);
            System.out.println(s + '\t' + Spells.isAllChinese(s));
            System.out.println(s + '\t' + Spells.containsChinese(s));
            System.out.println(s + '\t' + Spells.isChinese(s.charAt(0)));
        }
    }

    @Test
    public void lengthTest() {
        System.out.println(Spells.isMessyCode("锘挎槬鐪犱笉瑙夋檽锛屽\uE629澶勯椈鍟奸笩"));
        System.out.println(Spells.isMessyCode("ï»¿æ˜¥çœ ä¸\u008Dè§‰æ™“ï¼Œå¤„å¤„é—»å•¼é¸Ÿ¡£"));
        System.out.println(Spells.isMessyCode("锘挎槬鐪犱笉瑙夋檽锛屽"));
        System.out.println(Spells.getChineseLength(Strings.randomChars(15) + "1e绿路"));
        System.out.println(Spells.getChineseLength(Strings.randomChars(15) + "123123qweqwe#$%"));
    }

    @Test
    public void cjkTest() {
        for (int i = 0; i < 5; i++) {
            String s = Strings.randomChars(15);
            System.out.println(s + '\t' + Spells.isChineseByReg(s));
            System.out.println(s + '\t' + Spells.isChineseByName(s));
        }
    }

}

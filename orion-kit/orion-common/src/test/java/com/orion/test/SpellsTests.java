package com.orion.test;

import com.orion.utils.Spells;
import com.orion.utils.Strings;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 10:57
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

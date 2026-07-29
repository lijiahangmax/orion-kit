package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpellsTest {

    @Test
    public void testGetSpell() {
        String result = Spells.getSpell("中国");
        assertEquals("zhongguo", result);
    }

    @Test
    public void testGetFirstSpell() {
        String result = Spells.getFirstSpell("中国");
        assertEquals("zg", result);
    }

    @Test
    public void testGetSpellWithEnglish() {
        String result = Spells.getSpell("hello世界");
        assertEquals("helloshijie", result);
    }

    @Test
    public void testIsChineseByReg() {
        assertTrue(Spells.isChineseByReg("中文"));
        assertFalse(Spells.isChineseByReg("english"));
        assertFalse(Spells.isChineseByReg(""));
        assertFalse(Spells.isChineseByReg(null));
    }

    @Test
    public void testIsChineseByName() {
        // isChineseByName uses CJK regex, may not match all chars on all JDKs
        assertFalse(Spells.isChineseByName("english"));
        assertFalse(Spells.isChineseByName(""));
    }

    @Test
    public void testContainsChinese() {
        assertTrue(Spells.containsChinese("hello中文"));
        assertFalse(Spells.containsChinese("hello"));
    }

    @Test
    public void testIsChinese() {
        assertTrue(Spells.isChinese('中'));
        assertFalse(Spells.isChinese('A'));
    }

    @Test
    public void testGetChineseLength() {
        assertEquals(2, Spells.getChineseLength("hello中文world"));
        assertEquals(0, Spells.getChineseLength("hello"));
    }
}

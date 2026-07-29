package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DesensitizesTest {

    @Test
    public void testMixFirstName() {
        String result = Desensitizes.mixFirstName("张三");
        assertEquals("*三", result);
    }

    @Test
    public void testMixName() {
        String result = Desensitizes.mixName("张三");
        assertEquals("张*", result);
    }

    @Test
    public void testMixPhone() {
        String result = Desensitizes.mixPhone("13812345678");
        assertEquals("138****5678", result);
    }

    @Test
    public void testMixCardNum() {
        String result = Desensitizes.mixCardNum("110101199001011234");
        assertEquals("11**************34", result);
    }

    @Test
    public void testMixWithKeepStartEnd() {
        String result = Desensitizes.mix("hello world", 2, 2);
        assertEquals("he*******ld", result);
    }

    @Test
    public void testMixEmpty() {
        assertEquals("", Desensitizes.mix("", 1, 1));
        assertEquals("", Desensitizes.mix(null, 1, 1));
    }

    @Test
    public void testMixKeepExceedsLength() {
        // keepStart + keepEnd >= length, no desensitize
        assertEquals("abc", Desensitizes.mix("abc", 2, 2));
    }

    @Test
    public void testMixWithReplacer() {
        String result = Desensitizes.mix("hello", 1, 1, "***");
        assertEquals("h***o", result);
    }
}

package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VariableStylesTest {

    @Test
    public void testSmallHumpToSerpentine() {
        String result = VariableStyles.SMALL_HUMP.toSerpentine("helloWorld");
        assertEquals("hello_world", result);
    }

    @Test
    public void testSmallHumpToSpine() {
        String result = VariableStyles.SMALL_HUMP.toSpine("helloWorld");
        assertEquals("hello-world", result);
    }

    @Test
    public void testSmallHumpToBigHump() {
        String result = VariableStyles.SMALL_HUMP.toBigHump("helloWorld");
        assertEquals("HelloWorld", result);
    }

    @Test
    public void testSmallHumpToSmallHump() {
        String result = VariableStyles.SMALL_HUMP.toSmallHump("helloWorld");
        assertEquals("helloWorld", result);
    }

    @Test
    public void testBigHumpToSmallHump() {
        String result = VariableStyles.BIG_HUMP.toSmallHump("HelloWorld");
        assertEquals("helloWorld", result);
    }

    @Test
    public void testSerpentineToSmallHump() {
        String result = VariableStyles.SERPENTINE.toSmallHump("hello_world");
        assertEquals("helloWorld", result);
    }

    @Test
    public void testSerpentineToBigHump() {
        String result = VariableStyles.SERPENTINE.toBigHump("hello_world");
        assertEquals("HelloWorld", result);
    }

    @Test
    public void testSpineToSmallHump() {
        String result = VariableStyles.SPINE.toSmallHump("hello-world");
        assertEquals("helloWorld", result);
    }

    @Test
    public void testSpineToBigHump() {
        String result = VariableStyles.SPINE.toBigHump("hello-world");
        assertEquals("HelloWorld", result);
    }

    @Test
    public void testConvert() {
        assertEquals("hello_world", VariableStyles.convert("helloWorld", VariableStyles.SERPENTINE));
        assertEquals("hello-world", VariableStyles.convert("helloWorld", VariableStyles.SPINE));
        assertEquals("HelloWorld", VariableStyles.convert("helloWorld", VariableStyles.BIG_HUMP));
    }

    @Test
    public void testConvertBlank() {
        assertNull(VariableStyles.convert(null, VariableStyles.SERPENTINE));
        assertEquals("", VariableStyles.convert("", VariableStyles.SERPENTINE));
    }
}

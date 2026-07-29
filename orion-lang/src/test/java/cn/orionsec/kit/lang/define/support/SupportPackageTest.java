package cn.orionsec.kit.lang.define.support;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * support 包单元测试
 */
public class SupportPackageTest {

    static class TestClone extends CloneSupport<TestClone> {
        String name;
        int value;

        TestClone(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    @Test
    public void testCloneSupportBasic() {
        TestClone original = new TestClone("hello", 42);
        TestClone cloned = original.clone();
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertEquals("hello", cloned.name);
        assertEquals(42, cloned.value);
    }

    @Test
    public void testCloneSupportIndependence() {
        TestClone original = new TestClone("a", 1);
        TestClone cloned = original.clone();
        cloned.name = "b";
        cloned.value = 2;
        assertEquals("a", original.name);
        assertEquals(1, original.value);
    }

    @Test
    public void testCloneSupportType() {
        TestClone original = new TestClone("test", 0);
        TestClone cloned = original.clone();
        assertTrue(cloned instanceof TestClone);
    }
}

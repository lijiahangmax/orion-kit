package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * ByteCodes 单元测试
 */
public class ByteCodesTest {

    static class SampleClass {
        public void doSomething(String s, int n) {
        }

        public String getName() {
            return "";
        }

        public void setValues(int[] arr) {
        }
    }

    @Test
    public void testIsAndroid() {
        Assert.assertFalse(ByteCodes.isAndroid());
    }

    @Test
    public void testIsAndroidByVmName() {
        Assert.assertFalse(ByteCodes.isAndroid("Java HotSpot(TM) 64-Bit Server VM"));
        Assert.assertTrue(ByteCodes.isAndroid("Dalvik"));
        Assert.assertTrue(ByteCodes.isAndroid("Lemur VM"));
    }

    @Test
    public void testGetPrimitiveLetter() {
        Assert.assertEquals("I", ByteCodes.getPrimitiveLetter(int.class));
        Assert.assertEquals("J", ByteCodes.getPrimitiveLetter(long.class));
        Assert.assertEquals("Z", ByteCodes.getPrimitiveLetter(boolean.class));
        Assert.assertEquals("B", ByteCodes.getPrimitiveLetter(byte.class));
        Assert.assertEquals("C", ByteCodes.getPrimitiveLetter(char.class));
        Assert.assertEquals("S", ByteCodes.getPrimitiveLetter(short.class));
        Assert.assertEquals("F", ByteCodes.getPrimitiveLetter(float.class));
        Assert.assertEquals("D", ByteCodes.getPrimitiveLetter(double.class));
        Assert.assertEquals("V", ByteCodes.getPrimitiveLetter(void.class));
    }

    @Test
    public void testGetClassSignature() {
        Assert.assertEquals("I", ByteCodes.getClassSignature(int.class));
        Assert.assertEquals("Ljava/lang/String;", ByteCodes.getClassSignature(String.class));
        Assert.assertEquals("[I", ByteCodes.getClassSignature(int[].class));
    }

    @Test
    public void testGetClassTypeName() {
        Assert.assertEquals("java/lang/String", ByteCodes.getClassTypeName(String.class));
        Assert.assertEquals("I", ByteCodes.getClassTypeName(int.class));
        Assert.assertEquals("[I", ByteCodes.getClassTypeName(int[].class));
    }

    @Test
    public void testGetMethodSignature() throws Exception {
        Method method = SampleClass.class.getDeclaredMethod("doSomething", String.class, int.class);
        String sig = ByteCodes.getMethodSignature(method);
        Assert.assertEquals("(Ljava/lang/String;I)V", sig);
    }

    @Test
    public void testGetMethodSignatureGetter() throws Exception {
        Method method = SampleClass.class.getDeclaredMethod("getName");
        String sig = ByteCodes.getMethodSignature(method);
        Assert.assertEquals("()Ljava/lang/String;", sig);
    }
}

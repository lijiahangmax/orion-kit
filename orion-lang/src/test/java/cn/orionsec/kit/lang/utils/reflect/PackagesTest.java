package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

/**
 * Packages 单元测试
 */
public class PackagesTest {

    @Test
    public void testGetPackageNameByObject() {
        Object obj = Integer.valueOf(1);
        String pkg = Packages.getPackageName(obj);
        Assert.assertEquals("java.lang", pkg);
    }

    @Test
    public void testGetPackageNameByClass() {
        String pkg = Packages.getPackageName(String.class);
        Assert.assertEquals("java.lang", pkg);
    }

    @Test
    public void testGetPackageNameByClassName() {
        String pkg = Packages.getPackageName("cn.orionsec.kit.lang.utils.reflect.Packages");
        Assert.assertEquals("cn.orionsec.kit.lang.utils.reflect", pkg);
    }

    @Test
    public void testGetPackageNameNoPackage() {
        String pkg = Packages.getPackageName("SimpleClassName");
        Assert.assertEquals("", pkg);
    }

    @Test
    public void testGetPackageNameNull() {
        String pkg = Packages.getPackageName((Object) null);
        Assert.assertEquals("", pkg);
    }

    @Test
    public void testGetPackage() {
        // Package.getPackage may return null in some classloader contexts
        // Just verify no exception is thrown
        Packages.getPackage("java.lang");
        Packages.getPackage(String.class);
        Packages.getPackage("hello");
    }
}

package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * PackageScanner 单元测试
 */
public class PackageScannerTest {

    @Test
    public void testScanPackage() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect");
        scanner.with(PackageScanner.class).scan();

        Set<Class<?>> classes = scanner.getClasses();
        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.size() > 0);
        Assert.assertTrue(classes.contains(Fields.class));
        Assert.assertTrue(classes.contains(Methods.class));
    }

    @Test
    public void testScanPackageWithWildcard() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect.*");
        scanner.with(PackageScanner.class).scan();

        Set<Class<?>> classes = scanner.getClasses();
        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.size() > 0);
    }

    @Test
    public void testGetImplClass() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect.*");
        scanner.with(PackageScanner.class).scan();

        Set<Class<?>> classes = scanner.getClasses();
        Assert.assertNotNull(classes);
    }

    @Test
    public void testAddPackage() {
        PackageScanner scanner = new PackageScanner();
        scanner.addPackage("cn.orionsec.kit.lang.utils.reflect");
        scanner.with(PackageScanner.class).scan();

        Set<Class<?>> classes = scanner.getClasses();
        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.size() > 0);
    }

    @Test
    public void testGetPackages() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect");
        Assert.assertNotNull(scanner.getPackages());
        Assert.assertTrue(scanner.getPackages().contains("cn.orionsec.kit.lang.utils.reflect"));
    }

    @Test
    public void testGetResources() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect");
        scanner.with(PackageScanner.class);
        Assert.assertNotNull(scanner.getResources());
        Assert.assertTrue(scanner.getResources().size() > 0);
    }

    @Test
    public void testGetClassLoader() {
        PackageScanner scanner = new PackageScanner("cn.orionsec.kit.lang.utils.reflect");
        Assert.assertNotNull(scanner.getClassLoader());
    }
}

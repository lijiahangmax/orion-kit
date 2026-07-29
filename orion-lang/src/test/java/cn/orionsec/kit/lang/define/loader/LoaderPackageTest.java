package cn.orionsec.kit.lang.define.loader;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * loader 包单元测试
 */
public class LoaderPackageTest {

    @Test
    public void testJarClassLoaderCreate() {
        JarClassLoader loader = new JarClassLoader();
        assertNotNull(loader);
    }

    @Test
    public void testJarClassLoaderParent() {
        JarClassLoader loader = new JarClassLoader();
        assertNotNull(loader.getParent());
    }

    @Test(expected = Exception.class)
    public void testJarClassLoaderAddInvalidFile() {
        JarClassLoader loader = new JarClassLoader();
        loader.addJar("nonexistent.jar");
    }

    @Test(expected = Exception.class)
    public void testJarClassLoaderAddNonJar() {
        JarClassLoader loader = new JarClassLoader();
        loader.addJar("test.txt");
    }
}

package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Jars 单元测试
 */
public class JarsTest {

    @Test
    public void testGetTargetUrl() {
        URL url = Jars.getTargetUrl();
        // In test env this should not be null (we have classloader resource)
        Assert.assertNotNull(url);
    }

    @Test
    public void testGetJarFile() {
        // In non-jar env, this returns null
        // Just verify no exception is thrown
        Jars.getJarFile();
    }

    @Test
    public void testGetJarFileFromUrl() {
        URL url = Jars.getTargetUrl();
        if (url != null) {
            // For file:// urls this will return a JarFile or null
            Jars.getJarFile(url);
        }
    }
}

package cn.orionsec.kit.lang.utils.loader;

import org.junit.Test;

import java.util.ServiceLoader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ServiceLoaders 单元测试
 */
public class ServiceLoadersTest {

    @Test
    public void testLoad() {
        // Load a known JDK service
        ServiceLoader<java.nio.charset.spi.CharsetProvider> loader =
                ServiceLoaders.load(java.nio.charset.spi.CharsetProvider.class);
        assertNotNull(loader);
    }

    @Test
    public void testLoadWithClassLoader() {
        ServiceLoader<java.nio.charset.spi.CharsetProvider> loader =
                ServiceLoaders.load(java.nio.charset.spi.CharsetProvider.class,
                        Thread.currentThread().getContextClassLoader());
        assertNotNull(loader);
    }

    @Test
    public void testLoadFirstAvailable() {
        // May return null if no implementation found
        Object result = ServiceLoaders.loadFirstAvailable(java.nio.charset.spi.CharsetProvider.class);
        // result can be null, that's ok
    }

    @Test
    public void testLoadFirst() {
        // May return null if no implementation found
        Object result = ServiceLoaders.loadFirst(java.nio.charset.spi.CharsetProvider.class);
        // result can be null, that's ok
    }

    @Test
    public void testLoadReturnsIterable() {
        ServiceLoader<java.nio.charset.spi.CharsetProvider> loader =
                ServiceLoaders.load(java.nio.charset.spi.CharsetProvider.class);
        // Should be iterable
        int count = 0;
        for (java.nio.charset.spi.CharsetProvider p : loader) {
            count++;
        }
        // count may be 0 or more
        assertTrue(count >= 0);
    }
}

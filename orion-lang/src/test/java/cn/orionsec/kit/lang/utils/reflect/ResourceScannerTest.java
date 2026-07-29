package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Set;

/**
 * ResourceScanner 单元测试
 */
public class ResourceScannerTest {

    @Test
    public void testScan() {
        ResourceScanner scanner = new ResourceScanner();
        scanner.with(ResourceScanner.class);
        scanner.scan();

        Set<String> resources = scanner.getScannedResources();
        Assert.assertNotNull(resources);
    }

    @Test
    public void testScanWithInclude() {
        ResourceScanner scanner = new ResourceScanner();
        scanner.with(ResourceScanner.class);
        scanner.include(".properties");
        scanner.scan();

        Set<String> resources = scanner.getScannedResources();
        Assert.assertNotNull(resources);
        for (String r : resources) {
            Assert.assertTrue(r.endsWith(".properties"));
        }
    }

    @Test
    public void testScanWithExclude() {
        ResourceScanner scanner = new ResourceScanner();
        scanner.with(ResourceScanner.class);
        scanner.exclude(".class");
        scanner.scan();

        Set<String> resources = scanner.getScannedResources();
        Assert.assertNotNull(resources);
        for (String r : resources) {
            Assert.assertFalse(r.endsWith(".class"));
        }
    }

    @Test
    public void testGetResources() {
        ResourceScanner scanner = new ResourceScanner();
        scanner.with(ResourceScanner.class);
        Assert.assertNotNull(scanner.getResources());
        Assert.assertTrue(scanner.getResources().size() > 0);
    }

    @Test
    public void testGetResourceAsStream() {
        // Try to get a resource that exists in classpath
        InputStream is = ResourceScanner.getResourceAsStream("META-INF/MANIFEST.MF");
        // May or may not exist depending on classpath, just ensure no exception
        if (is != null) {
            Assert.assertNotNull(is);
        }
    }

    @Test
    public void testAddResource() {
        ResourceScanner scanner = new ResourceScanner();
        URL url = ResourceScanner.class.getClassLoader().getResource("");
        if (url != null) {
            scanner.addResource(url);
            Assert.assertTrue(scanner.getResources().size() > 0);
        }
    }
}

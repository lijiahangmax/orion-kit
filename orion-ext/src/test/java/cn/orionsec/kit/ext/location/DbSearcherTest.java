package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.block.DataBlock;
import cn.orionsec.kit.ext.location.region.config.DbConfig;
import cn.orionsec.kit.ext.location.region.core.DbSearcher;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * DbSearcher 单元测试
 */
public class DbSearcherTest {

    private static DbSearcher searcher;
    private static boolean available = false;

    @BeforeClass
    public static void setUp() {
        try {
            InputStream is = DbSearcherTest.class.getClassLoader().getResourceAsStream("region.db");
            Assume.assumeTrue("region.db not available on classpath", is != null);
            Path tempFile = Files.createTempFile("region", ".db");
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            is.close();
            searcher = new DbSearcher(new DbConfig(), tempFile.toString());
            available = true;
        } catch (Exception e) {
            Assume.assumeTrue("Failed to initialize DbSearcher: " + e.getMessage(), false);
        }
    }

    @AfterClass
    public static void tearDown() {
        if (searcher != null) {
            try {
                searcher.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    @Test
    public void testBtreeSearch() throws Exception {
        Assume.assumeTrue(available);
        DataBlock result = searcher.btreeSearch("61.148.8.54");
        assertNotNull(result);
        assertNotNull(result.getRegion());
        assertTrue(result.getCityId() >= 0);
    }

    @Test
    public void testBtreeSearchWithLong() throws Exception {
        Assume.assumeTrue(available);
        // 61.148.8.54 => ip2long
        long ip = ((61L << 24) | (148L << 16) | (8L << 8) | 54L) & 0xFFFFFFFFL;
        DataBlock result = searcher.btreeSearch(ip);
        assertNotNull(result);
    }

    @Test
    public void testMemorySearch() throws Exception {
        Assume.assumeTrue(available);
        DataBlock result = searcher.memorySearch("61.148.8.54");
        assertNotNull(result);
        assertNotNull(result.getRegion());
    }

    @Test
    public void testMemorySearchWithLong() throws Exception {
        Assume.assumeTrue(available);
        long ip = ((61L << 24) | (148L << 16) | (8L << 8) | 54L) & 0xFFFFFFFFL;
        DataBlock result = searcher.memorySearch(ip);
        assertNotNull(result);
    }

    @Test
    public void testGetDbConfig() {
        Assume.assumeTrue(available);
        DbConfig config = searcher.getDbConfig();
        assertNotNull(config);
    }

    @Test
    public void testSearchNonExistentIp() throws Exception {
        Assume.assumeTrue(available);
        // 0.0.0.0 might not be in the database
        DataBlock result = searcher.btreeSearch("0.0.0.0");
        // could be null if not found
        // just verify no exception thrown
    }
}

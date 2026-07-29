package cn.orionsec.kit.lang.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * UUIds 单元测试
 */
public class UUIdsTest {

    @Test
    public void testRandom15NotNull() {
        String uuid = UUIds.random15();
        assertNotNull(uuid);
        assertEquals(15, uuid.length());
    }

    @Test
    public void testRandom15Unique() {
        Set<String> ids = new HashSet<>();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            ids.add(UUIds.random15());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testRandom15Long() {
        long id = UUIds.random15Long();
        assertTrue(id > 0);
    }

    @Test
    public void testRandom19() {
        String uuid = UUIds.random19();
        assertNotNull(uuid);
        assertEquals(19, uuid.length());
    }

    @Test
    public void testRandom19Unique() {
        Set<String> ids = new HashSet<>();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            ids.add(UUIds.random19());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testRandom32() {
        String uuid = UUIds.random32();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        // 不应包含横线
        assertFalse(uuid.contains("-"));
        // 应只包含十六进制字符
        assertTrue(uuid.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testRandom36() {
        String uuid = UUIds.random();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        // 应包含4个横线
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    public void testRandomBase64() {
        String base64 = UUIds.randomBase64();
        assertNotNull(base64);
        // base64 编码 16 字节 -> 24 字符 (带padding)
        assertTrue(base64.length() > 0);
    }

    @Test
    public void testRandom32Unique() {
        Set<String> ids = new HashSet<>();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            ids.add(UUIds.random32());
        }
        assertEquals(count, ids.size());
    }

}

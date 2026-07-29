package cn.orionsec.kit.lang.utils.hash;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Hash 工具类单元测试
 */
public class HashesTest {

    @Test
    public void testHashCode() {
        assertEquals(0, Hashes.hashCode(null));
        assertNotEquals(0, Hashes.hashCode("hello"));
        assertNotEquals(0, Hashes.hashCode(new int[]{1, 2, 3}));
        assertNotEquals(0, Hashes.hashCode(new byte[]{1, 2, 3}));
    }

    @Test
    public void testIdentityHashCode() {
        Object obj = new Object();
        assertEquals(System.identityHashCode(obj), Hashes.identityHashCode(obj));
    }

    @Test
    public void testHash() {
        int hash = Hashes.hash("hello");
        assertEquals("hello".hashCode(), hash);
    }

    @Test
    public void testAdditiveHash() {
        int hash = Hashes.additiveHash("test", 31);
        assertTrue(hash >= 0 && hash < 31);
    }

    @Test
    public void testRotatingHash() {
        int hash = Hashes.rotatingHash("test", 31);
        assertTrue(hash >= 0 && hash < 31);
    }

    @Test
    public void testOneByOneHash() {
        int hash = Hashes.oneByOneHash("test");
        assertNotEquals(0, hash);
    }

    @Test
    public void testBernstein() {
        int hash = Hashes.bernstein("test");
        assertNotEquals(0, hash);
    }

    @Test
    public void testFnvHashBytes() {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        int hash = Hashes.fnvHash(data);
        assertTrue(hash >= 0);
    }

    @Test
    public void testFnvHashString() {
        int hash = Hashes.fnvHash("hello");
        assertTrue(hash >= 0);
    }

    @Test
    public void testIntHash() {
        int hash = Hashes.intHash(12345);
        assertNotEquals(12345, hash);
    }

    @Test
    public void testRsHash() {
        int hash = Hashes.rsHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testJsHash() {
        int hash = Hashes.jsHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testPjwHash() {
        int hash = Hashes.pjwHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testElfHash() {
        int hash = Hashes.elfHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testBkdrHash() {
        int hash = Hashes.bkdrHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testSdbmHash() {
        int hash = Hashes.sdbmHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testDjbHash() {
        int hash = Hashes.djbHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testDekHash() {
        int hash = Hashes.dekHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testApHash() {
        int hash = Hashes.apHash("test");
        assertNotEquals(0, hash);
    }

    @Test
    public void testTianlHash() {
        long hash = Hashes.tianlHash("test");
        assertTrue(hash >= 0);
    }

    @Test
    public void testTianlHashEmpty() {
        long hash = Hashes.tianlHash("");
        assertEquals(0, hash);
    }

    @Test
    public void testMixHash() {
        long hash = Hashes.mixHash("test");
        assertNotEquals(0, hash);
    }

    @Test
    public void testMurmur32() {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        int hash = Hashes.murmur32(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testMurmur64() {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        long hash = Hashes.murmur64(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testMurmur128() {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        long[] hash = Hashes.murmur128(data);
        assertEquals(2, hash.length);
    }

    @Test
    public void testCityHash32() {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
        int hash = Hashes.cityHash32(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testCityHash64() {
        byte[] data = "hello world".getBytes(StandardCharsets.UTF_8);
        long hash = Hashes.cityHash64(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testCityHash64WithSeed() {
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);
        long hash = Hashes.cityHash64(data, 42L);
        assertNotEquals(0, hash);
    }

    @Test
    public void testCityHash128() {
        byte[] data = "hello world test data for hash".getBytes(StandardCharsets.UTF_8);
        long[] hash = Hashes.cityHash128(data);
        assertEquals(2, hash.length);
    }

    @Test
    public void testConsistentHash() {
        List<String> nodes = Arrays.asList("node1", "node2", "node3");
        ConsistentHash<String> ch = new ConsistentHash<>(150, nodes);
        String result = ch.get("testKey");
        assertNotNull(result);
        assertTrue(nodes.contains(result));
    }

    @Test
    public void testConsistentHashAddRemove() {
        List<String> nodes = Arrays.asList("node1", "node2", "node3");
        ConsistentHash<String> ch = new ConsistentHash<>(150, nodes);
        ch.add("node4");
        ch.remove("node1");
        String result = ch.get("testKey");
        assertNotNull(result);
    }

    @Test
    public void testConsistentHashConsistency() {
        List<String> nodes = Arrays.asList("node1", "node2", "node3");
        ConsistentHash<String> ch = new ConsistentHash<>(150, nodes);
        String result1 = ch.get("sameKey");
        String result2 = ch.get("sameKey");
        assertEquals(result1, result2);
    }
}

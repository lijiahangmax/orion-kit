package cn.orionsec.kit.lang.utils.hash;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * MurmurHash 单元测试
 */
public class MurmurHashTest {

    @Test
    public void testHash32String() {
        int hash = MurmurHash.hash32("hello");
        assertNotEquals(0, hash);
        // Deterministic
        assertEquals(hash, MurmurHash.hash32("hello"));
    }

    @Test
    public void testHash32Bytes() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        int hash = MurmurHash.hash32(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash32WithSeed() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        int hash1 = MurmurHash.hash32(data, data.length, 0);
        int hash2 = MurmurHash.hash32(data, data.length, 42);
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testHash64String() {
        long hash = MurmurHash.hash64("hello");
        assertNotEquals(0, hash);
        assertEquals(hash, MurmurHash.hash64("hello"));
    }

    @Test
    public void testHash64Bytes() {
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        long hash = MurmurHash.hash64(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash64WithSeed() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        long hash1 = MurmurHash.hash64(data, data.length, 0);
        long hash2 = MurmurHash.hash64(data, data.length, 42);
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testHash128String() {
        long[] hash = MurmurHash.hash128("hello world");
        assertNotNull(hash);
        assertEquals(2, hash.length);
    }

    @Test
    public void testHash128Bytes() {
        byte[] data = "test data for 128 bit hash".getBytes(StandardCharsets.UTF_8);
        long[] hash = MurmurHash.hash128(data);
        assertEquals(2, hash.length);
    }

    @Test
    public void testHash128WithSeed() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        long[] hash1 = MurmurHash.hash128(data, data.length, 0);
        long[] hash2 = MurmurHash.hash128(data, data.length, 42);
        assertFalse(hash1[0] == hash2[0] && hash1[1] == hash2[1]);
    }

    @Test
    public void testDifferentInputsDifferentHashes() {
        int hash1 = MurmurHash.hash32("abc");
        int hash2 = MurmurHash.hash32("def");
        assertNotEquals(hash1, hash2);
    }
}

package cn.orionsec.kit.lang.utils.hash;

import cn.orionsec.kit.lang.define.number.Number128;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * CityHash 单元测试
 */
public class CityHashTest {

    @Test
    public void testHash32Short() {
        byte[] data = "hi".getBytes(StandardCharsets.UTF_8);
        int hash = CityHash.hash32(data);
        // deterministic
        assertEquals(hash, CityHash.hash32(data));
    }

    @Test
    public void testHash32Medium() {
        byte[] data = "hello world!".getBytes(StandardCharsets.UTF_8);
        int hash = CityHash.hash32(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash32Long() {
        byte[] data = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8);
        int hash = CityHash.hash32(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash64Short() {
        byte[] data = "ab".getBytes(StandardCharsets.UTF_8);
        long hash = CityHash.hash64(data);
        assertEquals(hash, CityHash.hash64(data));
    }

    @Test
    public void testHash64Medium() {
        byte[] data = "hello world test!!!!".getBytes(StandardCharsets.UTF_8);
        long hash = CityHash.hash64(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash64Long() {
        byte[] data = "The quick brown fox jumps over the lazy dog and more text to make it longer than 64 bytes total".getBytes(StandardCharsets.UTF_8);
        long hash = CityHash.hash64(data);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash64WithSeed() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        long hash = CityHash.hash64(data, 123L);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash64WithTwoSeeds() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        long hash = CityHash.hash64(data, 123L, 456L);
        assertNotEquals(0, hash);
    }

    @Test
    public void testHash128() {
        byte[] data = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8);
        Number128 hash = CityHash.hash128(data);
        assertNotNull(hash);
    }

    @Test
    public void testHash128WithSeed() {
        byte[] data = "test data for hashing with seed value that is long enough".getBytes(StandardCharsets.UTF_8);
        Number128 seed = new Number128(1L, 2L);
        Number128 hash = CityHash.hash128(data, seed);
        assertNotNull(hash);
    }

    @Test
    public void testDeterministic() {
        byte[] data = "deterministic test".getBytes(StandardCharsets.UTF_8);
        assertEquals(CityHash.hash32(data), CityHash.hash32(data));
        assertEquals(CityHash.hash64(data), CityHash.hash64(data));
    }
}

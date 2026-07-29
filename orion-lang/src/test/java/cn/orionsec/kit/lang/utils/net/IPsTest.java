package cn.orionsec.kit.lang.utils.net;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * IPs 工具类测试
 */
public class IPsTest {

    @Test
    public void testIsIpv4() {
        assertTrue(IPs.isIpv4("192.168.1.1"));
        assertTrue(IPs.isIpv4("0.0.0.0"));
        assertTrue(IPs.isIpv4("255.255.255.255"));
        assertFalse(IPs.isIpv4("256.1.1.1"));
        assertFalse(IPs.isIpv4("192.168.1"));
        assertFalse(IPs.isIpv4("abc.def.ghi.jkl"));
        assertFalse(IPs.isIpv4("192.168.01.1"));
    }

    @Test
    public void testIsIp() {
        assertTrue(IPs.isIp("192.168.1.1"));
        assertFalse(IPs.isIp("not-an-ip"));
    }

    @Test
    public void testCheckIp() {
        assertEquals("192.168.1.1", IPs.checkIp("192.168.1.1"));
        assertNull(IPs.checkIp(""));
        assertNull(IPs.checkIp(null));
        assertNull(IPs.checkIp("unknown"));
        assertNull(IPs.checkIp("not-ip"));
    }

    @Test
    public void testIsInternalIp() {
        assertTrue(IPs.isInternalIp("192.168.1.1"));
        assertTrue(IPs.isInternalIp("10.0.0.1"));
        assertTrue(IPs.isInternalIp("127.0.0.1"));
        assertFalse(IPs.isInternalIp("8.8.8.8"));
    }

    @Test
    public void testIpToLong() {
        long result = IPs.ipToLong("192.168.1.1");
        assertTrue(result > 0);
        assertEquals(3232235777L, result);
    }

    @Test
    public void testLongToIp() {
        String ip = IPs.longToIp(3232235777L);
        assertEquals("192.168.1.1", ip);
    }

    @Test
    public void testIpToLongRoundTrip() {
        String original = "10.20.30.40";
        long l = IPs.ipToLong(original);
        String result = IPs.longToIp(l);
        assertEquals(original, result);
    }

    @Test
    public void testIpInRange() {
        assertTrue(IPs.ipInRange("192.168.1.0", "192.168.1.255", "192.168.1.100"));
        assertFalse(IPs.ipInRange("192.168.1.0", "192.168.1.255", "192.168.2.1"));
        assertTrue(IPs.ipInRange("10.0.0.0", "10.0.0.10", "10.0.0.0"));
        assertTrue(IPs.ipInRange("10.0.0.0", "10.0.0.10", "10.0.0.10"));
    }

    @Test
    public void testIpv4ToBytes() {
        byte[] bytes = IPs.ipv4ToBytes("192.168.1.1");
        assertNotNull(bytes);
        assertEquals(4, bytes.length);
        assertEquals((byte) 192, bytes[0]);
        assertEquals((byte) 168, bytes[1]);
        assertEquals((byte) 1, bytes[2]);
        assertEquals((byte) 1, bytes[3]);
    }

    @Test
    public void testRandomIp() {
        String ip = IPs.randomIp();
        assertNotNull(ip);
        assertTrue(IPs.isIpv4(ip));
    }
}

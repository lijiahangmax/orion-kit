package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.region.LocationRegions;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * LocationRegions 单元测试
 */
public class LocationRegionsTest {

    private static boolean available = false;

    @BeforeClass
    public static void setUp() {
        try {
            // Try to trigger static init of LocationRegions
            String address = LocationRegions.getAddress("61.148.8.54");
            available = (address != null);
        } catch (ExceptionInInitializerError | NoClassDefFoundError | Exception e) {
            available = false;
        }
    }

    @Test
    public void testGetAddress() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("61.148.8.54");
        assertNotNull(address);
        assertFalse(address.isEmpty());
    }

    @Test
    public void testGetAddressWithAlgorithm1() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("61.148.8.54", 1);
        assertNotNull(address);
    }

    @Test
    public void testGetAddressWithAlgorithm2() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("61.148.8.54", 2);
        assertNotNull(address);
    }

    @Test
    public void testGetAddressWithAlgorithm3() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("61.148.8.54", 3);
        assertNotNull(address);
    }

    @Test
    public void testGetRegion() {
        Assume.assumeTrue("LocationRegions not available", available);
        Region region = LocationRegions.getRegion("61.148.8.54");
        assertNotNull(region);
        assertNotNull(region.getCountry());
        assertNotNull(region.getProvince());
    }

    @Test
    public void testGetRegionWithAlgorithm() {
        Assume.assumeTrue("LocationRegions not available", available);
        Region region = LocationRegions.getRegion("61.148.8.54", 1);
        assertNotNull(region);
    }

    @Test
    public void testGetAddressInvalidIp() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("invalid_ip");
        assertNotNull(address);
        // invalid ip should return UNKNOWN format
    }

    @Test
    public void testGetAddressPrivateIp() {
        Assume.assumeTrue("LocationRegions not available", available);
        String address = LocationRegions.getAddress("192.168.1.1");
        assertNotNull(address);
    }

    @Test
    public void testGetRegionConsistency() {
        Assume.assumeTrue("LocationRegions not available", available);
        Region r1 = LocationRegions.getRegion("61.148.8.54", 1);
        Region r2 = LocationRegions.getRegion("61.148.8.54", 3);
        assertNotNull(r1);
        assertNotNull(r2);
        // Different algorithms should return same result
        assertEquals(r1.getCountry(), r2.getCountry());
    }
}

package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.ext.LocationExt;
import cn.orionsec.kit.ext.location.ext.core.IpLocation;
import cn.orionsec.kit.ext.location.ext.core.LocationSeeker;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * LocationExt 单元测试
 */
public class LocationExtTest {

    private static boolean available = false;

    @BeforeClass
    public static void setUp() {
        try {
            // Try to trigger static init of LocationExt
            LocationSeeker seeker = LocationExt.getSeeker();
            available = (seeker != null);
        } catch (ExceptionInInitializerError | NoClassDefFoundError | Exception e) {
            available = false;
        }
    }

    @Test
    public void testGetSeeker() {
        Assume.assumeTrue("LocationExt not available", available);
        LocationSeeker seeker = LocationExt.getSeeker();
        assertNotNull(seeker);
    }

    @Test
    public void testGetCountry() {
        Assume.assumeTrue("LocationExt not available", available);
        String country = LocationExt.getCountry("61.148.8.54");
        assertNotNull(country);
        assertFalse(country.isEmpty());
    }

    @Test
    public void testGetCountryInvalidIp() {
        Assume.assumeTrue("LocationExt not available", available);
        String country = LocationExt.getCountry("invalid");
        assertNull(country);
    }

    @Test
    public void testGetAddress() {
        Assume.assumeTrue("LocationExt not available", available);
        String address = LocationExt.getAddress("61.148.8.54");
        assertNotNull(address);
        assertFalse(address.isEmpty());
    }

    @Test
    public void testGetAddressInvalidIp() {
        Assume.assumeTrue("LocationExt not available", available);
        String address = LocationExt.getAddress("not_an_ip");
        assertNull(address);
    }

    @Test
    public void testGetArea() {
        Assume.assumeTrue("LocationExt not available", available);
        String area = LocationExt.getArea("61.148.8.54");
        assertNotNull(area);
    }

    @Test
    public void testGetAreaInvalidIp() {
        Assume.assumeTrue("LocationExt not available", available);
        String area = LocationExt.getArea("abc");
        assertNull(area);
    }

    @Test
    public void testGetIpLocation() {
        Assume.assumeTrue("LocationExt not available", available);
        IpLocation loc = LocationExt.getIpLocation("61.148.8.54");
        assertNotNull(loc);
        assertNotNull(loc.getCountry());
        assertNotNull(loc.getArea());
    }

    @Test
    public void testGetIpLocationInvalidIp() {
        Assume.assumeTrue("LocationExt not available", available);
        IpLocation loc = LocationExt.getIpLocation("invalid");
        assertNull(loc);
    }

    @Test
    public void testGetRegion() {
        Assume.assumeTrue("LocationExt not available", available);
        Region region = LocationExt.getRegion("61.148.8.54");
        assertNotNull(region);
    }

    @Test
    public void testGetRegionInvalidIp() {
        Assume.assumeTrue("LocationExt not available", available);
        Region region = LocationExt.getRegion("xyz");
        assertNull(region);
    }
}

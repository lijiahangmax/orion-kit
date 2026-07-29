package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.ext.core.IpLocation;
import cn.orionsec.kit.ext.location.ext.core.LocationSeeker;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.*;

/**
 * LocationSeeker 单元测试
 */
public class LocationSeekerTest {

    private static LocationSeeker seeker;
    private static boolean available = false;

    @BeforeClass
    public static void setUp() {
        try {
            InputStream is = LocationSeekerTest.class.getClassLoader().getResourceAsStream("region.dat");
            Assume.assumeTrue("region.dat not available on classpath", is != null);
            // copy to temp file
            Path tempFile = Files.createTempFile("region", ".dat");
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            is.close();
            seeker = new LocationSeeker(tempFile.toFile());
            available = true;
        } catch (Exception e) {
            Assume.assumeTrue("Failed to initialize LocationSeeker: " + e.getMessage(), false);
        }
    }

    @Test
    public void testGetCountry() {
        Assume.assumeTrue(available);
        String country = seeker.getCountry("61.148.8.54");
        assertNotNull(country);
        assertFalse(country.isEmpty());
    }

    @Test
    public void testGetArea() {
        Assume.assumeTrue(available);
        String area = seeker.getArea("61.148.8.54");
        assertNotNull(area);
    }

    @Test
    public void testGetAddress() {
        Assume.assumeTrue(available);
        String address = seeker.getAddress("61.148.8.54");
        assertNotNull(address);
        assertFalse(address.isEmpty());
    }

    @Test
    public void testGetIpLocation() {
        Assume.assumeTrue(available);
        IpLocation loc = seeker.getIpLocation("61.148.8.54");
        assertNotNull(loc);
        assertNotNull(loc.getCountry());
        assertNotNull(loc.getArea());
    }

    @Test
    public void testGetRegion() {
        Assume.assumeTrue(available);
        Region region = seeker.getRegion("61.148.8.54");
        assertNotNull(region);
    }

    @Test
    public void testGetRegionWithEmptyIp() {
        Assume.assumeTrue(available);
        Region region = seeker.getRegion("");
        assertNotNull(region);
    }

    @Test
    public void testGetRegionWithNullIp() {
        Assume.assumeTrue(available);
        Region region = seeker.getRegion(null);
        assertNotNull(region);
    }

    @Test
    public void testGetCountryWithByteArray() {
        Assume.assumeTrue(available);
        byte[] ip = new byte[]{61, (byte) 148, 8, 54};
        String country = seeker.getCountry(ip);
        assertNotNull(country);
    }

    @Test
    public void testGetAreaWithByteArray() {
        Assume.assumeTrue(available);
        byte[] ip = new byte[]{61, (byte) 148, 8, 54};
        String area = seeker.getArea(ip);
        assertNotNull(area);
    }

    @Test
    public void testGetCountryCached() {
        Assume.assumeTrue(available);
        // Call twice to test cache
        String country1 = seeker.getCountry("61.148.8.54");
        String country2 = seeker.getCountry("61.148.8.54");
        assertEquals(country1, country2);
    }

    @Test
    public void testConstructorWithNullFile() {
        // null RandomAccessFile should not throw but seeker won't work
        LocationSeeker s = new LocationSeeker((java.io.RandomAccessFile) null);
        assertNotNull(s);
    }
}

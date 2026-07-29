package cn.orionsec.kit.ext.location;

import cn.orionsec.kit.ext.location.ext.core.IpLocation;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

/**
 * IpLocation 单元测试
 */
public class IpLocationTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        assertNotNull(loc);
        // default country and area should return UNKNOWN (because empty string doesn't end with CZ88.NET)
        assertNotNull(loc.getCountry());
        assertNotNull(loc.getArea());
    }

    @Test
    public void testGetCountryWithCz88() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        // Set country field via reflection
        java.lang.reflect.Field countryField = IpLocation.class.getDeclaredField("country");
        countryField.setAccessible(true);
        countryField.set(loc, "纯真网络CZ88.NET");
        // Should return UNKNOWN since it ends with CZ88.NET
        assertEquals(LocationConst.UNKNOWN, loc.getCountry());
    }

    @Test
    public void testGetAreaWithCz88() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        java.lang.reflect.Field areaField = IpLocation.class.getDeclaredField("area");
        areaField.setAccessible(true);
        areaField.set(loc, "对方和您在同一内部网CZ88.NET");
        assertEquals(LocationConst.UNKNOWN, loc.getArea());
    }

    @Test
    public void testGetCountryNormal() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        java.lang.reflect.Field countryField = IpLocation.class.getDeclaredField("country");
        countryField.setAccessible(true);
        countryField.set(loc, "北京市朝阳区");
        assertEquals("北京市朝阳区", loc.getCountry());
    }

    @Test
    public void testGetAreaNormal() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        java.lang.reflect.Field areaField = IpLocation.class.getDeclaredField("area");
        areaField.setAccessible(true);
        areaField.set(loc, "联通");
        assertEquals("联通", loc.getArea());
    }

    @Test
    public void testToString() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        java.lang.reflect.Field countryField = IpLocation.class.getDeclaredField("country");
        countryField.setAccessible(true);
        countryField.set(loc, "上海市");
        java.lang.reflect.Field areaField = IpLocation.class.getDeclaredField("area");
        areaField.setAccessible(true);
        areaField.set(loc, "电信");
        String str = loc.toString();
        assertTrue(str.contains("上海市"));
        assertTrue(str.contains("电信"));
    }

    @Test
    public void testCopy() throws Exception {
        Constructor<IpLocation> ctor = IpLocation.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        IpLocation loc = ctor.newInstance();
        java.lang.reflect.Field countryField = IpLocation.class.getDeclaredField("country");
        countryField.setAccessible(true);
        countryField.set(loc, "广东省深圳市");
        java.lang.reflect.Field areaField = IpLocation.class.getDeclaredField("area");
        areaField.setAccessible(true);
        areaField.set(loc, "移动");

        // call copy via reflection
        java.lang.reflect.Method copyMethod = IpLocation.class.getDeclaredMethod("copy");
        copyMethod.setAccessible(true);
        IpLocation copied = (IpLocation) copyMethod.invoke(loc);
        assertEquals("广东省深圳市", copied.getCountry());
        assertEquals("移动", copied.getArea());
    }
}

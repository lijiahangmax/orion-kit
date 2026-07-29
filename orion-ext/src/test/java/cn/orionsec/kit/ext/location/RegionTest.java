package cn.orionsec.kit.ext.location;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Region 单元测试
 */
public class RegionTest {

    @Test
    public void testDefaultConstructor() {
        Region region = new Region();
        assertNotNull(region.getCountry());
        assertNotNull(region.getArea());
        assertNotNull(region.getProvince());
        assertNotNull(region.getCity());
        assertNotNull(region.getNet());
    }

    @Test
    public void testThreeArgConstructor() {
        Region region = new Region("中国", "上海", "浦东");
        assertEquals("中国", region.getCountry());
        assertEquals("上海", region.getProvince());
        assertEquals("浦东", region.getCity());
        assertNotNull(region.getArea());
        assertNotNull(region.getNet());
    }

    @Test
    public void testFiveArgConstructor() {
        Region region = new Region("中国", "华东", "上海", "浦东", "电信");
        assertEquals("中国", region.getCountry());
        assertEquals("华东", region.getArea());
        assertEquals("上海", region.getProvince());
        assertEquals("浦东", region.getCity());
        assertEquals("电信", region.getNet());
    }

    @Test
    public void testSetters() {
        Region region = new Region();
        region.setCountry("美国");
        region.setArea("北美");
        region.setProvince("加利福尼亚");
        region.setCity("旧金山");
        region.setNet("谷歌");
        assertEquals("美国", region.getCountry());
        assertEquals("北美", region.getArea());
        assertEquals("加利福尼亚", region.getProvince());
        assertEquals("旧金山", region.getCity());
        assertEquals("谷歌", region.getNet());
    }

    @Test
    public void testToString() {
        Region region = new Region("中国", "华东", "上海", "浦东", "电信");
        String str = region.toString();
        assertEquals("中国|华东|上海|浦东|电信", str);
    }

    @Test
    public void testToStringWithPipe() {
        Region region = new Region("A", "B", "C", "D", "E");
        assertEquals("A|B|C|D|E", region.toString());
    }
}

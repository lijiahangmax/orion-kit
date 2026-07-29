package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

/**
 * BeanMap 单元测试
 */
public class BeanMapTest {

    static class SimpleBean {
        private String name;
        private int age;
        private boolean active;

        public SimpleBean() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    @Test
    public void testCreate() {
        SimpleBean bean = new SimpleBean();
        bean.setName("test");
        bean.setAge(18);
        bean.setActive(true);

        BeanMap map = BeanMap.create(bean);
        Assert.assertEquals("test", map.get("name"));
        Assert.assertEquals(18, map.get("age"));
        Assert.assertEquals(true, map.get("active"));
    }

    @Test
    public void testCreateWithIgnoreFields() {
        SimpleBean bean = new SimpleBean();
        bean.setName("test");
        bean.setAge(18);
        bean.setActive(true);

        BeanMap map = BeanMap.create(bean, "age");
        Assert.assertTrue(map.containsKey("name"));
        Assert.assertFalse(map.containsKey("age"));
    }

    @Test
    public void testCreateAddNull() {
        SimpleBean bean = new SimpleBean();
        bean.setAge(10);
        // name is null

        BeanMap mapWithNull = BeanMap.create(bean, true);
        Assert.assertTrue(mapWithNull.containsKey("name"));

        BeanMap mapWithoutNull = BeanMap.create(bean, false);
        Assert.assertFalse(mapWithoutNull.containsKey("name"));
    }

    @Test
    public void testGetValue() {
        SimpleBean bean = new SimpleBean();
        bean.setName("getValue");

        BeanMap map = BeanMap.create(bean);
        SimpleBean value = map.getValue();
        Assert.assertSame(bean, value);
    }

    @Test
    public void testConstructorWithBean() {
        SimpleBean bean = new SimpleBean();
        bean.setName("ctorTest");
        bean.setAge(99);

        BeanMap map = new BeanMap(bean);
        Assert.assertEquals("ctorTest", map.get("name"));
        Assert.assertEquals(99, map.get("age"));
    }
}

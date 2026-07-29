package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * BeanWrapper 单元测试
 */
public class BeanWrapperTest {

    static class Source {
        private String name;
        private int age;
        private boolean active;

        public Source() {
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

    static class Target {
        private String name;
        private int age;
        private boolean active;
        private String extra;

        public Target() {
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

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }

    @Test
    public void testToMap() {
        Source source = new Source();
        source.setName("test");
        source.setAge(25);
        source.setActive(true);

        Map<String, Object> map = BeanWrapper.toMap(source);
        Assert.assertEquals("test", map.get("name"));
        Assert.assertEquals(25, map.get("age"));
        Assert.assertEquals(true, map.get("active"));
    }

    @Test
    public void testToMapIgnoreFields() {
        Source source = new Source();
        source.setName("test");
        source.setAge(25);
        source.setActive(true);

        Map<String, Object> map = BeanWrapper.toMap(source, "age");
        Assert.assertTrue(map.containsKey("name"));
        Assert.assertFalse(map.containsKey("age"));
    }

    @Test
    public void testToMapPutNull() {
        Source source = new Source();
        source.setAge(10);
        // name is null

        Map<String, Object> mapWithNull = BeanWrapper.toMap(source, true);
        Assert.assertTrue(mapWithNull.containsKey("name"));

        Map<String, Object> mapWithoutNull = BeanWrapper.toMap(source, false);
        Assert.assertFalse(mapWithoutNull.containsKey("name"));
    }

    @Test
    public void testToBean() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "hello");
        map.put("age", 30);
        map.put("active", true);

        Source bean = BeanWrapper.toBean(map, Source.class);
        Assert.assertNotNull(bean);
        Assert.assertEquals("hello", bean.getName());
        Assert.assertEquals(30, bean.getAge());
        Assert.assertTrue(bean.isActive());
    }

    @Test
    public void testToBeanFromArray() {
        Object[] values = {"arrayName", 42, true};
        Map<Integer, String> indexMapper = new HashMap<>();
        indexMapper.put(0, "name");
        indexMapper.put(1, "age");
        indexMapper.put(2, "active");

        Source bean = BeanWrapper.toBean(values, indexMapper, Source.class);
        Assert.assertNotNull(bean);
        Assert.assertEquals("arrayName", bean.getName());
        Assert.assertEquals(42, bean.getAge());
        Assert.assertTrue(bean.isActive());
    }

    @Test
    public void testToMapFromArray() {
        String[] values = {"a", "b", "c"};
        Map<Integer, String> indexMapper = new HashMap<>();
        indexMapper.put(0, "first");
        indexMapper.put(1, "second");
        indexMapper.put(2, "third");

        Map<String, String> result = BeanWrapper.toMap(values, indexMapper);
        Assert.assertEquals("a", result.get("first"));
        Assert.assertEquals("b", result.get("second"));
        Assert.assertEquals("c", result.get("third"));
    }

    @Test
    public void testCopyPropertiesToClass() {
        Source source = new Source();
        source.setName("copy");
        source.setAge(18);
        source.setActive(true);

        Target target = BeanWrapper.copyProperties(source, Target.class);
        Assert.assertNotNull(target);
        Assert.assertEquals("copy", target.getName());
        Assert.assertEquals(18, target.getAge());
        Assert.assertTrue(target.isActive());
    }

    @Test
    public void testCopyPropertiesToObject() {
        Source source = new Source();
        source.setName("obj");
        source.setAge(99);

        Target target = new Target();
        BeanWrapper.copyProperties(source, target);
        Assert.assertEquals("obj", target.getName());
        Assert.assertEquals(99, target.getAge());
    }

    @Test
    public void testCopyPropertiesIgnoreFields() {
        Source source = new Source();
        source.setName("ignored");
        source.setAge(50);

        Target target = BeanWrapper.copyProperties(source, Target.class, "name");
        Assert.assertNull(target.getName());
        Assert.assertEquals(50, target.getAge());
    }

    @Test
    public void testCopyPropertiesWithFieldMapper() {
        Source source = new Source();
        source.setName("mapped");
        source.setAge(33);

        // fieldMapper: key=源字段, value=目标字段
        Map<String, String> fieldMapper = new HashMap<>();
        fieldMapper.put("name", "extra");

        Target target = BeanWrapper.copyProperties(source, Target.class, fieldMapper);
        Assert.assertNotNull(target);
        Assert.assertEquals("mapped", target.getExtra());
    }
}

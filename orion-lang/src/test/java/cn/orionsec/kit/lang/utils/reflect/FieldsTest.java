package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Fields 单元测试
 */
public class FieldsTest {

    // ----- 辅助内部类 -----
    static class Parent {
        private String parentName;
        private int parentAge;

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public int getParentAge() {
            return parentAge;
        }

        public void setParentAge(int parentAge) {
            this.parentAge = parentAge;
        }
    }

    static class Child extends Parent {
        private Long id;
        private String name;
        private boolean active;
        public static final String CONST = "constant";
        private transient String temp;

        public Child() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    @Test
    public void testGetFieldNameByMethodName() {
        Assert.assertEquals("name", Fields.getFieldNameByMethod("getName"));
        Assert.assertEquals("name", Fields.getFieldNameByMethod("setName"));
        Assert.assertEquals("name", Fields.getFieldNameByMethod("isName"));
        Assert.assertNull(Fields.getFieldNameByMethod("get"));
        Assert.assertNull(Fields.getFieldNameByMethod("set"));
        Assert.assertNull(Fields.getFieldNameByMethod("is"));
        Assert.assertNull(Fields.getFieldNameByMethod(""));
        Assert.assertNull(Fields.getFieldNameByMethod((String) null));
    }

    @Test
    public void testGetFieldByMethod() {
        Field field = Fields.getFieldByMethod(Child.class, "getName");
        Assert.assertNotNull(field);
        Assert.assertEquals("name", field.getName());

        Field idField = Fields.getFieldByMethod(Child.class, "getId");
        Assert.assertNotNull(idField);
        Assert.assertEquals("id", idField.getName());
    }

    @Test
    public void testGetFieldValue() {
        Child child = new Child();
        child.setId(100L);
        child.setName("test");

        Long id = Fields.getFieldValue(child, "id");
        Assert.assertEquals(Long.valueOf(100L), id);

        String name = Fields.getFieldValue(child, "name");
        Assert.assertEquals("test", name);
    }

    @Test
    public void testGetFieldValueByField() {
        Child child = new Child();
        child.setId(200L);
        Field field = Fields.getAccessibleField(Child.class, "id");
        Long id = Fields.getFieldValue(child, field);
        Assert.assertEquals(Long.valueOf(200L), id);
    }

    @Test
    public void testSetFieldValue() {
        Child child = new Child();
        Fields.setFieldValue(child, "id", 999L);
        Assert.assertEquals(Long.valueOf(999L), child.getId());

        Fields.setFieldValue(child, "name", "hello");
        Assert.assertEquals("hello", child.getName());
    }

    @Test
    public void testSetFieldValueByField() {
        Child child = new Child();
        Field field = Fields.getAccessibleField(Child.class, "name");
        Fields.setFieldValue(child, field, "world");
        Assert.assertEquals("world", child.getName());
    }

    @Test
    public void testSetFieldValueInfer() {
        Child child = new Child();
        Fields.setFieldValueInfer(child, "id", 123);
        Assert.assertEquals(Long.valueOf(123L), child.getId());
    }

    @Test
    public void testGetFields() {
        List<Field> fields = Fields.getFields(Child.class);
        Assert.assertNotNull(fields);
        // Child has: id, name, active + Parent has: parentName, parentAge (no static, no transient)
        Assert.assertTrue(fields.size() >= 5);
    }

    @Test
    public void testGetFieldMap() {
        Map<String, Field> fieldMap = Fields.getFieldMap(Child.class);
        Assert.assertNotNull(fieldMap);
        Assert.assertTrue(fieldMap.containsKey("id"));
        Assert.assertTrue(fieldMap.containsKey("name"));
        Assert.assertTrue(fieldMap.containsKey("parentName"));
    }

    @Test
    public void testGetStaticFields() {
        List<Field> staticFields = Fields.getStaticFields(Child.class);
        Assert.assertNotNull(staticFields);
        // CONST is static but not transient
        Assert.assertTrue(staticFields.size() >= 1);
    }

    @Test
    public void testGetAccessibleField() {
        Field field = Fields.getAccessibleField(Child.class, "id");
        Assert.assertNotNull(field);
        Assert.assertEquals("id", field.getName());

        // parent field
        Field parentField = Fields.getAccessibleField(Child.class, "parentName");
        Assert.assertNotNull(parentField);

        // non-existing field
        Field noField = Fields.getAccessibleField(Child.class, "nonExist");
        Assert.assertNull(noField);
    }

    @Test
    public void testGetFieldsByCache() {
        List<Field> fields1 = Fields.getFieldsByCache(Child.class);
        List<Field> fields2 = Fields.getFieldsByCache(Child.class);
        Assert.assertSame(fields1, fields2);
    }

    @Test
    public void testGetFieldByCache() {
        Field field = Fields.getFieldByCache(Child.class, "id");
        Assert.assertNotNull(field);
        Assert.assertEquals("id", field.getName());

        Field noField = Fields.getFieldByCache(Child.class, "nonExist");
        Assert.assertNull(noField);
    }

    @Test
    public void testSetAccessible() {
        Field field = Fields.getAccessibleField(Child.class, "id");
        Assert.assertNotNull(field);
        Fields.setAccessible(field);
        Assert.assertTrue(field.isAccessible());
    }
}

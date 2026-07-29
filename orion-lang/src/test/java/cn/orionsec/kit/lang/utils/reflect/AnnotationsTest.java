package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.define.collect.MutableHashMap;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Annotations 单元测试
 */
public class AnnotationsTest {

    @Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface TestAnno {
        String value() default "default";

        int num() default 10;
    }

    @Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface TestAnno2 {
    }

    @TestAnno(value = "classLevel", num = 99)
    static class AnnotatedClass {
        @TestAnno(value = "fieldLevel")
        private String name;

        @TestAnno2
        private int age;

        @TestAnno(value = "ctorLevel")
        public AnnotatedClass(@TestAnno(value = "param0") String name, @TestAnno2 int age) {
            this.name = name;
            this.age = age;
        }

        public AnnotatedClass() {
        }

        @TestAnno(value = "methodLevel")
        @TestAnno2
        public void annotatedMethod(@TestAnno(value = "mp0") String param, @TestAnno2 int num) {
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
    }

    @Test
    public void testGetAnnotationOnClass() {
        TestAnno anno = Annotations.getAnnotation(AnnotatedClass.class, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("classLevel", anno.value());
        Assert.assertEquals(99, anno.num());
    }

    @Test
    public void testGetAnnotationsOnClass() {
        List<Annotation> annos = Annotations.getAnnotations(AnnotatedClass.class);
        Assert.assertNotNull(annos);
        Assert.assertTrue(annos.size() >= 1);
    }

    @Test
    public void testGetAnnotationOnField() throws Exception {
        Field field = AnnotatedClass.class.getDeclaredField("name");
        TestAnno anno = Annotations.getAnnotation(field, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("fieldLevel", anno.value());
    }

    @Test
    public void testGetAnnotationsOnField() throws Exception {
        Field field = AnnotatedClass.class.getDeclaredField("name");
        List<Annotation> annos = Annotations.getAnnotations(field);
        Assert.assertNotNull(annos);
        Assert.assertTrue(annos.size() >= 1);
    }

    @Test
    public void testGetAnnotationOnMethod() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        TestAnno anno = Annotations.getAnnotation(method, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("methodLevel", anno.value());
    }

    @Test
    public void testGetAnnotationsOnMethod() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        List<Annotation> annos = Annotations.getAnnotations(method);
        Assert.assertNotNull(annos);
        Assert.assertTrue(annos.size() >= 2);
    }

    @Test
    public void testGetAnnotationOnConstructor() throws Exception {
        Constructor<?> ctor = AnnotatedClass.class.getDeclaredConstructor(String.class, int.class);
        TestAnno anno = Annotations.getAnnotation(ctor, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("ctorLevel", anno.value());
    }

    @Test
    public void testGetAnnotationsOnConstructor() throws Exception {
        Constructor<?> ctor = AnnotatedClass.class.getDeclaredConstructor(String.class, int.class);
        List<Annotation> annos = Annotations.getAnnotations(ctor);
        Assert.assertNotNull(annos);
        Assert.assertTrue(annos.size() >= 1);
    }

    @Test
    public void testGetParameterAnnotationOnMethod() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        TestAnno anno = Annotations.getParameterAnnotation(method, 0, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("mp0", anno.value());
    }

    @Test
    public void testGetParameterAnnotationOnConstructor() throws Exception {
        Constructor<?> ctor = AnnotatedClass.class.getDeclaredConstructor(String.class, int.class);
        TestAnno anno = Annotations.getParameterAnnotation(ctor, 0, TestAnno.class);
        Assert.assertNotNull(anno);
        Assert.assertEquals("param0", anno.value());
    }

    @Test
    public void testGetParameterAnnotationsByIndex() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        List<Annotation> annos = Annotations.getParameterAnnotation(method, 0);
        Assert.assertNotNull(annos);
        Assert.assertTrue(annos.size() >= 1);
    }

    @Test
    public void testGetAllParameterAnnotations() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        List<List<Annotation>> allAnnos = Annotations.getParameterAnnotation(method);
        Assert.assertNotNull(allAnnos);
        Assert.assertEquals(2, allAnnos.size());
    }

    @Test
    public void testPresent() {
        Assert.assertTrue(Annotations.present(AnnotatedClass.class, TestAnno.class));
        Assert.assertFalse(Annotations.present(AnnotatedClass.class, TestAnno2.class));
    }

    @Test
    public void testPresentOnField() throws Exception {
        Field field = AnnotatedClass.class.getDeclaredField("name");
        Assert.assertTrue(Annotations.present(field, TestAnno.class));
        Assert.assertFalse(Annotations.present(field, TestAnno2.class));
    }

    @Test
    public void testPresentOnMethod() throws Exception {
        Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod", String.class, int.class);
        Assert.assertTrue(Annotations.present(method, TestAnno.class, TestAnno2.class));
    }

    @Test
    public void testGetValue() {
        TestAnno anno = Annotations.getAnnotation(AnnotatedClass.class, TestAnno.class);
        String value = Annotations.getValue(anno);
        Assert.assertEquals("classLevel", value);
    }

    @Test
    public void testGetAttribute() {
        TestAnno anno = Annotations.getAnnotation(AnnotatedClass.class, TestAnno.class);
        Integer num = Annotations.getAttribute(anno, "num");
        Assert.assertEquals(Integer.valueOf(99), num);
    }

    @Test
    public void testGetAttributes() {
        TestAnno anno = Annotations.getAnnotation(AnnotatedClass.class, TestAnno.class);
        MutableHashMap<String, Object> attrs = Annotations.getAttributes(anno);
        Assert.assertNotNull(attrs);
        Assert.assertEquals("classLevel", attrs.get("value"));
        Assert.assertEquals(99, attrs.get("num"));
    }

    @Test
    public void testGetDefaultValue() {
        String defaultValue = Annotations.getDefaultValue(TestAnno.class);
        Assert.assertEquals("default", defaultValue);

        Integer defaultNum = Annotations.getDefaultValue(TestAnno.class, "num");
        Assert.assertEquals(Integer.valueOf(10), defaultNum);
    }

    @Test
    public void testGetAnnotatedFields() {
        Map<Field, TestAnno> fields = Annotations.getAnnotatedFields(AnnotatedClass.class, TestAnno.class);
        Assert.assertNotNull(fields);
        Assert.assertTrue(fields.size() >= 1);
    }

    @Test
    public void testGetAnnotatedGetterMethods() {
        Map<Method, TestAnno> methods = Annotations.getAnnotatedGetterMethods(AnnotatedClass.class, TestAnno.class);
        Assert.assertNotNull(methods);
    }

    @Test
    public void testGetAnnotatedSetterMethods() {
        Map<Method, TestAnno2> methods = Annotations.getAnnotatedSetterMethods(AnnotatedClass.class, TestAnno2.class);
        Assert.assertNotNull(methods);
    }
}

package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.function.IGetter;
import cn.orionsec.kit.lang.function.ISetter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Lambdas 单元测试
 */
public class LambdasTest {

    static class LambdaBean {
        private String name;
        private int age;

        public LambdaBean() {
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
    public void testGetGetterFieldName() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        String fieldName = Lambdas.getGetterFieldName(getter);
        Assert.assertEquals("name", fieldName);
    }

    @Test
    public void testGetSetterFieldName() {
        ISetter<LambdaBean, String> setter = LambdaBean::setName;
        String fieldName = Lambdas.getSetterFieldName(setter);
        Assert.assertEquals("name", fieldName);
    }

    @Test
    public void testGetSerializedLambda() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        Assert.assertNotNull(lambda);
    }

    @Test
    public void testGetImplClassName() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        String className = Lambdas.getImplClassName(lambda);
        Assert.assertTrue(className.contains("LambdaBean"));
    }

    @Test
    public void testGetImplClass() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        Class<?> clazz = Lambdas.getImplClass(lambda);
        Assert.assertNotNull(clazz);
    }

    @Test
    public void testGetFieldName() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        String fieldName = Lambdas.getFieldName(lambda);
        Assert.assertEquals("name", fieldName);
    }

    @Test
    public void testGetField() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        Field field = Lambdas.getField(lambda);
        Assert.assertNotNull(field);
        Assert.assertEquals("name", field.getName());
    }

    @Test
    public void testGetMethodName() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        String methodName = Lambdas.getMethodName(lambda);
        Assert.assertEquals("getName", methodName);
    }

    @Test
    public void testGetMethod() {
        IGetter<LambdaBean, String> getter = LambdaBean::getName;
        SerializedLambda lambda = Lambdas.getSerializedLambda(getter);
        Method method = Lambdas.getMethod(lambda);
        Assert.assertNotNull(method);
        Assert.assertEquals("getName", method.getName());
    }
}

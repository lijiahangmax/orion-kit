package cn.orionsec.kit.lang.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * TypeInfer 单元测试
 */
public class TypeInferTest {

    static class InferTarget {
        private String result;

        public InferTarget() {
        }

        public InferTarget(String value) {
            this.result = value;
        }

        public InferTarget(int num) {
            this.result = "int:" + num;
        }

        public String process(String s) {
            return "string:" + s;
        }

        public String process(int n) {
            return "int:" + n;
        }

        public String getResult() {
            return result;
        }
    }

    @Test
    public void testAllTypeMatchStrict() {
        Class<?>[] source = {String.class, Integer.class};
        List<Class<?>[]> targets = new ArrayList<>();
        targets.add(new Class[]{String.class, Integer.class});
        targets.add(new Class[]{String.class, Long.class});

        int index = TypeInfer.allTypeMatch(source, targets, 1);
        Assert.assertEquals(0, index);
    }

    @Test
    public void testAllTypeMatchNotFound() {
        Class<?>[] source = {String.class, Double.class};
        List<Class<?>[]> targets = new ArrayList<>();
        targets.add(new Class[]{String.class, Integer.class});
        targets.add(new Class[]{String.class, Long.class});

        int index = TypeInfer.allTypeMatch(source, targets, 1);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testInvokeInfer() {
        InferTarget target = new InferTarget();
        List<Method> methods = Methods.getAccessibleMethods(InferTarget.class, "process");
        String result = TypeInfer.invokeInfer(target, methods, new Object[]{"hello"});
        Assert.assertEquals("string:hello", result);
    }

    @Test
    public void testNewInstanceInfer() {
        List<Constructor<InferTarget>> ctors = Constructors.getConstructors(InferTarget.class, 1);
        InferTarget obj = TypeInfer.newInstanceInfer(ctors, new Object[]{"test"});
        Assert.assertNotNull(obj);
        Assert.assertEquals("test", obj.getResult());
    }
}

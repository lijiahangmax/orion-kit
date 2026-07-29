package cn.orionsec.kit.lang.function;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Consumer/Supplier 函数式接口测试
 * 覆盖: BooleanConsumer, ByteConsumer, CharConsumer, FloatConsumer, ShotConsumer,
 * ByteSupplier, CharSupplier, FloatSupplier, ShortSupplier
 */
public class ConsumerSupplierTest {

    // ==================== BooleanConsumer ====================

    @Test
    public void testBooleanConsumerAccept() {
        AtomicBoolean ref = new AtomicBoolean(false);
        BooleanConsumer consumer = ref::set;
        consumer.accept(true);
        Assert.assertTrue(ref.get());
    }

    @Test
    public void testBooleanConsumerAndThen() {
        AtomicInteger count = new AtomicInteger(0);
        BooleanConsumer first = b -> count.incrementAndGet();
        BooleanConsumer second = b -> count.incrementAndGet();
        BooleanConsumer chained = first.andThen(second);
        chained.accept(true);
        Assert.assertEquals(2, count.get());
    }

    @Test(expected = NullPointerException.class)
    public void testBooleanConsumerAndThenNull() {
        BooleanConsumer consumer = b -> {
        };
        consumer.andThen(null);
    }

    // ==================== ByteConsumer ====================

    @Test
    public void testByteConsumerAccept() {
        AtomicInteger ref = new AtomicInteger(0);
        ByteConsumer consumer = b -> ref.set(b);
        consumer.accept((byte) 42);
        Assert.assertEquals(42, ref.get());
    }

    @Test
    public void testByteConsumerAndThen() {
        AtomicInteger count = new AtomicInteger(0);
        ByteConsumer first = b -> count.addAndGet(b);
        ByteConsumer second = b -> count.addAndGet(b);
        ByteConsumer chained = first.andThen(second);
        chained.accept((byte) 5);
        Assert.assertEquals(10, count.get());
    }

    @Test(expected = NullPointerException.class)
    public void testByteConsumerAndThenNull() {
        ByteConsumer consumer = b -> {
        };
        consumer.andThen(null);
    }

    // ==================== CharConsumer ====================

    @Test
    public void testCharConsumerAccept() {
        AtomicReference<Character> ref = new AtomicReference<>((char) 0);
        CharConsumer consumer = ref::set;
        consumer.accept('A');
        Assert.assertEquals(Character.valueOf('A'), ref.get());
    }

    @Test
    public void testCharConsumerAndThen() {
        StringBuilder sb = new StringBuilder();
        CharConsumer first = sb::append;
        CharConsumer second = sb::append;
        CharConsumer chained = first.andThen(second);
        chained.accept('X');
        Assert.assertEquals("XX", sb.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testCharConsumerAndThenNull() {
        CharConsumer consumer = c -> {
        };
        consumer.andThen(null);
    }

    // ==================== FloatConsumer ====================

    @Test
    public void testFloatConsumerAccept() {
        AtomicReference<Float> ref = new AtomicReference<>(0.0f);
        FloatConsumer consumer = ref::set;
        consumer.accept(3.14f);
        Assert.assertEquals(3.14f, ref.get(), 0.001f);
    }

    @Test
    public void testFloatConsumerAndThen() {
        AtomicInteger count = new AtomicInteger(0);
        FloatConsumer first = f -> count.incrementAndGet();
        FloatConsumer second = f -> count.incrementAndGet();
        FloatConsumer chained = first.andThen(second);
        chained.accept(1.0f);
        Assert.assertEquals(2, count.get());
    }

    @Test(expected = NullPointerException.class)
    public void testFloatConsumerAndThenNull() {
        FloatConsumer consumer = f -> {
        };
        consumer.andThen(null);
    }

    // ==================== ShotConsumer (short consumer) ====================

    @Test
    public void testShotConsumerAccept() {
        AtomicInteger ref = new AtomicInteger(0);
        ShotConsumer consumer = s -> ref.set(s);
        consumer.accept((short) 100);
        Assert.assertEquals(100, ref.get());
    }

    @Test
    public void testShotConsumerAndThen() {
        AtomicInteger count = new AtomicInteger(0);
        ShotConsumer first = s -> count.addAndGet(s);
        ShotConsumer second = s -> count.addAndGet(s);
        ShotConsumer chained = first.andThen(second);
        chained.accept((short) 7);
        Assert.assertEquals(14, count.get());
    }

    @Test(expected = NullPointerException.class)
    public void testShotConsumerAndThenNull() {
        ShotConsumer consumer = s -> {
        };
        consumer.andThen(null);
    }

    // ==================== ByteSupplier ====================

    @Test
    public void testByteSupplier() {
        ByteSupplier supplier = () -> (byte) 99;
        Assert.assertEquals((byte) 99, supplier.getAsByte());
    }

    @Test
    public void testByteSupplierLambda() {
        byte value = 127;
        ByteSupplier supplier = () -> value;
        Assert.assertEquals(value, supplier.getAsByte());
    }

    // ==================== CharSupplier ====================

    @Test
    public void testCharSupplier() {
        CharSupplier supplier = () -> 'Z';
        Assert.assertEquals('Z', supplier.getAsChar());
    }

    @Test
    public void testCharSupplierLambda() {
        char value = '中';
        CharSupplier supplier = () -> value;
        Assert.assertEquals(value, supplier.getAsChar());
    }

    // ==================== FloatSupplier ====================

    @Test
    public void testFloatSupplier() {
        FloatSupplier supplier = () -> 2.718f;
        Assert.assertEquals(2.718f, supplier.getAsFloat(), 0.001f);
    }

    @Test
    public void testFloatSupplierLambda() {
        float value = Float.MAX_VALUE;
        FloatSupplier supplier = () -> value;
        Assert.assertEquals(value, supplier.getAsFloat(), 0.0f);
    }

    // ==================== ShortSupplier ====================

    @Test
    public void testShortSupplier() {
        ShortSupplier supplier = () -> (short) 32767;
        Assert.assertEquals((short) 32767, supplier.getAsShort());
    }

    @Test
    public void testShortSupplierLambda() {
        short value = -100;
        ShortSupplier supplier = () -> value;
        Assert.assertEquals(value, supplier.getAsShort());
    }

}

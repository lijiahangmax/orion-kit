package cn.orionsec.kit.lang.function;

import cn.orionsec.kit.lang.function.impl.ReaderLineConsumer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 函数式接口测试
 * 覆盖: Conversion, FileFilter, Functions, Suppliers, IGetter, ISetter, Mapper, Reduce, ReaderLineConsumer
 */
public class FunctionInterfaceTest {

    // ==================== Conversion ====================

    @Test
    public void testConversionApply() {
        Conversion<String, Integer> conversion = Integer::parseInt;
        Assert.assertEquals(Integer.valueOf(123), conversion.apply("123"));
    }

    @Test
    public void testConversionWith() {
        Function<String, Integer> func = String::length;
        Conversion<String, Integer> conversion = Conversion.with(func);
        Assert.assertEquals(Integer.valueOf(5), conversion.apply("hello"));
    }

    @Test
    public void testConversionAsFunction() {
        Conversion<Integer, String> conversion = Object::toString;
        // 可以当做 Function 使用
        Function<Integer, String> func = conversion;
        Assert.assertEquals("42", func.apply(42));
    }

    // ==================== FileFilter ====================

    @Test
    public void testFileFilterSuffix() {
        FileFilter filter = FileFilter.suffix(".txt");
        Assert.assertTrue(filter.test(new File("readme.txt")));
        Assert.assertTrue(filter.test(new File("README.TXT")));
        Assert.assertFalse(filter.test(new File("readme.pdf")));
    }

    @Test
    public void testFileFilterContains() {
        FileFilter filter = FileFilter.contains("test");
        Assert.assertTrue(filter.test(new File("MyTestFile.java")));
        Assert.assertTrue(filter.test(new File("testing.txt")));
        Assert.assertFalse(filter.test(new File("readme.md")));
    }

    @Test
    public void testFileFilterMatches() {
        Pattern pattern = Pattern.compile(".*\\.java$");
        FileFilter filter = FileFilter.matches(pattern);
        Assert.assertTrue(filter.test(new File("Test.java")));
        Assert.assertFalse(filter.test(new File("Test.txt")));
    }

    // ==================== Functions ====================

    @Test
    public void testEmptyConsumer() {
        Consumer<String> consumer = Functions.emptyConsumer();
        Assert.assertNotNull(consumer);
        // 不抛异常即可
        consumer.accept("test");
        consumer.accept(null);
    }

    @Test
    public void testEmptyBiConsumer() {
        BiConsumer<String, Integer> biConsumer = Functions.emptyBiConsumer();
        Assert.assertNotNull(biConsumer);
        biConsumer.accept("test", 1);
        biConsumer.accept(null, null);
    }

    @Test
    public void testMergeLeft() {
        BinaryOperator<String> left = Functions.left();
        Assert.assertEquals("a", left.apply("a", "b"));
    }

    @Test
    public void testMergeRight() {
        BinaryOperator<String> right = Functions.right();
        Assert.assertEquals("b", right.apply("a", "b"));
    }

    @Test
    public void testPrintConsumer() {
        Consumer<String> consumer = Functions.printConsumer();
        Assert.assertNotNull(consumer);
        // 调用不抛异常即可
        consumer.accept("hello");
    }

    // ==================== Suppliers ====================

    @Test
    public void testNullSupplier() {
        java.util.function.Supplier<String> supplier = Suppliers.nullSupplier();
        Assert.assertNull(supplier.get());
    }

    @Test
    public void testByteSupplierConstant() {
        Assert.assertEquals((byte) 0, Suppliers.BYTE_SUPPLIER.getAsByte());
    }

    @Test
    public void testShortSupplierConstant() {
        Assert.assertEquals((short) 0, Suppliers.SHORT_SUPPLIER.getAsShort());
    }

    @Test
    public void testIntSupplierConstant() {
        Assert.assertEquals(0, Suppliers.INT_SUPPLIER.getAsInt());
    }

    @Test
    public void testLongSupplierConstant() {
        Assert.assertEquals(0L, Suppliers.LONG_SUPPLIER.getAsLong());
    }

    @Test
    public void testFloatSupplierConstant() {
        Assert.assertEquals(0F, Suppliers.FLOAT_SUPPLIER.getAsFloat(), 0.0f);
    }

    @Test
    public void testDoubleSupplierConstant() {
        Assert.assertEquals(0D, Suppliers.DOUBLE_SUPPLIER.getAsDouble(), 0.0);
    }

    @Test
    public void testBooleanSupplierConstant() {
        Assert.assertFalse(Suppliers.BOOLEAN_SUPPLIER.getAsBoolean());
    }

    @Test
    public void testCharSupplierConstant() {
        Assert.assertEquals((char) 0, Suppliers.CHAR_SUPPLIER.getAsChar());
    }

    // ==================== IGetter ====================

    @Test
    public void testIGetter() {
        IGetter<String, Integer> getter = String::length;
        Assert.assertEquals(Integer.valueOf(3), getter.apply("abc"));
    }

    @Test
    public void testIGetterAsFunction() {
        IGetter<Integer, String> getter = Object::toString;
        Function<Integer, String> func = getter;
        Assert.assertEquals("10", func.apply(10));
    }

    // ==================== ISetter ====================

    @Test
    public void testISetter() {
        List<String> list = new ArrayList<>();
        ISetter<List<String>, String> setter = List::add;
        setter.accept(list, "hello");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("hello", list.get(0));
    }

    @Test
    public void testISetterAsBiConsumer() {
        List<String> list = new ArrayList<>();
        ISetter<List<String>, String> setter = List::add;
        BiConsumer<List<String>, String> biConsumer = setter;
        biConsumer.accept(list, "world");
        Assert.assertEquals("world", list.get(0));
    }

    // ==================== Mapper ====================

    @Test
    public void testMapper() {
        Mapper<String, Integer> mapper = String::length;
        Assert.assertEquals(Integer.valueOf(5), mapper.map("hello"));
    }

    @Test
    public void testMapperLambda() {
        Mapper<Integer, String> mapper = i -> "num:" + i;
        Assert.assertEquals("num:42", mapper.map(42));
    }

    // ==================== Reduce ====================

    @Test
    public void testReduce() {
        Reduce<Integer, Integer> reduce = arr -> {
            int sum = 0;
            for (int v : arr) {
                sum += v;
            }
            return sum;
        };
        Integer[] input = {1, 2, 3, 4, 5};
        Assert.assertEquals(Integer.valueOf(15), reduce.accept(input));
    }

    @Test
    public void testReduceStringConcat() {
        Reduce<String, String> reduce = arr -> String.join(",", arr);
        String[] input = {"a", "b", "c"};
        Assert.assertEquals("a,b,c", reduce.accept(input));
    }

    // ==================== ReaderLineConsumer ====================

    @Test
    public void testReaderLineConsumer() {
        String content = "line1\nline2\nline3";
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<>();
        ReaderLineConsumer consumer = new ReaderLineConsumer(lines::add);
        consumer.accept(input);
        Assert.assertEquals(3, lines.size());
        Assert.assertEquals("line1", lines.get(0));
        Assert.assertEquals("line2", lines.get(1));
        Assert.assertEquals("line3", lines.get(2));
    }

    @Test
    public void testReaderLineConsumerWithCharset() {
        String content = "hello\nworld";
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<>();
        ReaderLineConsumer consumer = new ReaderLineConsumer()
                .charset("UTF-8")
                .bufferSize(1024)
                .lineConsumer(lines::add);
        consumer.accept(input);
        Assert.assertEquals(2, lines.size());
        Assert.assertEquals("hello", lines.get(0));
        Assert.assertEquals("world", lines.get(1));
    }

    @Test
    public void testReaderLineConsumerWithCharsetParam() {
        String content = "abc\ndef";
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<>();
        ReaderLineConsumer consumer = new ReaderLineConsumer()
                .lineConsumer(lines::add, "UTF-8");
        consumer.accept(input);
        Assert.assertEquals(2, lines.size());
    }

    @Test
    public void testReaderLineConsumerPrinter() {
        String content = "print this";
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ReaderLineConsumer consumer = ReaderLineConsumer.printer();
        // 不抛异常即可
        consumer.accept(input);
    }

    @Test
    public void testReaderLineConsumerPrinterWithCharset() {
        String content = "print charset";
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        ReaderLineConsumer consumer = ReaderLineConsumer.printer("UTF-8");
        consumer.accept(input);
    }

    @Test
    public void testReaderLineConsumerEmptyInput() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        List<String> lines = new ArrayList<>();
        ReaderLineConsumer consumer = new ReaderLineConsumer(lines::add);
        consumer.accept(input);
        Assert.assertTrue(lines.isEmpty());
    }

}

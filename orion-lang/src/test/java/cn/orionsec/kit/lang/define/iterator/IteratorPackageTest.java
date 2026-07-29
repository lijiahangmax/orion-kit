package cn.orionsec.kit.lang.define.iterator;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.*;

import static org.junit.Assert.*;

/**
 * iterator 包单元测试
 */
public class IteratorPackageTest {

    // ==================== ArrayIterator ====================

    @Test
    public void testArrayIteratorBasic() {
        String[] arr = {"a", "b", "c"};
        ArrayIterator<String> it = new ArrayIterator<>(arr);
        assertTrue(it.hasNext());
        assertEquals("a", it.next());
        assertEquals("b", it.next());
        assertEquals("c", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testArrayIteratorWithRange() {
        String[] arr = {"a", "b", "c", "d", "e"};
        ArrayIterator<String> it = new ArrayIterator<>(arr, 1, 4);
        List<String> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        assertEquals(Arrays.asList("b", "c", "d"), result);
    }

    @Test
    public void testArrayIteratorReset() {
        Integer[] arr = {1, 2, 3};
        ArrayIterator<Integer> it = new ArrayIterator<>(arr);
        it.next();
        it.next();
        it.reset();
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrayIteratorRemove() {
        ArrayIterator<String> it = new ArrayIterator<>(new String[]{"a"});
        it.remove();
    }

    @Test
    public void testArrayIteratorIterable() {
        String[] arr = {"x", "y"};
        ArrayIterator<String> it = new ArrayIterator<>(arr);
        assertSame(it, it.iterator());
    }

    // ==================== ByteArrayIterator ====================

    @Test
    public void testByteArrayIteratorBasic() {
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        byte[] buffer = new byte[4];
        ByteArrayIterator it = new ByteArrayIterator(bais, buffer);

        assertTrue(it.hasNext());
        int read1 = it.next();
        assertEquals(4, read1);

        assertTrue(it.hasNext());
        int read2 = it.next();
        assertEquals(4, read2);

        assertTrue(it.hasNext());
        int read3 = it.next();
        assertEquals(2, read3);

        assertFalse(it.hasNext());
    }

    @Test
    public void testByteArrayIteratorClose() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayIterator it = new ByteArrayIterator(bais, new byte[2]);
        it.autoClose(true);
        it.close();
        assertFalse(it.hasNext());
    }

    @Test
    public void testByteArrayIteratorIterable() {
        byte[] data = {1};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayIterator it = new ByteArrayIterator(bais, new byte[10]);
        assertSame(it, it.iterator());
    }

    // ==================== ClassIterator ====================

    @Test
    public void testClassIteratorParents() {
        ClassIterator<ArrayList> it = new ClassIterator<>(ArrayList.class);
        List<Class<?>> parents = new ArrayList<>();
        while (it.hasNext()) {
            parents.add(it.next());
        }
        assertTrue(parents.contains(AbstractList.class));
        assertTrue(parents.contains(AbstractCollection.class));
        assertFalse(parents.contains(Object.class));
    }

    @Test
    public void testClassIteratorIncludeObject() {
        ClassIterator<ArrayList> it = new ClassIterator<>(ArrayList.class, true);
        List<Class<?>> parents = new ArrayList<>();
        while (it.hasNext()) {
            parents.add(it.next());
        }
        assertTrue(parents.contains(Object.class));
    }

    // ==================== EmptyIterator ====================

    @Test
    public void testEmptyIteratorHasNext() {
        EmptyIterator<String> it = new EmptyIterator<>();
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptyIteratorNext() {
        EmptyIterator<String> it = new EmptyIterator<>();
        it.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyIteratorRemove() {
        EmptyIterator<String> it = new EmptyIterator<>();
        it.remove();
    }

    @Test
    public void testEmptyIteratorIterable() {
        EmptyIterator<String> it = new EmptyIterator<>();
        assertSame(it, it.iterator());
    }

    // ==================== LineIterator ====================

    @Test
    public void testLineIteratorBasic() {
        StringReader sr = new StringReader("line1\nline2\nline3");
        LineIterator it = new LineIterator(sr);

        assertTrue(it.hasNext());
        assertEquals("line1", it.next());
        assertEquals("line2", it.next());
        assertEquals("line3", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testLineIteratorSkipLine() {
        StringReader sr = new StringReader("a\nb\nc\nd");
        LineIterator it = new LineIterator(sr);
        it.skipLine(2);
        assertTrue(it.hasNext());
        assertEquals("c", it.next());
    }

    @Test
    public void testLineIteratorClose() {
        StringReader sr = new StringReader("hello\nworld");
        LineIterator it = new LineIterator(sr);
        it.autoClose(true);
        it.close();
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testLineIteratorNextOnEmpty() {
        StringReader sr = new StringReader("");
        LineIterator it = new LineIterator(sr);
        it.next();
    }

    // ==================== MapIterator ====================

    @Test
    public void testMapIteratorBasic() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        MapIterator<String, Integer> it = new MapIterator<>();
        it.setMap(map);

        assertTrue(it.hasNext());
        Map.Entry<String, Integer> entry = it.next();
        assertEquals("a", entry.getKey());
        assertEquals(Integer.valueOf(1), entry.getValue());
    }

    @Test
    public void testMapIteratorIterable() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("x", 10);
        map.put("y", 20);
        MapIterator<String, Integer> it = new MapIterator<>();
        it.setMap(map);

        int count = 0;
        for (Map.Entry<String, Integer> entry : it) {
            count++;
        }
        assertEquals(2, count);
    }

    // ==================== SingletonIterator ====================

    @Test
    public void testSingletonIteratorBasic() {
        SingletonIterator<String> it = new SingletonIterator<>("only");
        assertTrue(it.hasNext());
        assertEquals("only", it.next());
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingletonIteratorNextTwice() {
        SingletonIterator<String> it = new SingletonIterator<>("one");
        it.next();
        it.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonIteratorRemove() {
        SingletonIterator<String> it = new SingletonIterator<>("x");
        it.remove();
    }

    @Test
    public void testSingletonIteratorForEachRemaining() {
        SingletonIterator<String> it = new SingletonIterator<>("val");
        List<String> result = new ArrayList<>();
        it.forEachRemaining(result::add);
        assertEquals(1, result.size());
        assertEquals("val", result.get(0));
        assertFalse(it.hasNext());
    }
}

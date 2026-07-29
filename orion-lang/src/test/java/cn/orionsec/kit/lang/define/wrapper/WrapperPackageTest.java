package cn.orionsec.kit.lang.define.wrapper;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * wrapper 包单元测试
 */
public class WrapperPackageTest {

    // ==================== HttpWrapper ====================

    @Test
    public void testHttpWrapperOk() {
        HttpWrapper<String> wrapper = HttpWrapper.ok("data");
        assertEquals(HttpWrapper.HTTP_OK_CODE.intValue(), wrapper.getCode());
        assertEquals(HttpWrapper.HTTP_OK_MESSAGE, wrapper.getMsg());
        assertEquals("data", wrapper.getData());
        assertTrue(wrapper.isOk());
    }

    @Test
    public void testHttpWrapperError() {
        HttpWrapper<String> wrapper = HttpWrapper.error("fail");
        assertEquals(HttpWrapper.HTTP_ERROR_CODE.intValue(), wrapper.getCode());
        assertEquals("fail", wrapper.getMsg());
        assertNull(wrapper.getData());
        assertFalse(wrapper.isOk());
    }

    @Test
    public void testHttpWrapperChain() {
        HttpWrapper<String> wrapper = HttpWrapper.<String>get()
                .code(200)
                .msg("success")
                .data("content");
        assertEquals(200, wrapper.getCode());
        assertEquals("success", wrapper.getMsg());
        assertEquals("content", wrapper.getData());
    }

    @Test
    public void testHttpWrapperMap() {
        HttpWrapper<Integer> wrapper = HttpWrapper.ok(10);
        HttpWrapper<String> mapped = wrapper.map(Object::toString);
        assertEquals("10", mapped.getData());
        assertEquals(wrapper.getCode(), mapped.getCode());
    }

    @Test
    public void testHttpWrapperToMap() {
        HttpWrapper<String> wrapper = HttpWrapper.ok("val");
        Map<String, Object> map = wrapper.toMap();
        assertEquals(HttpWrapper.HTTP_OK_CODE, map.get("code"));
        assertEquals(HttpWrapper.HTTP_OK_MESSAGE, map.get("msg"));
        assertEquals("val", map.get("data"));
    }

    @Test
    public void testHttpWrapperOptional() {
        HttpWrapper<String> ok = HttpWrapper.ok("hello");
        Optional<String> opt = ok.optional();
        assertTrue(opt.isPresent());
        assertEquals("hello", opt.get());

        HttpWrapper<String> err = HttpWrapper.error("err");
        Optional<String> optErr = err.optional();
        assertFalse(optErr.isPresent());
    }

    // ==================== RpcWrapper ====================

    @Test
    public void testRpcWrapperSuccess() {
        RpcWrapper<String> wrapper = RpcWrapper.success("data");
        assertEquals(RpcWrapper.RPC_SUCCESS_CODE.intValue(), wrapper.getCode());
        assertEquals("data", wrapper.getData());
        assertTrue(wrapper.isSuccess());
        assertNotNull(wrapper.getTraceId());
    }

    @Test
    public void testRpcWrapperError() {
        RpcWrapper<String> wrapper = RpcWrapper.error("fail");
        assertEquals(RpcWrapper.RPC_ERROR_CODE.intValue(), wrapper.getCode());
        assertEquals("fail", wrapper.getMsg());
        assertFalse(wrapper.isSuccess());
    }

    @Test
    public void testRpcWrapperChain() {
        RpcWrapper<Integer> wrapper = RpcWrapper.<Integer>get()
                .code(200)
                .msg("ok")
                .data(42);
        assertEquals(200, wrapper.getCode());
        assertEquals("ok", wrapper.getMsg());
        assertEquals(Integer.valueOf(42), wrapper.getData());
    }

    @Test
    public void testRpcWrapperErrorMessages() {
        RpcWrapper<String> wrapper = RpcWrapper.success();
        wrapper.addErrorMessage("err1");
        wrapper.addErrorMessage("err2");
        assertEquals(2, wrapper.getErrorMessages().size());
        assertFalse(wrapper.isSuccess());
    }

    @Test
    public void testRpcWrapperMap() {
        RpcWrapper<Integer> wrapper = RpcWrapper.success(100);
        RpcWrapper<String> mapped = wrapper.map(Object::toString);
        assertEquals("100", mapped.getData());
    }

    @Test
    public void testRpcWrapperToHttpWrapper() {
        RpcWrapper<String> rpc = RpcWrapper.success("data");
        HttpWrapper<String> http = rpc.toHttpWrapper();
        assertEquals(rpc.getCode(), http.getCode());
        assertEquals(rpc.getMsg(), http.getMsg());
        assertEquals(rpc.getData(), http.getData());
    }

    // ==================== Pair ====================

    @Test
    public void testPairBasic() {
        Pair<String, Integer> pair = Pair.of("key", 42);
        assertEquals("key", pair.getKey());
        assertEquals(Integer.valueOf(42), pair.getValue());
    }

    @Test
    public void testPairSetValue() {
        Pair<String, Integer> pair = new Pair<>("k", 1);
        pair.setValue(2);
        assertEquals(Integer.valueOf(2), pair.getValue());
    }

    @Test
    public void testPairEquals() {
        Pair<String, Integer> a = Pair.of("k", 1);
        Pair<String, Integer> b = Pair.of("k", 1);
        assertEquals(a, b);
    }

    @Test
    public void testPairToString() {
        Pair<String, Integer> pair = Pair.of("hello", 42);
        assertEquals("hello=42", pair.toString());
    }

    @Test
    public void testPairOptional() {
        Pair<String, Integer> pair = Pair.of("k", null);
        assertTrue(pair.keyOptional().isPresent());
        assertFalse(pair.valueOptional().isPresent());
    }

    // ==================== Tuple ====================

    @Test
    public void testTupleBasic() {
        Tuple tuple = Tuple.of("a", 1, true);
        assertEquals(3, tuple.size());
        assertEquals("a", tuple.get(0));
        assertEquals(1, (int) tuple.get(1));
        assertEquals(true, tuple.get(2));
    }

    @Test
    public void testTupleEquals() {
        Tuple a = Tuple.of(1, "two", 3.0);
        Tuple b = Tuple.of(1, "two", 3.0);
        assertEquals(a, b);
    }

    @Test
    public void testTupleIsNotEmpty() {
        Tuple tuple = Tuple.of("x");
        assertTrue(tuple.isNotEmpty());
        assertFalse(tuple.isEmpty());
    }

    @Test
    public void testTupleOptional() {
        Tuple tuple = Tuple.of("a", null, "c");
        Optional<String> opt = tuple.optional(0);
        assertTrue(opt.isPresent());
        assertEquals("a", opt.get());

        Optional<String> optNull = tuple.optional(1);
        assertFalse(optNull.isPresent());
    }

    @Test
    public void testTupleIterator() {
        Tuple tuple = Tuple.of(1, 2, 3);
        int count = 0;
        for (Object o : tuple) {
            count++;
        }
        assertEquals(3, count);
    }

    // ==================== Ref ====================

    @Test
    public void testRefBasic() {
        Ref<String> ref = Ref.of("hello");
        assertEquals("hello", ref.get());
        ref.set("world");
        assertEquals("world", ref.get());
    }

    @Test
    public void testRefMap() {
        Ref<Integer> ref = Ref.of(42);
        Ref<String> mapped = ref.map(Object::toString);
        assertEquals("42", mapped.get());
    }

    @Test
    public void testRefEquals() {
        Ref<String> a = Ref.of("test");
        Ref<String> b = Ref.of("test");
        assertEquals(a, b);
    }

    @Test
    public void testRefOptional() {
        Ref<String> ref = Ref.of("val");
        assertTrue(ref.optional().isPresent());
        Ref<String> empty = new Ref<>();
        assertFalse(empty.optional().isPresent());
    }

    // ==================== PageRequest ====================

    @Test
    public void testPageRequestDefault() {
        PageRequest pr = PageRequest.of();
        assertEquals(1, pr.getPage());
        assertTrue(pr.getLimit() > 0);
    }

    @Test
    public void testPageRequestCustom() {
        PageRequest pr = PageRequest.of(3, 20);
        assertEquals(3, pr.getPage());
        assertEquals(20, pr.getLimit());
    }

    @Test
    public void testPageRequestSetters() {
        PageRequest pr = new PageRequest();
        pr.setPage(5);
        pr.setLimit(50);
        assertEquals(5, pr.getPage());
        assertEquals(50, pr.getLimit());
    }

    // ==================== TimestampValue ====================

    @Test
    public void testTimestampValueBasic() {
        TimestampValue<String> tv = TimestampValue.of(1000L, "data");
        assertEquals(Long.valueOf(1000L), tv.getTime());
        assertEquals("data", tv.getValue());
    }

    @Test
    public void testTimestampValueSetters() {
        TimestampValue<String> tv = new TimestampValue<>();
        tv.setTime(2000L);
        tv.setValue("val");
        assertEquals(Long.valueOf(2000L), tv.getTime());
        assertEquals("val", tv.getValue());
    }

    @Test
    public void testTimestampValueEquals() {
        TimestampValue<String> a = TimestampValue.of(100L, "x");
        TimestampValue<String> b = TimestampValue.of(100L, "x");
        assertEquals(a, b);
    }

    @Test
    public void testTimestampValueMap() {
        TimestampValue<Integer> tv = TimestampValue.of(1L, 42);
        TimestampValue<String> mapped = tv.map(Object::toString);
        assertEquals("42", mapped.getValue());
        assertEquals(Long.valueOf(1L), mapped.getTime());
    }

    @Test
    public void testTimestampValueOptional() {
        TimestampValue<String> tv = TimestampValue.of(1L, "val");
        assertTrue(tv.optional().isPresent());
        TimestampValue<String> empty = TimestampValue.of(1L, null);
        assertFalse(empty.optional().isPresent());
    }
}

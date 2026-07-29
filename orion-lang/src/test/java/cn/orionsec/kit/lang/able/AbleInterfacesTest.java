package cn.orionsec.kit.lang.able;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * able 包接口单元测试
 * <p>
 * 验证所有接口可以通过 lambda/匿名类实现, 并测试有默认方法的接口行为
 */
public class AbleInterfacesTest {

    // ==================== 简单函数式接口 lambda 实例化测试 ====================

    @Test
    public void testAdaptable() {
        Adaptable<String> adaptable = () -> "adapted";
        assertEquals("adapted", adaptable.forNew());
    }

    @Test
    public void testAnalysable() {
        AtomicBoolean called = new AtomicBoolean(false);
        Analysable analysable = () -> called.set(true);
        analysable.analysis();
        assertTrue(called.get());
    }

    @Test
    public void testAsyncable() {
        AtomicReference<String> ref = new AtomicReference<>();
        Asyncable<String> asyncable = ref::set;
        asyncable.async("handler");
        assertEquals("handler", ref.get());
    }

    @Test
    public void testAwaitable() {
        Awaitable<Integer> awaitable = () -> 42;
        assertEquals(Integer.valueOf(42), awaitable.await());
    }

    @Test
    public void testBuildable() {
        Buildable<String> buildable = () -> "built";
        assertEquals("built", buildable.build());
    }

    @Test
    public void testCancelable() {
        AtomicBoolean cancelled = new AtomicBoolean(false);
        Cancelable cancelable = () -> cancelled.set(true);
        cancelable.cancel();
        assertTrue(cancelled.get());
    }

    @Test
    public void testConnectable() {
        AtomicBoolean connected = new AtomicBoolean(false);
        Connectable connectable = () -> connected.set(true);
        connectable.connect();
        assertTrue(connected.get());
    }

    @Test
    public void testCountable() {
        Countable countable = () -> 10;
        assertEquals(10, countable.count());
    }

    @Test
    public void testDestroyable() {
        AtomicBoolean destroyed = new AtomicBoolean(false);
        Destroyable destroyable = () -> destroyed.set(true);
        destroyable.destroy();
        assertTrue(destroyed.get());
    }

    @Test
    public void testDisConnectable() {
        AtomicBoolean disconnected = new AtomicBoolean(false);
        DisConnectable disConnectable = () -> disconnected.set(true);
        disConnectable.disconnect();
        assertTrue(disconnected.get());
    }

    @Test
    public void testExecutable() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Executable executable = () -> executed.set(true);
        executable.exec();
        assertTrue(executed.get());
    }

    @Test
    public void testGettable() {
        Gettable<String> gettable = () -> "value";
        assertEquals("value", gettable.get());
    }

    @Test
    public void testIArrayObject() {
        IArrayObject<String> arrayObject = () -> new String[]{"a", "b", "c"};
        String[] arr = arrayObject.toArray();
        assertEquals(3, arr.length);
        assertEquals("a", arr[0]);
    }

    @Test
    public void testIDoneEvent() {
        AtomicBoolean done = new AtomicBoolean(false);
        IDoneEvent event = () -> done.set(true);
        event.onDone();
        assertTrue(done.get());
    }

    @Test
    public void testIEntryObject() {
        IEntryObject<String, Integer> entry = new IEntryObject<String, Integer>() {
            @Override
            public String getKey() {
                return "key";
            }

            @Override
            public Integer getValue() {
                return 100;
            }
        };
        assertEquals("key", entry.getKey());
        assertEquals(Integer.valueOf(100), entry.getValue());
    }

    @Test
    public void testIErrorEvent() {
        AtomicReference<Exception> ref = new AtomicReference<>();
        IErrorEvent event = ref::set;
        RuntimeException ex = new RuntimeException("test");
        event.onError(ex);
        assertSame(ex, ref.get());
    }

    @Test
    public void testIInitialEvent() {
        AtomicBoolean initialized = new AtomicBoolean(false);
        IInitialEvent event = () -> initialized.set(true);
        event.init();
        assertTrue(initialized.get());
    }

    @Test
    public void testIKeyObject() {
        IKeyObject<Long> keyObject = () -> 1L;
        assertEquals(Long.valueOf(1L), keyObject.getKey());
    }

    @Test
    public void testILock() {
        ILock lock = new ILock() {
            private boolean locked = false;

            @Override
            public boolean tryLock() {
                locked = true;
                return true;
            }

            @Override
            public void unLock() {
                locked = false;
            }

            @Override
            public boolean isLocked() {
                return locked;
            }
        };
        assertFalse(lock.isLocked());
        assertTrue(lock.tryLock());
        assertTrue(lock.isLocked());
        lock.unLock();
        assertFalse(lock.isLocked());
    }

    @Test
    public void testILogObject() {
        ILogObject logObject = () -> "log message";
        assertEquals("log message", logObject.toLogString());
    }

    @Test
    public void testISendEvent() {
        AtomicReference<String> ref = new AtomicReference<>();
        ISendEvent<String> event = ref::set;
        event.send("hello");
        assertEquals("hello", ref.get());
    }

    @Test
    public void testIStopEvent() {
        AtomicBoolean stopped = new AtomicBoolean(false);
        IStopEvent event = () -> stopped.set(true);
        event.onStop();
        assertTrue(stopped.get());
    }

    @Test
    public void testISuccessEvent() {
        AtomicBoolean success = new AtomicBoolean(false);
        ISuccessEvent event = () -> success.set(true);
        event.onSuccess();
        assertTrue(success.get());
    }

    @Test
    public void testIValueObject() {
        IValueObject<String> valueObject = () -> "val";
        assertEquals("val", valueObject.getValue());
    }

    @Test
    public void testIdGenerator() {
        AtomicInteger counter = new AtomicInteger(0);
        IdGenerator<Integer> generator = counter::incrementAndGet;
        assertEquals(Integer.valueOf(1), generator.nextId());
        assertEquals(Integer.valueOf(2), generator.nextId());
    }

    @Test
    public void testMutable() {
        Mutable<String> mutable = new Mutable<String>() {
            private String value;

            @Override
            public String get() {
                return value;
            }

            @Override
            public void set(String s) {
                this.value = s;
            }
        };
        assertNull(mutable.get());
        mutable.set("hello");
        assertEquals("hello", mutable.get());
    }

    @Test
    public void testProcessable() {
        Processable<String, Integer> processable = String::length;
        assertEquals(Integer.valueOf(5), processable.execute("hello"));
    }

    @Test
    public void testRenewable() {
        AtomicBoolean resumed = new AtomicBoolean(false);
        Renewable renewable = () -> resumed.set(true);
        renewable.resume();
        assertTrue(resumed.get());
    }

    @Test
    public void testSafeCloseable() {
        AtomicBoolean closed = new AtomicBoolean(false);
        SafeCloseable closeable = () -> closed.set(true);
        closeable.close();
        assertTrue(closed.get());
    }

    @Test
    public void testSafeFlushable() {
        AtomicBoolean flushed = new AtomicBoolean(false);
        SafeFlushable flushable = () -> flushed.set(true);
        flushable.flush();
        assertTrue(flushed.get());
    }

    @Test
    public void testSettable() {
        AtomicReference<String> ref = new AtomicReference<>();
        Settable<String> settable = ref::set;
        settable.set("value");
        assertEquals("value", ref.get());
    }

    @Test
    public void testStoppable() {
        AtomicBoolean stopped = new AtomicBoolean(false);
        Stoppable stoppable = () -> stopped.set(true);
        stoppable.stop();
        assertTrue(stopped.get());
    }

    @Test
    public void testWatchable() {
        AtomicBoolean watched = new AtomicBoolean(false);
        Watchable watchable = () -> watched.set(true);
        watchable.watch();
        assertTrue(watched.get());
    }

    // ==================== 有默认方法的接口测试 ====================

    @Test
    public void testIHttpResponse_isOk_with200() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com";
            }

            @Override
            public int getCode() {
                return 200;
            }
        };
        assertTrue(response.isOk());
        assertEquals("http://example.com", response.getUrl());
    }

    @Test
    public void testIHttpResponse_isOk_with201() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com/resource";
            }

            @Override
            public int getCode() {
                return 201;
            }
        };
        assertTrue(response.isOk());
    }

    @Test
    public void testIHttpResponse_isOk_with299() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com";
            }

            @Override
            public int getCode() {
                return 299;
            }
        };
        assertTrue(response.isOk());
    }

    @Test
    public void testIHttpResponse_isOk_with300() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com";
            }

            @Override
            public int getCode() {
                return 300;
            }
        };
        assertFalse(response.isOk());
    }

    @Test
    public void testIHttpResponse_isOk_with404() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com/notfound";
            }

            @Override
            public int getCode() {
                return 404;
            }
        };
        assertFalse(response.isOk());
    }

    @Test
    public void testIHttpResponse_isOk_with500() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com/error";
            }

            @Override
            public int getCode() {
                return 500;
            }
        };
        assertFalse(response.isOk());
    }

    @Test
    public void testIHttpResponse_isOk_with199() {
        IHttpResponse response = new IHttpResponse() {
            @Override
            public String getUrl() {
                return "http://example.com";
            }

            @Override
            public int getCode() {
                return 199;
            }
        };
        assertFalse(response.isOk());
    }

    @Test
    public void testIJsonObject_toJsonString() {
        IJsonObject jsonObject = new IJsonObject() {
        };
        String json = jsonObject.toJsonString();
        assertNotNull(json);
    }

    @Test
    public void testIMapObject_toMap() {
        IMapObject<String, Object> mapObject = new IMapObject<String, Object>() {
            @Override
            public Map<String, Object> toMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("key", "value");
                return map;
            }
        };
        Map<String, Object> map = mapObject.toMap();
        assertNotNull(map);
        assertEquals("value", map.get("key"));
    }

    @Test
    public void testBeanConvertible_convert() {
        BeanConvertible convertible = new BeanConvertible() {
            @Override
            public <T> T convert(Class<T> clazz) {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    return null;
                }
            }
        };
        Object result = convertible.convert(Object.class);
        assertNotNull(result);
    }

}

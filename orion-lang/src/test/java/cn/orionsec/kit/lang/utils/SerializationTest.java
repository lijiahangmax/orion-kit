package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.define.Null;
import cn.orionsec.kit.lang.define.Void;
import cn.orionsec.kit.lang.define.collect.ConcurrentReferenceHashMap;
import cn.orionsec.kit.lang.define.collect.EmptyList;
import cn.orionsec.kit.lang.define.collect.EmptyMap;
import cn.orionsec.kit.lang.define.collect.EmptySet;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.lang.utils.time.cron.Cron;
import org.junit.Test;

import java.io.ObjectInputFilter;

import static org.junit.Assert.*;

/**
 * 序列化相关测试
 */
public class SerializationTest {

    @Test
    public void testConcurrentReferenceHashMap() {
        ConcurrentReferenceHashMap<String, String> map = new ConcurrentReferenceHashMap<>();
        map.put("k", "v");
        ConcurrentReferenceHashMap<String, String> copy = Objects1.deserialize(Objects1.serialize(map));
        assertEquals("v", copy.get("k"));
    }

    @Test
    public void testCron() {
        Cron copy = Objects1.deserialize(Objects1.serialize(new Cron("0 0 12 * * ?")));
        assertTrue(copy.isSatisfiedBy(Dates.build(2026, 10, 18, 12, 0, 0)));
    }

    @Test
    public void testSingletonIdentity() {
        assertSame(EmptyList.EMPTY, Objects1.deserialize(Objects1.serialize(EmptyList.EMPTY)));
        assertSame(EmptyMap.EMPTY, Objects1.deserialize(Objects1.serialize(EmptyMap.EMPTY)));
        assertSame(EmptySet.EMPTY, Objects1.deserialize(Objects1.serialize(EmptySet.EMPTY)));
        assertSame(Null.VALUE, Objects1.deserialize(Objects1.serialize(Null.VALUE)));
        assertSame(Void.VALUE, Objects1.deserialize(Objects1.serialize(Void.VALUE)));
    }

    @Test
    public void testDeserializeWithFilter() {
        byte[] bytes = Objects1.serialize(new java.util.ArrayList<>(java.util.List.of("data")));
        assertThrows(IllegalArgumentException.class,
                () -> Objects1.deserialize(bytes, info -> ObjectInputFilter.Status.REJECTED));
    }

}

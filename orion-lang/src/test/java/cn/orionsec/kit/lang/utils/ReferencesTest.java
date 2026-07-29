package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.lang.ref.Reference;

import static org.junit.Assert.*;

public class ReferencesTest {

    @Test
    public void testCreateSoftReference() {
        Reference<String> ref = References.create(References.ReferenceType.SOFT, "hello");
        assertNotNull(ref);
        assertEquals("hello", ref.get());
    }

    @Test
    public void testCreateWeakReference() {
        Reference<String> ref = References.create(References.ReferenceType.WEAK, "world");
        assertNotNull(ref);
        // weak reference may be collected, but right after creation should exist
        assertNotNull(ref.get());
    }

    @Test
    public void testCreatePhantomReference() {
        Reference<String> ref = References.create(References.ReferenceType.PHANTOM, "phantom");
        assertNotNull(ref);
        // phantom reference get() always returns null
        assertNull(ref.get());
    }
}

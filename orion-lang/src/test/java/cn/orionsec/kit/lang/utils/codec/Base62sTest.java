package cn.orionsec.kit.lang.utils.codec;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Base62s 单元测试
 */
public class Base62sTest {

    @Test
    public void testEncodeString() {
        String encoded = Base62s.encode("hello");
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
    }

    @Test
    public void testDecodeString() {
        String original = "hello world";
        String encoded = Base62s.encode(original);
        String decoded = Base62s.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeGmp() {
        String original = "Test Base62 GMP";
        String encoded = Base62s.encode(original, true);
        String decoded = Base62s.decode(encoded, true);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeInverted() {
        String original = "Test Base62 Inverted";
        String encoded = Base62s.encode(original, false);
        String decoded = Base62s.decode(encoded, false);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeBytes() {
        byte[] original = {10, 20, 30, 40, 50};
        byte[] encoded = Base62s.encode(original);
        byte[] decoded = Base62s.decode(encoded);
        assertArrayEquals(original, decoded);
    }

    @Test
    public void testEncodeDecodeBytesInverted() {
        byte[] original = {1, 2, 3, 4, 5, 6, 7, 8};
        byte[] encoded = Base62s.encode(original, false);
        byte[] decoded = Base62s.decode(encoded, false);
        assertArrayEquals(original, decoded);
    }
}

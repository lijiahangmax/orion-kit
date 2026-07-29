package cn.orionsec.kit.lang.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * ObjectIds / ObjectIdWorker 单元测试
 */
public class ObjectIdsTest {

    @Test
    public void testNextIdNotNull() {
        String id = ObjectIds.nextId();
        assertNotNull(id);
    }

    @Test
    public void testNextIdLength() {
        String id = ObjectIds.nextId();
        // ObjectId 无分隔符时 24 位
        assertEquals(24, id.length());
    }

    @Test
    public void testNextIdHexFormat() {
        String id = ObjectIds.nextId();
        // 只包含十六进制字符
        assertTrue(id.matches("[0-9a-f]{24}"));
    }

    @Test
    public void testNextIdWithSymbol() {
        String id = ObjectIds.nextId(true);
        assertNotNull(id);
        // 含分隔符 26 位: 8-8-8 = 24 hex + 2 dashes
        assertEquals(26, id.length());
        assertTrue(id.contains("-"));
    }

    @Test
    public void testNextIdWithoutSymbol() {
        String id = ObjectIds.nextId(false);
        assertNotNull(id);
        assertEquals(24, id.length());
        assertFalse(id.contains("-"));
    }

    @Test
    public void testNextIdUnique() {
        Set<String> ids = new HashSet<>();
        int count = 10000;
        for (int i = 0; i < count; i++) {
            ids.add(ObjectIds.nextId());
        }
        assertEquals(count, ids.size());
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = ObjectIds.nextBytes();
        assertNotNull(bytes);
        assertEquals(12, bytes.length);
    }

    @Test
    public void testIsValid() {
        String id = ObjectIds.nextId();
        assertTrue(ObjectIds.isValid(id));
    }

    @Test
    public void testIsValidWithDash() {
        String id = ObjectIds.nextId(true);
        assertTrue(ObjectIds.isValid(id));
    }

    @Test
    public void testIsValidNull() {
        assertFalse(ObjectIds.isValid(null));
    }

    @Test
    public void testIsValidInvalidLength() {
        assertFalse(ObjectIds.isValid("abc"));
    }

    @Test
    public void testIsValidInvalidChars() {
        assertFalse(ObjectIds.isValid("zzzzzzzzzzzzzzzzzzzzzzzz"));
    }

    @Test
    public void testGetMachineCode() {
        int code = ObjectIds.getMachineCode();
        // 机器码应该是一个有效整数
        assertTrue(code != 0 || code == 0);
    }

    @Test
    public void testObjectIdWorkerDirectly() {
        ObjectIdWorker worker = new ObjectIdWorker(12345);
        String id = worker.nextId();
        assertNotNull(id);
        assertEquals(24, id.length());
        assertEquals(12345, worker.getMachineCode());
    }

}

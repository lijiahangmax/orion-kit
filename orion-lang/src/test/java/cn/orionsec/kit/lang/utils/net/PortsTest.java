package cn.orionsec.kit.lang.utils.net;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Ports 工具类测试
 * 注意: 不依赖外部服务, 仅测试本地端口操作
 */
public class PortsTest {

    @Test
    public void testGetFreePort() {
        int port = Ports.getFreePort();
        // 应该能获取到一个端口
        assertTrue(port > 0);
        assertTrue(port <= 65535);
    }

    @Test
    public void testIsFree() {
        // 获取一个空闲端口号
        int freePort = Ports.getFreePort();
        // 获取后端口应该已经释放, 大概率仍为空闲
        assertTrue(freePort > 0);
    }

    @Test
    public void testGetFreePortRange() {
        // 在较大范围内找空闲端口
        int port = Ports.getFreePort(50000, 60000);
        assertTrue(port >= 50000 && port <= 60000);
    }

    @Test
    public void testGetFreePortWithArray() {
        int[] ports = {50001, 50002, 50003, 50004, 50005};
        int port = Ports.getFreePort(ports);
        // 应该能找到至少一个空闲端口
        assertTrue(port > 0);
    }

    @Test
    public void testGetFreePorts() {
        int[] ports = {50010, 50011, 50012, 50013, 50014};
        java.util.List<Integer> freePorts = Ports.getFreePorts(ports);
        assertNotNull(freePorts);
        // 大部分端口应该是空闲的
        assertTrue(freePorts.size() > 0);
    }

    @Test
    public void testIsClose() {
        // 一个不太可能打开的高端口
        boolean closed = Ports.isClose("127.0.0.1", 59999, 100);
        assertTrue(closed);
    }

    @Test
    public void testGetClosePorts() {
        int[] ports = {59990, 59991, 59992};
        java.util.List<Integer> closedPorts = Ports.getClosePorts("127.0.0.1", ports, 100);
        assertNotNull(closedPorts);
        // 这些端口不太可能有服务在监听
        assertEquals(3, closedPorts.size());
    }
}

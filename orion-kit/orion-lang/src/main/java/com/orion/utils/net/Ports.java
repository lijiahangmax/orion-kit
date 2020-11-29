package com.orion.utils.net;

import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 端口工具
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/16 11:37
 */
public class Ports {

    private Ports() {
    }

    /**
     * 默认建立连接超时时间
     */
    private static int timeout = 1000;

    /**
     * 获取一个空闲的本地端口
     *
     * @return 端口  没有返回 -1
     */
    public static int getFreePort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            Streams.close(serverSocket);
            return port;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取一个空闲的本地端口
     *
     * @param ports ports
     * @return 端口  没有返回 -1
     */
    public static int getFreePort(int[] ports) {
        for (int port : ports) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Streams.close(serverSocket);
                return port;
            } catch (Exception e) {
                // try next port
            }
        }
        return -1;
    }

    /**
     * 获取一个空闲的本地端口
     *
     * @param start 端口开始
     * @param end   端口结束
     * @return 端口 没有返回 -1
     */
    public static int getFreePort(int start, int end) {
        Valid.gte(start, 1001, "start port must greater than 1000");
        Valid.lte(end, 65535, "end port must less than 65536");
        Valid.gt(end, start, "end port must greater than start");
        for (int i = start; i <= end; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(i);
                Streams.close(serverSocket);
                return i;
            } catch (IOException ex) {
                // try next port
            }
        }
        return -1;
    }

    /**
     * 获取空闲的本地端口
     *
     * @param ports ports
     * @return 端口
     */
    public static List<Integer> getFreePorts(int[] ports) {
        List<Integer> freePorts = new ArrayList<>();
        for (int port : ports) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Streams.close(serverSocket);
                freePorts.add(port);
            } catch (Exception e) {
                // try next port
            }
        }
        return freePorts;
    }

    /**
     * 获取空闲的本地端口
     *
     * @param start 端口开始
     * @param end   端口结束
     * @return 端口
     */
    public static List<Integer> getFreePorts(int start, int end) {
        Valid.gte(start, 1001, "start port must greater than 1000");
        Valid.lte(end, 65535, "end port must less than 65536");
        Valid.gt(end, start, "end port must greater than start");
        List<Integer> freePorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(i);
                Streams.close(serverSocket);
                freePorts.add(i);
            } catch (IOException ex) {
                // try next port
            }
        }
        return freePorts;
    }

    /**
     * 获取占用的本地端口
     *
     * @param ports ports
     * @return 端口
     */
    public static List<Integer> getUsedPorts(int[] ports) {
        List<Integer> usedPorts = new ArrayList<>();
        for (int port : ports) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Streams.close(serverSocket);
            } catch (Exception e) {
                usedPorts.add(port);
            }
        }
        return usedPorts;
    }

    /**
     * 获取占用的本地端口
     *
     * @param start 端口开始
     * @param end   端口结束
     * @return 端口
     */
    public static List<Integer> getUsedPorts(int start, int end) {
        Valid.gte(start, 1001, "start port must greater than 1000");
        Valid.lte(end, 65535, "end port must less than 65536");
        Valid.gt(end, start, "end port must greater than start");
        List<Integer> usedPorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                ServerSocket serverSocket = new ServerSocket(i);
                Streams.close(serverSocket);
            } catch (IOException ex) {
                usedPorts.add(i);
            }
        }
        return usedPorts;
    }

    /**
     * 判断端口是否空闲
     *
     * @param port 端口
     * @return true空闲
     */
    public static boolean isFree(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Streams.close(serverSocket);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * 判断端口是否占用
     *
     * @param port 端口
     * @return true占用
     */
    public static boolean isUsed(int port) {
        return isFree(port);
    }

    /**
     * 获取已开启的端口
     *
     * @param host  主机
     * @param ports 端口
     * @return 已开启的端口
     */
    public static List<Integer> getOpenPorts(String host, int[] ports) {
        List<Integer> openPorts = new ArrayList<>();
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                Streams.close(socket);
                openPorts.add(port);
            } catch (Exception e) {
                // try next port
            }
        }
        return openPorts;
    }

    /**
     * 获取已开启的端口
     *
     * @param host  主机
     * @param start 端口开始
     * @param end   端口结束
     * @return 已开启的端口
     */
    public static List<Integer> getOpenPorts(String host, int start, int end) {
        Valid.gt(start, 0, "start port must greater than 0");
        Valid.lte(end, 65535, "end port must less than 65536");
        Valid.gt(end, start, "end port must greater than start");
        List<Integer> openPorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, i), timeout);
                Streams.close(socket);
                openPorts.add(i);
            } catch (IOException ex) {
                // try next port
            }
        }
        return openPorts;
    }

    /**
     * 获取未开启的端口
     *
     * @param host  主机
     * @param ports 端口
     * @return 已开启的端口
     */
    public static List<Integer> getClosePorts(String host, int[] ports) {
        List<Integer> closePorts = new ArrayList<>();
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                Streams.close(socket);
            } catch (Exception e) {
                closePorts.add(port);
            }
        }
        return closePorts;
    }

    /**
     * 获取未开启的端口
     *
     * @param host  主机
     * @param start 端口开始
     * @param end   端口结束
     * @return 已开启的端口
     */
    public static List<Integer> getClosePorts(String host, int start, int end) {
        Valid.gt(start, 0, "start port must greater than 0");
        Valid.lte(end, 65535, "end port must less than 65536");
        Valid.gt(end, start, "end port must greater than start");
        List<Integer> closePorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, i), timeout);
                Streams.close(socket);
            } catch (IOException ex) {
                closePorts.add(i);
            }
        }
        return closePorts;
    }

    /**
     * 判断端口是否开启
     *
     * @param host 主机
     * @param port 端口
     * @return true开启
     */
    public static boolean isOpen(String host, int port) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
            Streams.close(socket);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断端口是否关闭
     *
     * @param host 主机
     * @param port 端口
     * @return true关闭
     */
    public static boolean isClose(String host, int port) {
        return !isOpen(host, port);
    }

    /**
     * 获取当前建立连接超时时间
     *
     * @return 超时时间ms
     */
    public static int getTimeout() {
        return timeout;
    }

    /**
     * 设置建立连接超时时间
     *
     * @param timeout 超时时间ms
     */
    public static void setTimeout(int timeout) {
        Ports.timeout = timeout;
    }

}

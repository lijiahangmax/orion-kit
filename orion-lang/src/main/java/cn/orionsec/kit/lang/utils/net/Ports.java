/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.net;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 端口工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/16 11:37
 */
public class Ports {

    private static final int TIMEOUT = Const.MS_S_3;

    private Ports() {
    }

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
        Assert.gte(start, 1001, "start port must greater than 1000");
        Assert.lte(end, 65535, "end port must less than 65536");
        Assert.gt(end, start, "end port must greater than start");
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
        Assert.gte(start, 1001, "start port must greater than 1000");
        Assert.lte(end, 65535, "end port must less than 65536");
        Assert.gt(end, start, "end port must greater than start");
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
        Assert.gte(start, 1001, "start port must greater than 1000");
        Assert.lte(end, 65535, "end port must less than 65536");
        Assert.gt(end, start, "end port must greater than start");
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
     * 判断端口是否开启
     *
     * @param host host
     * @param port port
     * @return opened
     */
    public static boolean isOpen(String host, int port) {
        return isOpen(host, port, TIMEOUT);
    }

    /**
     * 判断端口是否开启
     *
     * @param host    host
     * @param port    port
     * @param timeout timeout
     * @return opened
     */
    public static boolean isOpen(String host, int port, int timeout) {
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
     * 判断端口是否关闭
     *
     * @param host    host
     * @param port    port
     * @param timeout timeout
     * @return true关闭
     */
    public static boolean isClose(String host, int port, int timeout) {
        return !isOpen(host, port, timeout);
    }

    /**
     * 获取已开启的端口
     *
     * @param host  host
     * @param ports ports
     * @return openedPorts
     */
    public static List<Integer> getOpenPorts(String host, int[] ports) {
        return getOpenPorts(host, ports, TIMEOUT);
    }

    /**
     * 获取已开启的端口
     *
     * @param host    host
     * @param ports   ports
     * @param timeout timeout
     * @return openedPorts
     */
    public static List<Integer> getOpenPorts(String host, int[] ports, int timeout) {
        List<Integer> openedPorts = new ArrayList<>();
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                Streams.close(socket);
                openedPorts.add(port);
            } catch (Exception e) {
                // try next port
            }
        }
        return openedPorts;
    }

    /**
     * 获取已开启的端口
     *
     * @param host  host
     * @param start start
     * @param end   end
     * @return openedPorts
     */
    public static List<Integer> getOpenPorts(String host, int start, int end) {
        return getOpenPorts(host, start, end, TIMEOUT);
    }

    /**
     * 获取已开启的端口
     *
     * @param host    host
     * @param start   start
     * @param end     end
     * @param timeout timeout
     * @return openedPorts
     */
    public static List<Integer> getOpenPorts(String host, int start, int end, int timeout) {
        Assert.gt(start, 0, "start port must greater than 0");
        Assert.lte(end, 65535, "end port must less than 65536");
        Assert.gt(end, start, "end port must greater than start");
        List<Integer> openedPorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, i), timeout);
                Streams.close(socket);
                openedPorts.add(i);
            } catch (IOException ex) {
                // try next port
            }
        }
        return openedPorts;
    }

    /**
     * 获取未开启的端口
     *
     * @param host  host
     * @param ports ports
     * @return closedPorts
     */
    public static List<Integer> getClosePorts(String host, int[] ports) {
        return getClosePorts(host, ports, TIMEOUT);
    }

    /**
     * 获取未开启的端口
     *
     * @param host    host
     * @param ports   ports
     * @param timeout timeout
     * @return closedPorts
     */
    public static List<Integer> getClosePorts(String host, int[] ports, int timeout) {
        List<Integer> closedPorts = new ArrayList<>();
        for (int port : ports) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                Streams.close(socket);
            } catch (Exception e) {
                closedPorts.add(port);
            }
        }
        return closedPorts;
    }

    /**
     * 获取未开启的端口
     *
     * @param host  host
     * @param start start
     * @param end   end
     * @return closedPorts
     */
    public static List<Integer> getClosePorts(String host, int start, int end) {
        return getClosePorts(host, start, end, TIMEOUT);
    }

    /**
     * 获取未开启的端口
     *
     * @param host    host
     * @param start   start
     * @param end     end
     * @param timeout timeout
     * @return closedPorts
     */
    public static List<Integer> getClosePorts(String host, int start, int end, int timeout) {
        Assert.gt(start, 0, "start port must greater than 0");
        Assert.lte(end, 65535, "end port must less than 65536");
        Assert.gt(end, start, "end port must greater than start");
        List<Integer> closedPorts = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, i), timeout);
                Streams.close(socket);
            } catch (IOException ex) {
                closedPorts.add(i);
            }
        }
        return closedPorts;
    }

}

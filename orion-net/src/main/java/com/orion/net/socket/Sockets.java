package com.orion.net.socket;

import com.orion.utils.Valid;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Socket 工具
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/16 11:23
 */
public class Sockets {

    private Sockets() {
    }

    /**
     * 获取一个指定端口中可用端口的 ServerSocket
     *
     * @param ports 端口
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create(int[] ports) {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                // try next port
            }
        }
        return null;
    }

    /**
     * 获取一个可用端口的 ServerSocket (5000 - 65535]
     *
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create() {
        return create(5001, 65535);
    }

    /**
     * 获取一个指定端口范围中可用端口的 ServerSocket
     *
     * @param start 端口开始 > 1000
     * @param end   端口结束 <= 65535
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create(int start, int end) {
        Valid.gt(start, 1000, "start port must greater than 1000");
        Valid.gt(end, 1000, "end port must greater than 1000");
        Valid.lte(end, 65535, "end port must less than 65536");
        for (int i = start; i <= end; i++) {
            try {
                return new ServerSocket(i);
            } catch (IOException ex) {
                // try next port
            }
        }
        return null;
    }

}

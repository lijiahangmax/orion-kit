/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.utils.net;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.random.Randoms;
import com.orion.lang.utils.regexp.Matches;

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IP 地址工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/2 16:18
 */
public class IPs {

    /**
     * 本机IP  没有则为127.0.0.1
     */
    public static final String IP;

    private static final int[][] IP_RANGE;

    private IPs() {
    }

    static {
        String i = Const.LOCALHOST_IP_V4;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        i = ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        IP = i;
    }

    static {
        IP_RANGE = new int[][]{
                {607649792, 608174079},     // 36.56.0.0 ~ 36.63.255.255
                {1038614528, 1039007743},   // 61.232.0.0 ~ 61.237.255.255
                {1783627776, 1784676351},   // 106.80.0.0 ~ 106.95.255.255
                {2035023872, 2035154943},   // 121.76.0.0 ~ 121.77.255.255
                {2078801920, 2079064063},   // 123.232.0.0 ~ 123.235.255.255
                {-1950089216, -1948778497}, // 139.196.0.0 ~ 139.215.255.255
                {-1425539072, -1425014785}, // 171.8.0.0 ~ 171.15.255.255
                {-1236271104, -1235419137}, // 182.80.0.0 ~ 182.92.255.255
                {-770113536, -768606209},   // 210.25.0.0 ~ 210.47.255.255
                {-569376768, -564133889},   // 222.16.0.0 ~ 222.95.255.255
        };
    }

    /**
     * 检查是否为合法ip
     *
     * @param ip ip
     * @return ip地址
     */
    public static String checkIp(String ip) {
        if (Strings.isBlank(ip)) {
            return null;
        }
        if (Const.UNKNOWN.equalsIgnoreCase(ip)) {
            return null;
        }
        if (Const.LOCALHOST_IP_V6.equals(ip)) {
            return Const.LOCALHOST_IP_V4;
        }
        if (!isIp(ip)) {
            return null;
        }
        return ip;
    }

    /**
     * 是否为本机ip
     *
     * @param ip ip
     * @return true 是本机ip
     */
    public static boolean isLocal(String ip) {
        if (Strings.isBlank(ip)) {
            return false;
        }
        if (Const.LOCALHOST.equalsIgnoreCase(ip.trim())) {
            return true;
        }
        if (Const.LOCALHOST_IP_V4.equals(ip.trim())) {
            return true;
        }
        if (Const.LOCALHOST_IP_V6.equals(ip.trim())) {
            return true;
        }
        for (String s : getLocalHosts().keySet()) {
            if (s.equals(ip.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是ipv4
     *
     * @param ip ip地址
     * @return true 是
     */
    public static boolean isIpv4(String ip) {
        try {
            String[] pp = ip.split("\\.");
            if (pp.length != 4) {
                return false;
            }
            for (String p : pp) {
                if (p.length() > 3) {
                    return false;
                }
                // 01也会转化为1
                int b = Integer.parseInt(p);
                if (b > 255 || b < 0 || !Integer.toString(b).equals(p)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是ipv6
     *
     * @param ip ip地址
     * @return true 是
     */
    public static boolean isIpv6(String ip) {
        return Matches.isIpv6(ip);
    }

    /**
     * 判断是否是ip
     *
     * @param ip ipv4 或 ipv6
     * @return true 是
     */
    public static boolean isIp(String ip) {
        return isIpv4(ip) || isIpv6(ip);
    }

    /**
     * 是否为内网ip
     *
     * @param ip ip地址
     * @return true内网 false外网
     */
    public static boolean isInternalIp(String ip) {
        return isInternalIp(ipv4ToBytes(ip)) || Const.LOCALHOST_IP_V4.equals(ip) || Const.LOCALHOST.equals(ip);
    }

    /**
     * 是否为内网ip
     *
     * @param addr ip地址
     * @return true内网 false外网
     */
    public static boolean isInternalIp(byte[] addr) {
        if (addr == null || addr.length < 2) {
            return true;
        }
        switch (addr[0]) {
            case 0x0A:
                return true;
            case (byte) 0xAC:
                if (addr[1] >= 0x10) {
                    return true;
                }
            case (byte) 0xC0:
                if (addr[1] == (byte) 0xA8) {
                    return true;
                }
            default:
                return false;
        }
    }

    public static byte[] ipv4ToBytes(String ip) {
        if (ip.length() == 0) {
            return null;
        }
        byte[] bytes = new byte[4];
        String[] elements = ip.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    /**
     * 生成随机ip地址
     *
     * @return ip地址
     */
    public static String randomIp() {
        int[] range = Arrays1.random(IP_RANGE);
        return intToIp(range[0] + Randoms.randomInt(range[1] - range[0]));
    }

    /**
     * 检查 ipv4 是否在区间内
     *
     * @param ipStart 区间开始
     * @param ipEnd   区间结束
     * @param ip      ip
     * @return 是否在区间内 ipStart <= ip <= ipEnd
     */
    public static boolean ipInRange(String ipStart, String ipEnd, String ip) {
        long is = ipToLong(ipStart);
        long ie = ipToLong(ipEnd);
        long i = ipToLong(ip);
        return is <= i && i <= ie;
    }

    /**
     * ipv4 > long
     *
     * @param ip ipv4
     * @return long
     */
    public static long ipToLong(String ip) {
        String[] ips = ip.trim().split("\\.");
        long ipLong = 0L;
        for (int i = 0; i < 4; i++) {
            ipLong = ipLong << 8 | Integer.parseInt(ips[i]);
        }
        return ipLong;
    }

    /**
     * long > ipv4
     *
     * @param ip long
     * @return ipv4
     */
    public static String longToIp(long ip) {
        return intToIp((int) ip);
    }

    /**
     * 将十进制转换成ip
     *
     * @param ip 10进制ip
     * @return ip
     */
    public static String intToIp(int ip) {
        int[] b = new int[4];
        b[0] = (ip >> 24) & 0xFF;
        b[1] = (ip >> 16) & 0xFF;
        b[2] = (ip >> 8) & 0xFF;
        b[3] = ip & 0xFF;
        return b[0] + "." + b[1] + "." + b[2] + "." + b[3];
    }

    /**
     * 检测IP是否能ping通
     *
     * @param ip IP地址
     * @return 是否ping通
     */
    public static boolean ping(String ip) {
        return ping(ip, Const.MS_S_1);
    }

    /**
     * 检测IP是否能ping通
     *
     * @param ip      IP地址
     * @param timeout 超时时间
     * @return 是否ping通
     */
    public static boolean ping(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过名称获取主机ip
     *
     * @return ip
     */
    public static String getHostIp(String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (Exception e) {
            return Const.LOCALHOST_IP_V4;
        }
    }

    /**
     * 获取默认主机ip
     *
     * @return ip
     */
    public static String getDefaultHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return Const.LOCALHOST_IP_V4;
        }
    }

    /**
     * 获取默认主机名称
     *
     * @return 主机名称
     */
    public static String getDefaultHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return Const.UNKNOWN;
        }
    }

    /**
     * 获取本机所有 主机ip和名称
     *
     * @return key:ip value: host
     */
    public static Map<String, String> getLocalHosts() {
        Map<String, String> result = new HashMap<>(Const.CAPACITY_16);
        try {
            InetAddress[] allAddress = InetAddress.getAllByName(getDefaultHostName());
            for (InetAddress address : allAddress) {
                // %后的是网卡号
                result.put(address.getHostAddress().split("%")[0], address.getHostName());
            }
        } catch (Exception e) {
            // ignore
        }
        return result;
    }

    /**
     * 获取主机默认网卡信息
     *
     * @return 网卡信息
     */
    public static NetworkInterface getDefaultNetwork() {
        try {
            return NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取网卡
     *
     * @param networkName 网卡名称 linux: 默认eth0
     * @return 网卡
     */
    public static NetworkInterface getNetworkByName(String networkName) {
        try {
            return NetworkInterface.getByName(networkName);
        } catch (SocketException e) {
            return null;
        }
    }

    /**
     * 获取网卡ip地址
     *
     * @param networkName 网卡名称 linux: 默认eth0
     * @return ip地址
     */
    public static List<String> getNetworkAddresses(String networkName) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(networkName);
            return networkInterface.getInterfaceAddresses()
                    .stream()
                    .map(s -> s.getAddress().getHostAddress())
                    .map(s -> s.contains("%") ? s.split("%")[0] : s)
                    .collect(Collectors.toList());
        } catch (SocketException e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取网卡名称
     *
     * @return 网卡名称
     */
    public static List<String> getNetworkNames() {
        try {
            return Lists.as(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .map(NetworkInterface::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获得地址信息中的MAC地址 (本机)
     *
     * @param address address
     * @return MAC地址
     */
    public static String getMacAddress(InetAddress address) {
        if (address == null) {
            return null;
        }
        try {
            byte[] mac = NetworkInterface.getByInetAddress(address).getHardwareAddress();
            final StringBuilder sb = new StringBuilder();
            String s;
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(':');
                }
                s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建 InetSocketAddress
     *
     * @param port 0系统分配临时端口
     */
    public static InetSocketAddress createAddress(int port) {
        return new InetSocketAddress(port);
    }

    /**
     * 创建 InetSocketAddress
     *
     * @param host 域名/IP地址 null任意
     * @param port 0系统分配临时端口
     */
    public static InetSocketAddress createAddress(String host, int port) {
        return Strings.isBlank(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
    }

}

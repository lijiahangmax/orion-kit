package com.orion.utils.net;

import com.orion.constant.Const;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.regexp.Matches;

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IP 地址工具
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/2 16:18
 */
public class IPs {

    /**
     * 本机IP  没有则为127.0.0.1
     */
    public static final String IP;

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

    /**
     * 检查是否有ip, 或者合法
     *
     * @param ip IP地址
     * @return ip地址
     */
    public static String checkIp(String ip) {
        if (!Strings.isBlank(ip) && !Const.UNKNOWN.equalsIgnoreCase(ip)) {
            return Const.LOCALHOST_IP_V6.equals(ip) ? Const.LOCALHOST_IP_V4 : ip;
        } else {
            return null;
        }
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

    /**
     * 将IPv4地址转换成字节
     *
     * @param ip IPv4地址
     * @return ignores
     */
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
        Map<String, String> result = new HashMap<>(16);
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
     * 检测IP是否能ping通
     *
     * @param ip IP地址
     * @return 是否ping通
     */
    public static boolean ping(String ip) {
        return ping(ip, 500);
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
     * 获得地址信息中的MAC地址 (本-机)
     *
     * @param address address
     * @return MAC地址
     */
    public static String getMacAddress(InetAddress address) {
        if (null == address) {
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

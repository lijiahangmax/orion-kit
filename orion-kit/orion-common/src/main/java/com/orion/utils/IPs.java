package com.orion.utils;

import com.orion.utils.collect.Lists;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;

/**
 * IP 地址工具
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/2 16:18
 */
public class IPs {

    /**
     * 本机外网IP  没有则为127.0.0.1
     */
    private static final String IP;

    private IPs() {
    }

    static {
        String i = "127.0.0.1";
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
        if (!Strings.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
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
        if ("localhost".equalsIgnoreCase(ip.trim())) {
            return true;
        }
        if ("0:0:0:0:0:0:0:1".equals(ip.trim())) {
            return true;
        }
        if ("127.0.0.1".equals(ip.trim())) {
            return true;
        }
        for (String s : getAllHost().keySet()) {
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
                Integer b = Integer.valueOf(p);
                if (b > 255 || b < 0 || !b.toString().equals(p)) {
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
        return isInternalIp(ipv4ToBytes(ip)) || "127.0.0.1".equals(ip) || "localhost".equals(ip);
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
                if (addr[1] >= 0x10 && addr[0] <= 0x1F) {
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
     * 获取默认主机ip
     *
     * @return ip
     */
    public static String getDefaultHostIp() {
        try {
            return getHostIp(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }

    /**
     * 获取默认主机名称
     *
     * @return 主机名称
     */
    public static String getDefaultHostName() {
        try {
            return getHostName(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            return "";
        }
    }

    /**
     * 获取主机ip
     *
     * @param addr 地址
     * @return ip
     */
    public static String getHostIp(InetAddress addr) {
        if (addr == null) {
            return null;
        }
        return addr.getHostAddress();
    }

    /**
     * 获取主机名称
     *
     * @param addr 地址
     * @return 主机名称
     */
    public static String getHostName(InetAddress addr) {
        if (addr == null) {
            return null;
        }
        return addr.getHostName();
    }

    /**
     * 获取所有主机ip和名称
     *
     * @return key:ip value: name
     */
    public static Map<String, String> getAllHost() {
        Map<String, String> result = new HashMap<>(16);
        try {
            InetAddress[] allAddress = InetAddress.getAllByName(getDefaultHostName());
            for (InetAddress address : allAddress) {
                // %后的是网卡号
                result.put(address.getHostAddress().split("%")[0], address.getHostName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有主机地址
     *
     * @return key:ip value: name
     */
    public static List<InetAddress> getAllInetAddress() {
        try {
            return Lists.of(InetAddress.getAllByName(getDefaultHostName()));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}

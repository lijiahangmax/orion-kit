package com.orion.location.ext.core;

import com.orion.location.region.core.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ip查询器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/3 19:27
 */
@SuppressWarnings("ALL")
public class LocationSeeker {

    /**
     * dat文件默认值
     */
    private static final String DATA_UNKNOWN = "unknown";

    private static final int IP_RECORD_LENGTH = 7;
    private static final byte AREA_FOLLOWED = 0x01;
    private static final byte NO_AREA = 0x2;

    /**
     * 缓存
     */
    private HashMap<String, IpLocation> ipCache;

    /**
     * 随机文件访问类
     */
    private RandomAccessFile ipFile;

    /**
     * 内存映射文件
     */
    private MappedByteBuffer mbb;

    /**
     * 起始地区的开始和结束的绝对偏移
     */
    private long ipBegin, ipEnd;

    private IpLocation loc;
    private byte[] buf;
    private byte[] b4;
    private byte[] b3;

    public LocationSeeker(File file) throws FileNotFoundException {
        this(new RandomAccessFile(file, "r"));
    }

    public LocationSeeker(RandomAccessFile accessFile) {
        ipCache = new HashMap<>();
        loc = new LocationSeeker.IpLocation();
        buf = new byte[100];
        b4 = new byte[4];
        b3 = new byte[3];
        ipFile = accessFile;
        if (ipFile != null) {
            // 读取文件头信息
            try {
                ipBegin = readLong4(0);
                ipEnd = readLong4(4);
                if (ipBegin == -1 || ipEnd == -1) {
                    ipFile.close();
                    ipFile = null;
                }
            } catch (IOException e) {
                ipFile = null;
            }
        }
    }

    /**
     * 给定一个地点的不完全名字, 得到一系列包含s子串的IP范围记录
     *
     * @param s 地点子串
     * @return 包含IpEntry类型的List
     */
    public List<IpEntry> getIPEntries(String s) {
        List<IpEntry> ret = new ArrayList<>();
        try {
            // 映射IP信息文件到内存中
            if (mbb == null) {
                FileChannel fc = ipFile.getChannel();
                mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, ipFile.length());
                mbb.order(ByteOrder.LITTLE_ENDIAN);
            }

            int endOffset = (int) ipEnd;
            for (int offset = (int) ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
                int temp = readInt3(offset);
                if (temp != -1) {
                    IpLocation loc = getIpLocation(temp);
                    // 判断是否这个地点里面包含了s子串, 如果包含了, 添加这个记录到List中, 如果没有, 继续
                    if (loc.country.contains(s) || loc.area.contains(s)) {
                        IpEntry entry = new IpEntry();
                        entry.country = loc.country;
                        entry.area = loc.area;
                        // 得到起始IP
                        readIP(offset - 4, b4);
                        entry.beginIp = SeekerSupport.getIpStringFromBytes(b4);
                        // 得到结束IP
                        readIP(temp, b4);
                        entry.endIp = SeekerSupport.getIpStringFromBytes(b4);
                        // 添加该记录
                        ret.add(entry);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 给定一个地点的不完全名字, 得到一系列包含s子串的IP范围记录
     *
     * @param s 地点子串
     * @return 包含IpEntry类型的List
     */
    public List<IpEntry> getIPEntries1(String s) {
        List<IpEntry> ret = new ArrayList<>();
        long endOffset = ipEnd + 4;
        for (long offset = ipBegin + 4; offset <= endOffset; offset += IP_RECORD_LENGTH) {
            // 读取结束IP偏移
            long temp = readLong3(offset);
            // 如果temp不等于-1, 读取IP的地点信息
            if (temp != -1) {
                IpLocation loc = getIpLocation(temp);
                // 判断是否这个地点里面包含了s子串, 如果包含了, 添加这个记录到List中, 如果没有, 继续
                if (loc.country.indexOf(s) != -1 || loc.area.indexOf(s) != -1) {
                    IpEntry entry = new IpEntry();
                    entry.country = loc.country;
                    entry.area = loc.area;
                    // 得到起始IP
                    readIP(offset - 4, b4);
                    entry.beginIp = SeekerSupport.getIpStringFromBytes(b4);
                    // 得到结束IP
                    readIP(temp, b4);
                    entry.endIp = SeekerSupport.getIpStringFromBytes(b4);
                    // 添加该记录
                    ret.add(entry);
                }
            }
        }
        return ret;
    }

    /**
     * 根据IP得到国家
     *
     * @param ip ip
     * @return 国家
     */
    public String getCountry(byte[] ip) {
        // 保存ip, 转换ip字节数组为字符串形式
        String ipStr = SeekerSupport.getIpStringFromBytes(ip);
        // 先检查cache中是否已经包含有这个ip的结果, 没有再搜索文件
        if (ipCache.containsKey(ipStr)) {
            return ipCache.get(ipStr).country;
        } else {
            IpLocation loc = getIpLocation(ip);
            ipCache.put(ipStr, loc.copy());
            return loc.country;
        }
    }

    /**
     * 根据IP得到国家
     *
     * @param ip ip
     * @return 国家
     */
    public String getCountry(String ip) {
        return getCountry(SeekerSupport.getIpByteArrayFromString(ip));
    }

    /**
     * 根据IP得到地区
     *
     * @param ip ip
     * @return 地区
     */
    public String getArea(byte[] ip) {
        // 保存ip, 转换ip字节数组为字符串形式
        String ipStr = SeekerSupport.getIpStringFromBytes(ip);
        // 先检查cache中是否已经包含有这个ip的结果, 没有再搜索文件
        if (ipCache.containsKey(ipStr)) {
            return ipCache.get(ipStr).area;
        } else {
            IpLocation loc = getIpLocation(ip);
            ipCache.put(ipStr, loc.copy());
            return loc.area;
        }
    }

    /**
     * 根据IP得到地区
     *
     * @param ip ip
     * @return 地区
     */
    public String getArea(String ip) {
        return getArea(SeekerSupport.getIpByteArrayFromString(ip));
    }

    /**
     * 获取ip的地址
     *
     * @param ip ip
     * @return 国家 地区
     */
    public String getAddress(String ip) {
        String country = " CZ88.NET".equals(getCountry(ip)) ? "" : getCountry(ip);
        String area = " CZ88.NET".equals(getArea(ip)) ? "" : getArea(ip);
        String address = country + " " + area;
        return address.trim();
    }

    /**
     * 获取全部ip地址集合列表
     *
     * @return ignore
     */
    public List<String> getAllIp() {
        List<String> list = new ArrayList<String>();
        byte[] buf = new byte[4];
        for (long i = ipBegin; i < ipEnd; i += IP_RECORD_LENGTH) {
            try {
                this.readIP(this.readLong3(i + 4), buf);
                String ip = SeekerSupport.getIpStringFromBytes(buf);
                list.add(ip);
            } catch (Exception e) {
                // nothing
            }
        }
        return list;
    }

    /**
     * 根据ip搜索ip信息文件, 得到IpLocation结构, 所搜索的ip参数从类成员ip中得到
     *
     * @param ip 要查询的IP
     * @return IpLocation结构
     */
    public IpLocation getIpLocation(String ip) {
        return getIpLocation(SeekerSupport.getIpByteArrayFromString(ip));
    }

    /**
     * 根据ip搜索ip信息文件, 得到IpLocation结构, 所搜索的ip参数从类成员ip中得到
     *
     * @param ip 要查询的IP
     * @return IpLocation结构
     */
    public IpLocation getIpLocation(byte[] ip) {
        IpLocation info = null;
        long offset = locateIP(ip);
        if (offset != -1) {
            info = getIpLocation(offset);
        }
        if (info == null) {
            info = new IpLocation();
            info.country = "未知";
            info.area = "未知";
        }
        return info;
    }

    /**
     * 解析ip地址，返回该ip地址对应的国家省份信息
     *
     * @param ip ipv4
     * @return ignore
     */
    public Region getRegion(String ip) {
        Region info = new Region();
        if (ip == null || ip.trim().isEmpty()) {
            return info;
        }
        try {
            String country = getCountry(ip);
            if ("局域网".equals(country)) {
                info.setCountry("中国");
                info.setProvince("上海市");
            } else if (country != null && !country.trim().isEmpty()) {
                // 可解析 格式为: xxx省(xxx市)(xxx县/区)
                country = country.trim();
                int length = country.length();
                int index = country.indexOf('省');
                if (index > 0) {
                    // 23个省ip
                    info.setCountry("中国");
                    if (index == length - 1) {
                        // 格式为 xx省
                        info.setProvince(country);
                    } else {
                        // 格式为 xx省xx市
                        info.setProvince(country.substring(0, index + 1));
                        int cityIndex = country.indexOf('市', index);
                        if (cityIndex > 0) {
                            info.setCity(country.substring(index + 1, Math.min(cityIndex + 1, length)));
                        }
                    }
                } else {
                    // 5个自治区 4个直辖市 2个特别行政区 ip
                    String h = country.substring(0, 2);
                    switch (h) {
                        case "内蒙":
                            info.setCountry("中国");
                            info.setProvince("内蒙古自治区");
                            country = country.substring(3);
                            if (!country.isEmpty()) {
                                index = country.indexOf('市');
                                if (index > 0) {
                                    info.setCity(country.substring(0, Math.min(index + 1, country.length())));
                                }
                            }
                            break;
                        case "广西":
                        case "西藏":
                        case "宁夏":
                        case "新疆":
                            info.setCountry("中国");
                            info.setProvince(h);
                            country = country.substring(2);
                            if (!country.isEmpty()) {
                                index = country.indexOf('市');
                                if (index > 0) {
                                    info.setCity(country.substring(0, Math.min(index + 1, country.length())));
                                }
                            }
                            break;
                        case "上海":
                        case "北京":
                        case "天津":
                        case "重庆":
                            info.setCountry("中国");
                            info.setProvince(h + "市");
                            country = country.substring(3);
                            if (!country.isEmpty()) {
                                index = country.indexOf('区');
                                if (index > 0) {
                                    char ch = country.charAt(index - 1);
                                    if (ch != '校' || ch != '小') {
                                        info.setCity(country.substring(0, Math.min(index + 1, country.length())));
                                    }
                                }
                                if (DATA_UNKNOWN.equals(info.getCity())) {
                                    index = country.indexOf('县');
                                    if (index > 0) {
                                        info.setCity(country.substring(0, Math.min(index + 1, country.length())));
                                    }
                                }
                            }
                            break;
                        case "香港":
                        case "澳门":
                            info.setCountry("中国");
                            info.setProvince(h + "特别行政区");
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    // private

    /**
     * 从内存映射文件的offset位置开始的3个字节读取一个int
     */
    private int readInt3(int offset) {
        mbb.position(offset);
        return mbb.getInt() & 0x00FFFFFF;
    }

    /**
     * 从内存映射文件的当前位置开始的3个字节读取一个int
     */
    private int readInt3() {
        return mbb.getInt() & 0x00FFFFFF;
    }

    /**
     * 从offset位置读取4个字节为一个long, 因为java为big-endian格式, 所以没办法 用了这么一个函数来做转换
     *
     * @return 读取的long值, 返回-1表示读取文件失败
     */
    private long readLong4(long offset) {
        long ret = 0;
        try {
            ipFile.seek(offset);
            ret |= (ipFile.readByte() & 0xFF);
            ret |= ((ipFile.readByte() << 8) & 0xFF00);
            ret |= ((ipFile.readByte() << 16) & 0xFF0000);
            ret |= ((ipFile.readByte() << 24) & 0xFF000000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 从offset位置读取3个字节为一个long
     *
     * @return ong
     */
    private long readLong3(long offset) {
        long ret = 0;
        try {
            ipFile.seek(offset);
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 从当前位置读取3个字节转换成long
     *
     * @return long
     */
    private long readLong3() {
        long ret = 0;
        try {
            ipFile.readFully(b3);
            ret |= (b3[0] & 0xFF);
            ret |= ((b3[1] << 8) & 0xFF00);
            ret |= ((b3[2] << 16) & 0xFF0000);
            return ret;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 从offset位置读取四个字节的ip地址放入ip数组中
     */
    private void readIP(long offset, byte[] ip) {
        try {
            ipFile.seek(offset);
            ipFile.readFully(ip);
            byte temp = ip[0];
            ip[0] = ip[3];
            ip[3] = temp;
            temp = ip[1];
            ip[1] = ip[2];
            ip[2] = temp;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从offset位置读取四个字节的ip地址放入ip数组中
     */
    private void readIP(int offset, byte[] ip) {
        mbb.position(offset);
        mbb.get(ip);
        byte temp = ip[0];
        ip[0] = ip[3];
        ip[3] = temp;
        temp = ip[1];
        ip[1] = ip[2];
        ip[2] = temp;
    }

    /**
     * 把类成员ip和beginIp比较
     *
     * @param ip      要查询的IP
     * @param beginIp 和被查询IP相比较的IP
     * @return 相等返回0, ip大于beginIp则返回1, 小于返回-1
     */
    private int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    /**
     * 把两个byte当作无符号数进行比较
     *
     * @return 若b1大于b2则返回1, 相等返回0, 小于返回-1
     */
    private int compareByte(byte b1, byte b2) {
        // 比较是否大于
        if ((b1 & 0xFF) > (b2 & 0xFF)) {
            return 1;
            // 判断是否相等
        } else if ((b1 ^ b2) == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 这个方法将根据ip的内容, 定位到包含这个ip国家地区的记录处, 返回一个绝对偏移 方法使用二分法查找
     *
     * @param ip 要查询的IP
     * @return 结束IP的偏移, 没找到返回-1
     */
    private long locateIP(byte[] ip) {
        long m = 0;
        int r;
        // 比较第一个ip项
        readIP(ipBegin, b4);
        r = compareIP(ip, b4);
        if (r == 0) {
            return ipBegin;
        } else if (r < 0) {
            return -1;
        }
        // 开始二分搜索
        for (long i = ipBegin, j = ipEnd; i < j; ) {
            m = getMiddleOffset(i, j);
            readIP(m, b4);
            r = compareIP(ip, b4);
            if (r > 0) {
                i = m;
            } else if (r < 0) {
                if (m == j) {
                    j -= IP_RECORD_LENGTH;
                    m = j;
                } else {
                    j = m;
                }
            } else {
                return readLong3(m + 4);
            }
        }
        // 如果循环结束了, 那么i和j必定是相等的, 这个记录为最可能的记录, 但是并非
        // 肯定就是, 还要检查一下, 如果是, 就返回结束地址区的绝对偏移
        m = readLong3(m + 4);
        readIP(m, b4);
        r = compareIP(ip, b4);
        if (r <= 0) {
            return m;
        } else {
            return -1;
        }
    }

    /**
     * 得到begin偏移和end偏移中间位置记录的偏移
     */
    private long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / IP_RECORD_LENGTH;
        records >>= 1;
        if (records == 0) {
            records = 1;
        }
        return begin + records * IP_RECORD_LENGTH;
    }

    /**
     * 给定一个ip国家地区记录的偏移, 返回一个IpLocation结构
     */
    private IpLocation getIpLocation(long offset) {
        try {
            // 跳过4字节ip
            ipFile.seek(offset + 4);
            // 读取第一个字节判断是否标志字节
            byte b = ipFile.readByte();
            if (b == AREA_FOLLOWED) {
                // 读取国家偏移
                long countryOffset = readLong3();
                // 跳转至偏移处
                ipFile.seek(countryOffset);
                // 再检查一次标志字节, 因为这个时候这个地方仍然可能是个重定向
                b = ipFile.readByte();
                if (b == NO_AREA) {
                    loc.country = readString(readLong3());
                    ipFile.seek(countryOffset + 4);
                } else {
                    loc.country = readString(countryOffset);
                }
                // 读取地区标志
                loc.area = readArea(ipFile.getFilePointer());
            } else if (b == NO_AREA) {
                loc.country = readString(readLong3());
                loc.area = readArea(offset + 8);
            } else {
                loc.country = readString(ipFile.getFilePointer() - 1);
                loc.area = readArea(ipFile.getFilePointer());
            }
            return loc;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 给定一个ip国家地区记录的偏移, 返回一个IpLocation结构
     */
    private IpLocation getIpLocation(int offset) {
        // 跳过4字节ip
        mbb.position(offset + 4);
        // 读取第一个字节判断是否标志字节
        byte b = mbb.get();
        if (b == AREA_FOLLOWED) {
            // 读取国家偏移
            int countryOffset = readInt3();
            // 跳转至偏移处
            mbb.position(countryOffset);
            // 再检查一次标志字节, 因为这个时候这个地方仍然可能是个重定向
            b = mbb.get();
            if (b == NO_AREA) {
                loc.country = readString(readInt3());
                mbb.position(countryOffset + 4);
            } else {
                loc.country = readString(countryOffset);
            }
            // 读取地区标志
            loc.area = readArea(mbb.position());
        } else if (b == NO_AREA) {
            loc.country = readString(readInt3());
            loc.area = readArea(offset + 8);
        } else {
            loc.country = readString(mbb.position() - 1);
            loc.area = readArea(mbb.position());
        }
        return loc;
    }

    /**
     * 从offset偏移开始解析后面的字节, 读出一个地区名
     *
     * @return 地区名字符串
     */
    private String readArea(long offset) throws IOException {
        ipFile.seek(offset);
        byte b = ipFile.readByte();
        if (b == 0x01 || b == 0x02) {
            long areaOffset = readLong3(offset + 1);
            if (areaOffset == 0) {
                return "未知";
            } else {
                return readString(areaOffset);
            }
        } else {
            return readString(offset);
        }
    }

    /**
     * 从offset偏移开始解析后面的字节, 读出一个地区名
     *
     * @return 地区名字符串
     */
    private String readArea(int offset) {
        mbb.position(offset);
        byte b = mbb.get();
        if (b == 0x01 || b == 0x02) {
            int areaOffset = readInt3();
            if (areaOffset == 0) {
                return "未知";
            } else {
                return readString(areaOffset);
            }
        } else {
            return readString(offset);
        }
    }

    /**
     * 从offset偏移处读取一个以0结束的字符串
     *
     * @return 读取的字符串
     */
    private String readString(long offset) {
        try {
            ipFile.seek(offset);
            int i;
            for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile.readByte()) {
            }
            if (i != 0) {
                return SeekerSupport.getString(buf, 0, i, "GBK");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从内存映射文件的offset位置得到一个0结尾字符串
     */
    private String readString(int offset) {
        try {
            mbb.position(offset);
            int i;
            for (i = 0, buf[i] = mbb.get(); buf[i] != 0; buf[++i] = mbb.get()) {
            }
            if (i != 0) {
                return SeekerSupport.getString(buf, 0, i, "GBK");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ip所在的国家和地区
     */
    public class IpLocation {
        private String country;
        private String area;

        private IpLocation() {
            country = area = "";
        }

        private IpLocation copy() {
            IpLocation ret = new IpLocation();
            ret.country = country;
            ret.area = area;
            return ret;
        }

        @Override
        public String toString() {
            return country + " " + area;
        }
    }

    /**
     * IP的范围 国家, 区域, 起始IP, 结束IP
     */
    public class IpEntry {

        /**
         * 起始IP
         */
        private String beginIp;

        /**
         * 结束IP
         */
        private String endIp;

        /**
         * 国家
         */
        private String country;

        /**
         * 区域
         */
        private String area;

        private IpEntry() {
            beginIp = endIp = country = area = "";
        }

        @Override
        public String toString() {
            return this.area + " " + this.country + "IP  Χ:" + this.beginIp + "-" + this.endIp;
        }
    }

}

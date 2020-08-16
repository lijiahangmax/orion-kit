package com.orion.location.ext;

import com.orion.utils.Systems;
import com.orion.location.ext.core.LocationSeeker;
import com.orion.location.region.LocationRegions;
import com.orion.location.region.core.Region;
import com.orion.utils.IPs;

import java.io.*;

/**
 * 纯真ip地理位置提取器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/3 19:22
 */
public class LocationExt {

    private LocationExt() {
    }

    /**
     * db文件路径
     */
    private static final String DAT_PATH = Systems.HOME_DIR + "/region/" + "region.dat";

    /**
     * db文件
     */
    private static final File DAT_FILE = new File(DAT_PATH);

    /**
     * db文件目录
     */
    private static final File DAT_FILE_DIR = new File(Systems.HOME_DIR + "/region/");

    /**
     * 查询器
     */
    private static LocationSeeker seeker;

    /**
     * 是否初始化成功
     */
    private static boolean init = true;

    /**
     * 重新初始化
     */
    public static void reinit() {
        if (!init) {
            initDatFile();
        }
    }

    /**
     * 获取db文件
     */
    private static void initDatFile() {
        File file = new File(DAT_PATH);
        InputStream in = LocationRegions.class.getClassLoader().getResourceAsStream("region.dat");
        if (!file.exists() || (file.exists() && !file.isFile())) {
            if (in == null) {
                init = false;
                throw new RuntimeException("regionExt 服务初始化异常 未查询到dat文件");
            }
            try {
                if (!DAT_FILE_DIR.exists() || (DAT_FILE_DIR.exists() && !DAT_FILE_DIR.isDirectory())) {
                    if (!DAT_FILE_DIR.mkdirs()) {
                        init = false;
                        throw new RuntimeException("regionExt 服务初始化异常 不能创建本地临时文件夹");
                    }
                }
                if (!DAT_FILE.createNewFile()) {
                    init = false;
                    throw new RuntimeException("regionExt 服务初始化异常 不能创建本地临时文件");
                }
                // 设置编码为gbk
                OutputStreamWriter o = new OutputStreamWriter(new FileOutputStream(DAT_FILE), "GBK");
                o.write("init");
                o.flush();
                o.close();
                // 清空重新写入数据
                FileOutputStream out = new FileOutputStream(DAT_FILE, false);
                byte[] bs = new byte[2048 * 4];
                int c;
                while ((c = in.read(bs)) != -1) {
                    out.write(bs, 0, c);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                init = false;
                e.printStackTrace();
                throw new RuntimeException("regionExt 服务初始化异常");
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                if (in != null && DAT_FILE.length() != in.available() && DAT_FILE.delete()) {
                    initDatFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // init
    static {
        initDatFile();
        if (!init) {
            System.out.println("regionExt 服务未初始失败");
        } else {
            try {
                seeker = new LocationSeeker(new File(DAT_PATH));
            } catch (Exception e) {
                init = false;
                System.out.println("regionExt 服务未初始失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取seeker实例
     *
     * @return 实例
     */
    public static LocationSeeker getSeeker() {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        return seeker;
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static String getCountry(String ip) {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getCountry(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static String getAddress(String ip) {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getAddress(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static String getArea(String ip) {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getArea(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static LocationSeeker.IpLocation getIpLocation(String ip) {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getIpLocation(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static Region getRegion(String ip) {
        if (!init) {
            throw new RuntimeException("regionExt 未初始化成功");
        }
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getRegion(ip);
        } catch (Exception e) {
            return null;
        }
    }

}

package com.orion.location.region;

import com.orion.location.region.block.DataBlock;
import com.orion.location.region.config.DbConfig;
import com.orion.location.region.core.DbSearcher;
import com.orion.location.region.core.Region;
import com.orion.utils.IPs;
import com.orion.utils.Systems;
import com.orion.utils.io.Streams;

import java.io.*;

/**
 * ip查询地址提取器 ip2region
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/2 16:18
 */
public class LocationRegions {

    private LocationRegions() {
    }

    /**
     * db文件路径
     */
    private static final String DB_PATH = Systems.HOME_DIR + "/region/" + "region.db";

    /**
     * db文件
     */
    private static final File DB_FILE = new File(DB_PATH);

    /**
     * db文件目录
     */
    private static final File DB_FILE_DIR = new File(Systems.HOME_DIR + "/region/");

    /**
     * 是否初始化成功
     */
    private static boolean init = true;

    /**
     * 搜索器
     */
    private static DbSearcher searcher;

    /**
     * 重新初始化
     */
    public static void reinit() {
        if (!init) {
            initDbFile();
        }
    }

    /**
     * 获取db文件
     */
    private static void initDbFile() {
        File file = new File(DB_PATH);
        InputStream in = LocationRegions.class.getClassLoader().getResourceAsStream("region.db");
        if (!file.exists() || (file.exists() && !file.isFile())) {
            if (in == null) {
                init = false;
                throw new RuntimeException("region 服务初始化异常 未查询到db文件");
            }
            try {
                if (!DB_FILE_DIR.exists() || (DB_FILE_DIR.exists() && !DB_FILE_DIR.isDirectory())) {
                    if (!DB_FILE_DIR.mkdirs()) {
                        init = false;
                        throw new RuntimeException("region 服务初始化异常 不能创建本地临时文件夹");
                    }
                }
                if (!DB_FILE.createNewFile()) {
                    init = false;
                    throw new RuntimeException("region 服务初始化异常 不能创建本地临时文件");
                }
                // 设置编码为gbk
                OutputStreamWriter o = new OutputStreamWriter(new FileOutputStream(DB_FILE), "GBK");
                o.write("init");
                o.flush();
                o.close();
                // 清空重新写入数据
                FileOutputStream out = new FileOutputStream(DB_FILE, false);
                byte[] bs = new byte[2048 * 4];
                int c;
                while ((c = in.read(bs)) != -1) {
                    out.write(bs, 0, c);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                init = false;
                throw new RuntimeException("region 服务初始化异常");
            } finally {
                Streams.close(in);
            }
        } else {
            try {
                if (in != null && DB_FILE.length() != in.available() && DB_FILE.delete()) {
                    initDbFile();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    // init
    static {
        initDbFile();
        if (!init) {
            System.out.println("region 服务未初始失败");
        } else {
            try {
                searcher = new DbSearcher(new DbConfig(), DB_PATH);
            } catch (Exception e) {
                init = false;
                System.out.println("region 服务未初始失败");
            }
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return 国家|区域|省|市|网络
     */
    public static Region getRegionRegion(String ip) {
        return getRegionRegion(ip, 1);
    }

    /**
     * 获取ip信息
     *
     * @param ip        ip
     * @param algorithm 算法 1: b+tree 2: 二进制 3: 内存
     * @return 国家|区域|省|市|网络
     */
    public static Region getRegionRegion(String ip, int algorithm) {
        String s = getRegion(ip, algorithm);
        if (s == null) {
            return null;
        }
        String[] rs = s.split("\\|");
        return new Region(rs[0], rs[1], rs[2], rs[3], rs[4]);
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return 国家|区域|省|市|网络
     */
    public static String getRegion(String ip) {
        return getRegion(ip, 1);
    }

    /**
     * 获取ip信息
     *
     * @param ip        ip
     * @param algorithm 算法 1: b+tree 2: 二进制 3: 内存
     * @return 国家|区域|省|市|网络
     */
    public static String getRegion(String ip, int algorithm) {
        if (!init) {
            throw new RuntimeException("region 未初始化成功");
        }
        try {
            if (!IPs.isIpv4(ip)) {
                return "未知|未知|未知|未知|未知";
            }
            DataBlock dataBlock;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    dataBlock = searcher.btreeSearch(ip);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    dataBlock = searcher.binarySearch(ip);
                    break;
                case DbSearcher.MEMORY_ALGORITHM:
                    dataBlock = searcher.memorySearch(ip);
                    break;
                default:
                    dataBlock = searcher.btreeSearch(ip);
            }
            if (dataBlock != null) {
                return dataBlock.getRegion();
            } else {
                return "未知|未知|未知|未知|未知";
            }
        } catch (Exception e) {
            return null;
        }
    }

}
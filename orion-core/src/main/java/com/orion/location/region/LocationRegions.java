package com.orion.location.region;

import com.orion.constant.Const;
import com.orion.lang.Console;
import com.orion.location.region.block.DataBlock;
import com.orion.location.region.config.DbConfig;
import com.orion.location.region.core.DbSearcher;
import com.orion.location.region.core.Region;
import com.orion.utils.Exceptions;
import com.orion.utils.Systems;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import com.orion.utils.net.IPs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;

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
    private static final String DB_PATH = Systems.HOME_DIR + Systems.FILE_SEPARATOR +
            Const.ORION + Systems.FILE_SEPARATOR +
            "region" + Systems.FILE_SEPARATOR + "region.db";

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
    public static void init() {
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
        boolean needInit = false;
        if (!file.exists() || !file.isFile()) {
            needInit = true;
        }
        // 检查是否需要重新初始化
        if (!needInit) {
            try {
                if (in != null && file.length() != in.available()) {
                    needInit = true;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        if (needInit && in == null) {
            init = false;
            throw Exceptions.init("region 服务初始化异常 未找到地址库");
        }
        OutputStreamWriter writer = null;
        FileOutputStream out = null;
        try {
            if (!Files1.touch(file)) {
                init = false;
                throw Exceptions.init("region 服务初始化异常 不能创建本地文件");
            }
            // 设置编码为gbk
            writer = new OutputStreamWriter(new FileOutputStream(file), Const.GBK);
            writer.write("init");
            writer.flush();
            // 清空重新写入数据
            out = new FileOutputStream(file, false);
            Streams.transfer(in, out);
            out.flush();
        } catch (Exception e) {
            init = false;
            throw Exceptions.init("region 服务初始化异常 不能创建本地文件", e);
        } finally {
            Streams.close(in);
            Streams.close(writer);
            Streams.close(out);
        }
    }

    // init
    static {
        initDbFile();
        if (!init) {
            Console.error("region 服务未初始化");
        } else {
            try {
                searcher = new DbSearcher(new DbConfig(), DB_PATH);
            } catch (Exception e) {
                init = false;
                Console.error("region 服务未初始化");
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
        checkInit();
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
            Exceptions.printStacks(e);
            return null;
        }
    }

    private static void checkInit() {
        if (!init) {
            throw Exceptions.init("regions 未初始化成功");
        }
    }

}
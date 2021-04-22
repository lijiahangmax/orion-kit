package com.orion.location.region;

import com.orion.constant.Const;
import com.orion.lang.builder.StringSymbolBuilder;
import com.orion.location.region.block.DataBlock;
import com.orion.location.region.config.DbConfig;
import com.orion.location.region.core.DbSearcher;
import com.orion.location.region.core.Region;
import com.orion.utils.Exceptions;
import com.orion.utils.Systems;
import com.orion.utils.io.Files1;
import com.orion.utils.net.IPs;

import java.io.IOException;

/**
 * ip查询地址提取器 ip2region
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/2 16:18
 */
public class LocationRegions {

    private static final String UNKNOWN = "未知|未知|未知|未知|未知";

    private LocationRegions() {
    }

    /**
     * db文件路径
     */
    private static final String DB_PATH = StringSymbolBuilder.of(Systems.FILE_SEPARATOR)
            .with(Systems.HOME_DIR)
            .with(Const.ORION_DISPLAY)
            .with(".region")
            .with(".region.db")
            .build();

    /**
     * 搜索器
     */
    private static DbSearcher searcher;

    static {
        boolean init;
        try {
            init = Files1.resourceToFile("region.db", DB_PATH, Const.GBK);
            searcher = new DbSearcher(new DbConfig(), DB_PATH);
        } catch (IOException e) {
            throw Exceptions.init("location region 服务初始化异常", e);
        }
        if (!init) {
            throw Exceptions.init("location region 服务初始化失败");
        }
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return 国家|区域|省|市|网络
     */
    public static Region getRegion(String ip) {
        return getRegion(ip, 1);
    }

    /**
     * 获取ip信息
     *
     * @param ip        ip
     * @param algorithm 算法 1: b+tree 2: 二进制 3: 内存
     * @return 国家|区域|省|市|网络
     */
    public static Region getRegion(String ip, int algorithm) {
        String s = getAddress(ip, algorithm);
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
    public static String getAddress(String ip) {
        return getAddress(ip, 1);
    }

    /**
     * 获取ip信息
     *
     * @param ip        ip
     * @param algorithm 算法 1: b+tree 2: 二进制 3: 内存
     * @return 国家|区域|省|市|网络
     */
    public static String getAddress(String ip, int algorithm) {
        try {
            if (!IPs.isIpv4(ip)) {
                return UNKNOWN;
            }
            DataBlock dataBlock;
            switch (algorithm) {
                case DbSearcher.BINARY_ALGORITHM:
                    dataBlock = searcher.binarySearch(ip);
                    break;
                case DbSearcher.MEMORY_ALGORITHM:
                    dataBlock = searcher.memorySearch(ip);
                    break;
                case DbSearcher.BTREE_ALGORITHM:
                default:
                    dataBlock = searcher.btreeSearch(ip);
            }
            if (dataBlock != null) {
                return dataBlock.getRegion();
            } else {
                return UNKNOWN;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
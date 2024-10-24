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
package com.orion.ext.location.region;

import com.orion.ext.location.Region;
import com.orion.ext.location.region.block.DataBlock;
import com.orion.ext.location.region.config.DbConfig;
import com.orion.ext.location.region.core.DbSearcher;
import com.orion.lang.constant.Const;
import com.orion.lang.define.builder.StringJoiner;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Systems;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.net.IPs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * ip查询地址提取器 ip2region
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/2 16:18
 */
public class LocationRegions {

    private static final String UNKNOWN = new Region().toString();

    /**
     * db文件路径
     */
    private static final String DB_PATH = StringJoiner.of(Systems.FILE_SEPARATOR)
            .with(Systems.HOME_DIR)
            .with(Const.ORION_DISPLAY)
            .with(".region")
            .with(".region.db")
            .build();

    /**
     * 搜索器
     */
    private static final DbSearcher SEARCHER;

    private LocationRegions() {
    }

    static {
        boolean init;
        try {
            InputStream source = LocationRegions.class.getClassLoader().getResourceAsStream("region.db");
            init = Files1.resourceToFile(source, new File(DB_PATH), Const.GBK);
            SEARCHER = new DbSearcher(new DbConfig(), DB_PATH);
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
                    dataBlock = SEARCHER.binarySearch(ip);
                    break;
                case DbSearcher.MEMORY_ALGORITHM:
                    dataBlock = SEARCHER.memorySearch(ip);
                    break;
                case DbSearcher.BTREE_ALGORITHM:
                default:
                    dataBlock = SEARCHER.btreeSearch(ip);
            }
            if (dataBlock != null) {
                return dataBlock.getRegion();
            } else {
                return UNKNOWN;
            }
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("address query error ip: {}, algorithm: {}", ip, algorithm), e);
        }
    }

}
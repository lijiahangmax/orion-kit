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
package cn.orionsec.kit.ext.location.ext;

import cn.orionsec.kit.ext.location.Region;
import cn.orionsec.kit.ext.location.ext.core.IpLocation;
import cn.orionsec.kit.ext.location.ext.core.LocationSeeker;
import cn.orionsec.kit.ext.location.region.LocationRegions;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.builder.StringJoiner;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Systems;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.net.IPs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 纯真ip 地理位置提取器 cz88
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 19:22
 */
public class LocationExt {

    /**
     * 查询器
     */
    private static final LocationSeeker SEEKER;

    /**
     * dat文件
     */
    private static final String DAT_PATH = StringJoiner.of(Systems.FILE_SEPARATOR)
            .with(Systems.HOME_DIR)
            .with(Const.ORION_DISPLAY)
            .with(".region")
            .with(".region.dat")
            .build();


    private LocationExt() {
    }

    // init
    static {
        boolean init;
        try {
            InputStream source = LocationRegions.class.getClassLoader().getResourceAsStream("region.dat");
            init = Files1.resourceToFile(source, new File(DAT_PATH), Const.GBK);
            SEEKER = new LocationSeeker(DAT_PATH);
        } catch (IOException e) {
            throw Exceptions.init("region ext init error", e);
        }
        if (!init) {
            throw Exceptions.init("region ext init error");
        }
    }

    /**
     * 获取 seeker 实例
     *
     * @return 实例
     */
    public static LocationSeeker getSeeker() {
        return SEEKER;
    }

    /**
     * 获取国家
     *
     * @param ip ip
     * @return ignore
     */
    public static String getCountry(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return SEEKER.getCountry(ip);
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("country query error ip: {}", ip), e);
        }
    }

    /**
     * 获取地址
     *
     * @param ip ip
     * @return ignore
     */
    public static String getAddress(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return SEEKER.getAddress(ip);
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("address query error ip: {}", ip), e);
        }
    }

    /**
     * 获取地区
     *
     * @param ip ip
     * @return ignore
     */
    public static String getArea(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return SEEKER.getArea(ip);
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("area query error ip: {}", ip), e);
        }
    }

    /**
     * 获取地址
     *
     * @param ip ip
     * @return ignore
     */
    public static IpLocation getIpLocation(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return SEEKER.getIpLocation(ip);
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("location query error ip: {}", ip), e);
        }
    }

    /**
     * 获取位置
     *
     * @param ip ip
     * @return ignore
     */
    public static Region getRegion(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return SEEKER.getRegion(ip);
        } catch (Exception e) {
            throw Exceptions.runtime(Strings.format("region query error ip: {}", ip), e);
        }
    }

}

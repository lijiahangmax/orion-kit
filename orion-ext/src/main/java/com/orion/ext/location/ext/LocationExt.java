package com.orion.ext.location.ext;

import com.orion.ext.location.Region;
import com.orion.ext.location.ext.core.IpLocation;
import com.orion.ext.location.ext.core.LocationSeeker;
import com.orion.ext.location.region.LocationRegions;
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

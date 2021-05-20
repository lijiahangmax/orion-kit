package com.orion.location.ext;

import com.orion.constant.Const;
import com.orion.lang.builder.StringSymbolBuilder;
import com.orion.location.ext.core.LocationSeeker;
import com.orion.location.region.core.Region;
import com.orion.utils.Exceptions;
import com.orion.utils.Systems;
import com.orion.utils.io.Files1;
import com.orion.utils.net.IPs;

import java.io.IOException;

/**
 * 纯真ip地理位置提取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/3 19:22
 */
public class LocationExt {

    private LocationExt() {
    }

    /**
     * dat文件
     */
    private static final String DAT_PATH = StringSymbolBuilder.of(Systems.FILE_SEPARATOR)
            .with(Systems.HOME_DIR)
            .with(Const.ORION_DISPLAY)
            .with(".region")
            .with(".region.dat")
            .build();

    /**
     * 查询器
     */
    private static LocationSeeker seeker;

    // init
    static {
        boolean init;
        try {
            init = Files1.resourceToFile("region.dat", DAT_PATH, Const.GBK);
            seeker = new LocationSeeker(DAT_PATH);
        } catch (IOException e) {
            throw Exceptions.init("region ext 服务初始化异常", e);
        }
        if (!init) {
            throw Exceptions.init("region ext 服务初始化失败");
        }
    }

    /**
     * 获取seeker实例
     *
     * @return 实例
     */
    public static LocationSeeker getSeeker() {
        return seeker;
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static String getCountry(String ip) {
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getCountry(ip);
        } catch (Exception e) {
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
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getAddress(ip);
        } catch (Exception e) {
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
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getArea(ip);
        } catch (Exception e) {
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
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getIpLocation(ip);
        } catch (Exception e) {
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

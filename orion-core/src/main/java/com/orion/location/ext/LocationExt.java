package com.orion.location.ext;

import com.orion.constant.Const;
import com.orion.lang.Console;
import com.orion.location.ext.core.LocationSeeker;
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
 * 纯真ip地理位置提取器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/3 19:22
 */
public class LocationExt {

    private LocationExt() {
    }

    /**
     * dat文件
     */
    private static final String DAT_PATH = Systems.HOME_DIR + Systems.FILE_SEPARATOR +
            Const.ORION + Systems.FILE_SEPARATOR +
            "region" + Systems.FILE_SEPARATOR + "region.dat";

    /**
     * 查询器
     */
    private static LocationSeeker seeker;

    /**
     * 是否初始化成功
     */
    private static boolean init = true;

    /**
     * 初始化
     */
    public static void init() {
        if (!init) {
            initDatFile();
        }
    }

    /**
     * 获取db文件
     */
    private static void initDatFile() {
        File file = new File(DAT_PATH);
        InputStream in = LocationExt.class.getClassLoader().getResourceAsStream("region.dat");
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
            throw Exceptions.init("region EXT 服务初始化异常 未找到地址库");
        }
        OutputStreamWriter writer = null;
        FileOutputStream out = null;
        try {
            if (!Files1.touch(file)) {
                init = false;
                throw Exceptions.init("region EXT 服务初始化异常 不能创建本地文件");
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
            throw Exceptions.init("region EXT 服务初始化异常 不能创建本地文件", e);
        } finally {
            Streams.close(in);
            Streams.close(writer);
            Streams.close(out);
        }
    }

    // init
    static {
        initDatFile();
        if (!init) {
            Console.error("regionExt 服务未初始化");
        } else {
            try {
                seeker = new LocationSeeker(new File(DAT_PATH));
            } catch (Exception e) {
                init = false;
                Console.error("regionExt 服务未初始化");
                Exceptions.printStacks(e);
            }
        }
    }

    /**
     * 获取seeker实例
     *
     * @return 实例
     */
    public static LocationSeeker getSeeker() {
        checkInit();
        return seeker;
    }

    /**
     * 获取ip信息
     *
     * @param ip ip
     * @return ignore
     */
    public static String getCountry(String ip) {
        checkInit();
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getCountry(ip);
        } catch (Exception e) {
            Exceptions.printStacks(e);
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
        checkInit();
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getAddress(ip);
        } catch (Exception e) {
            Exceptions.printStacks(e);
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
        checkInit();
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getArea(ip);
        } catch (Exception e) {
            Exceptions.printStacks(e);
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
        checkInit();
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getIpLocation(ip);
        } catch (Exception e) {
            Exceptions.printStacks(e);
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
        checkInit();
        if (!IPs.isIpv4(ip)) {
            return null;
        }
        try {
            return seeker.getRegion(ip);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    private static void checkInit() {
        if (!init) {
            throw Exceptions.init("regionExt 未初始化成功");
        }
    }

}

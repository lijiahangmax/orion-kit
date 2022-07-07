package com.orion.ext.nginx;

import com.github.odiszapc.nginxparser.*;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Nginx 配置提取器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/27 16:14
 */
public class NginxExt {

    private NgxConfig config;

    public NginxExt(File file) {
        try {
            this.config = NgxConfig.read(file.getAbsolutePath());
        } catch (Exception e) {
            throw Exceptions.parse("parse nginx config file error path: '" + file.getAbsolutePath() + "'", e);
        }
    }

    public NginxExt(String path) {
        this(new File(path));
    }

    public NginxExt(InputStream in) {
        try {
            this.config = NgxConfig.read(in);
        } catch (Exception e) {
            throw Exceptions.parse("parse nginx config file error: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有的upstream
     *
     * @return upstream
     */
    public List<NginxEntry> getUpstreams() {
        List<NginxEntry> list = new ArrayList<>();
        List<NgxEntry> upstreams = config.findAll(NgxConfig.BLOCK, "upstream");
        for (NgxEntry up : upstreams) {
            NginxEntry nginxEntry = new NginxEntry()
                    .setLabelValue(parseLabelValue(up.toString()))
                    .setLabelType("upstream")
                    .setEntry(up);
            for (NgxEntry ngxEntry : ((NgxBlock) up).getEntries()) {
                if (ngxEntry instanceof NgxParam) {
                    NgxParam e = (NgxParam) ngxEntry;
                    nginxEntry.addParam(e.getName(), e.getValue());
                }
            }
            list.add(nginxEntry);
        }
        return list;
    }

    /**
     * 获取启动进程数
     *
     * @return ignore
     */
    public String getWorkerProcesses() {
        return getParam("worker_processes", null);
    }

    /**
     * 获取pid路径
     *
     * @return ignore
     */
    public String getPid() {
        return getParam("pid", null);
    }

    /**
     * 获取参数
     *
     * @param key 参数key
     * @return 参数value
     */
    public String getParam(String key) {
        return getParam(key, null);
    }

    /**
     * 获取参数
     *
     * @param key 参数key
     * @param def 默认参数
     * @return 参数value
     */
    public String getParam(String key, String def) {
        NgxParam value = config.findParam(key);
        if (value == null) {
            return def;
        }
        return value.getValue();
    }

    /**
     * 获取 http 下的所有 server
     *
     * @return servers
     */
    public List<NgxBlock> getHttpServerBlocks() {
        List<NgxEntry> servers = config.findAll(NgxConfig.BLOCK, "http", "server");
        if (Lists.isEmpty(servers)) {
            return new ArrayList<>();
        }
        List<NgxBlock> list = new ArrayList<>();
        for (NgxEntry server : servers) {
            list.add((NgxBlock) server);
        }
        return list;
    }

    /**
     * 获取 server 下的所有 location
     *
     * @param block serverBlock
     * @return locations
     */
    public List<NginxEntry> getServerLocation(NgxBlock block) {
        List<NginxEntry> list = new ArrayList<>();
        List<NgxEntry> locations = block.findAll(NgxConfig.BLOCK, "location");
        for (NgxEntry location : locations) {
            NginxEntry nginxEntry = new NginxEntry()
                    .setLabelType("location")
                    .setLabelValue(parseLabelValue(location.toString()))
                    .setEntry(location);
            for (NgxEntry ngxEntry : ((NgxBlock) location).getEntries()) {
                if (ngxEntry instanceof NgxParam) {
                    NgxParam e = (NgxParam) ngxEntry;
                    nginxEntry.addParam(e.getName(), e.getValue());
                }
            }
            list.add(nginxEntry);
        }
        return list;
    }

    /**
     * parse value split by ' ' skip 0 and last
     *
     * @param value value
     * @return 解析后的值
     */
    private String parseLabelValue(String value) {
        String[] ss = value.split(Strings.SPACE);
        if (ss.length == 0 || ss.length == 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < ss.length; i++) {
            if (ss.length - i > 2) {
                sb.append(ss[i]).append(Strings.SPACE);
            } else if (ss.length - i == 2) {
                sb.append(ss[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 获取当前配置文件信息
     *
     * @return string
     */
    public String dump() {
        return new NgxDumper(config).dump();
    }

    /**
     * 将配置写入到文件
     *
     * @param file 文件
     */
    public void dump(File file) {
        Files1.touch(file);
        try (OutputStream out = Files1.openOutputStream(file)) {
            new NgxDumper(config).dump(out);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 将配置写入到流
     *
     * @param out outputStream
     */
    public void dump(OutputStream out) {
        new NgxDumper(config).dump(out);
    }

    /**
     * 获取当前NgxConfig
     *
     * @return NgxConfig
     */
    public NgxConfig getConfig() {
        return config;
    }

}

package com.orion.nginx;

import com.github.odiszapc.nginxparser.*;
import com.orion.utils.Exceptions;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Nginx 配置提取器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/27 16:14
 */
public class NginxExt {

    private NgxConfig config;

    public NginxExt(File file) {
        try {
            this.config = NgxConfig.read(file.getAbsolutePath());
        } catch (Exception e) {
            throw Exceptions.parse("Parse Nginx config file error path: '" + file.getAbsolutePath() + "'");
        }
    }

    public NginxExt(String resourcePath) {
        try {
            this.config = NgxConfig.read(NginxExt.class.getClassLoader().getResourceAsStream(resourcePath));
        } catch (Exception e) {
            throw Exceptions.parse("Parse Nginx config file error resource path: '" + resourcePath + "'");
        }
    }

    public NginxExt(InputStream in) {
        try {
            this.config = NgxConfig.read(in);
        } catch (Exception e) {
            throw Exceptions.parse("Parse Nginx config file error: " + e.getMessage(), e);
        }
    }

    /**
     * 获取所有的upstream
     *
     * @return upstream
     */
    public List<NginxEntry> getUpstreams() {
        List<NginxEntry> list = new ArrayList<>();
        List<NgxEntry> upstreams = this.config.findAll(NgxConfig.BLOCK, "upstream");
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
        NgxParam value = this.config.findParam(key);
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
        List<NgxEntry> servers = this.config.findAll(NgxConfig.BLOCK, "http", "server");
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
        String[] ss = value.split(" ");
        if (ss.length == 0 || ss.length == 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < ss.length; i++) {
            if (ss.length - i > 2) {
                sb.append(ss[i]).append(" ");
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
        return new NgxDumper(this.config).dump();
    }

    /**
     * 将配置写入到文件
     *
     * @param file 文件
     */
    public void dump(File file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            out = Files1.openOutputStream(file);
            new NgxDumper(this.config).dump(out);
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 将配置写入到流
     *
     * @param out outputStream
     */
    public void dump(OutputStream out) {
        new NgxDumper(this.config).dump(out);
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

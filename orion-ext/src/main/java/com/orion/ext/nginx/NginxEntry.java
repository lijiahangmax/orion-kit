package com.orion.ext.nginx;

import com.github.odiszapc.nginxparser.NgxEntry;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Nginx 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/27 18:08
 */
@SuppressWarnings("ALL")
public class NginxEntry {

    /**
     * entry
     */
    private NgxEntry entry;

    /**
     * entry类型
     */
    private String labelType;

    /**
     * entry值
     */
    private String labelValue;

    /**
     * entry参数
     */
    private Map<String, String> params = new LinkedHashMap<>();

    public String getLabelType() {
        return labelType;
    }

    public NgxEntry getEntry() {
        return entry;
    }

    public NginxEntry setEntry(NgxEntry entry) {
        this.entry = entry;
        return this;
    }

    public NginxEntry setLabelType(String labelType) {
        this.labelType = labelType;
        return this;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public NginxEntry setLabelValue(String labelValue) {
        this.labelValue = labelValue;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public NginxEntry setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public NginxEntry addParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "NngixEnery{" +
                "labelType='" + labelType + '\'' +
                ", labelValue='" + labelValue + '\'' +
                ", params=" + params +
                '}';
    }

}

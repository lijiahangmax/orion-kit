package com.orion.location.region.core;

import java.io.Serializable;

/**
 * IP -> 位置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/2 22:42
 */
public class Region implements Serializable {

    private static final String UNKNOWN = "未知";

    /**
     * 国家
     */
    private String country;

    /**
     * 区域
     */
    private String area;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 网络
     */
    private String net;

    public Region() {
        this.country = UNKNOWN;
        this.area = UNKNOWN;
        this.province = UNKNOWN;
        this.city = UNKNOWN;
        this.net = UNKNOWN;
    }

    public Region(String country, String province, String city) {
        this.country = country;
        this.area = UNKNOWN;
        this.province = province;
        this.city = city;
        this.net = UNKNOWN;
    }

    public Region(String country, String area, String province, String city, String net) {
        this.country = country;
        this.area = area;
        this.province = province;
        this.city = city;
        this.net = net;
    }

    public String getCountry() {
        return country;
    }

    public String getArea() {
        return area;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getNet() {
        return net;
    }

    public Region setCountry(String country) {
        this.country = country;
        return this;
    }

    public Region setArea(String area) {
        this.area = area;
        return this;
    }

    public Region setProvince(String province) {
        this.province = province;
        return this;
    }

    public Region setCity(String city) {
        this.city = city;
        return this;
    }

    public Region setNet(String net) {
        this.net = net;
        return this;
    }

    @Override
    public String toString() {
        return country + "|" + area + "|" + province + "|" + city + "|" + net;
    }

}

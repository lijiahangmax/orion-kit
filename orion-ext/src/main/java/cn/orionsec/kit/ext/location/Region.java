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
package cn.orionsec.kit.ext.location;

import java.io.Serializable;

/**
 * IP -> 位置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/2 22:42
 */
public class Region implements Serializable {

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
        this.country = LocationConst.UNKNOWN;
        this.area = LocationConst.UNKNOWN;
        this.province = LocationConst.UNKNOWN;
        this.city = LocationConst.UNKNOWN;
        this.net = LocationConst.UNKNOWN;
    }

    public Region(String country, String province, String city) {
        this.country = country;
        this.area = LocationConst.UNKNOWN;
        this.province = province;
        this.city = city;
        this.net = LocationConst.UNKNOWN;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNet(String net) {
        this.net = net;
    }

    @Override
    public String toString() {
        return country + "|" + area + "|" + province + "|" + city + "|" + net;
    }

}

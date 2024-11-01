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
package cn.orionsec.kit.http.useragent;

import cn.orionsec.kit.lang.able.IJsonObject;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.Serializable;

/**
 * UA 解析信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 18:35
 */
public class UserAgentInfo implements Serializable, IJsonObject {

    private static final long serialVersionUID = 89873451943868422L;

    /**
     * UA唯一标识 id
     */
    private int id;

    /**
     * UA唯一标识 name
     */
    private String name;

    /**
     * 浏览器名
     */
    private String browserName;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * 浏览器家族
     */
    private String browserGroup;

    /**
     * 浏览器生产厂商
     */
    private String browserManufacturer;

    /**
     * 浏览器渲染引擎
     */
    private String browserEngine;

    /**
     * 操作系统名
     */
    private String systemName;

    /**
     * 访问设备类型
     */
    private String systemDevice;

    /**
     * 操作系统家族
     */
    private String systemGroup;

    /**
     * 操作系统生产厂商
     */
    private String systemManufacturer;

    public UserAgentInfo(UserAgent ua) {
        this.id = ua.getId();
        this.name = ua.toString();
        Browser browser = ua.getBrowser();
        this.browserName = browser.getName();
        this.browserType = browser.getBrowserType().getName();
        this.browserGroup = browser.getGroup().getName();
        this.browserManufacturer = browser.getManufacturer().getName();
        this.browserEngine = browser.getRenderingEngine().getName();
        OperatingSystem system = ua.getOperatingSystem();
        this.systemName = system.getName();
        this.systemDevice = system.getDeviceType().getName();
        this.systemGroup = system.getGroup().getName();
        this.systemManufacturer = system.getManufacturer().getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getBrowserGroup() {
        return browserGroup;
    }

    public void setBrowserGroup(String browserGroup) {
        this.browserGroup = browserGroup;
    }

    public String getBrowserManufacturer() {
        return browserManufacturer;
    }

    public void setBrowserManufacturer(String browserManufacturer) {
        this.browserManufacturer = browserManufacturer;
    }

    public String getBrowserEngine() {
        return browserEngine;
    }

    public void setBrowserEngine(String browserEngine) {
        this.browserEngine = browserEngine;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemDevice() {
        return systemDevice;
    }

    public void setSystemDevice(String systemDevice) {
        this.systemDevice = systemDevice;
    }

    public String getSystemGroup() {
        return systemGroup;
    }

    public void setSystemGroup(String systemGroup) {
        this.systemGroup = systemGroup;
    }

    public String getSystemManufacturer() {
        return systemManufacturer;
    }

    public void setSystemManufacturer(String systemManufacturer) {
        this.systemManufacturer = systemManufacturer;
    }

    @Override
    public String toString() {
        return "UserAgentInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", browserName='" + browserName + '\'' +
                ", browserType='" + browserType + '\'' +
                ", browserGroup='" + browserGroup + '\'' +
                ", browserManufacturer='" + browserManufacturer + '\'' +
                ", browserEngine='" + browserEngine + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemDevice='" + systemDevice + '\'' +
                ", systemGroup='" + systemGroup + '\'' +
                ", systemManufacturer='" + systemManufacturer + '\'' +
                '}';
    }

}

/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.generator.addres;

import cn.orionsec.kit.lang.define.builder.StringJoiner;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 19:16
 */
public class AddressSupport {

    private AddressSupport() {
    }

    /**
     * 随机获取一位省级码
     *
     * @return 省
     */
    public static Integer randomProvinceCode() {
        return AddressArea.POPULATION.next();
    }

    public static Integer randomCityCode() {
        return randomCityCode(randomProvinceCode());
    }

    /**
     * 随机获取一位市级码
     *
     * @param provinceCode 市编码
     * @return 市
     */
    public static Integer randomCityCode(int provinceCode) {
        List<Integer> cities = AddressArea.CITY.keySet().stream()
                .filter(c -> c.toString().startsWith(provinceCode + Strings.EMPTY))
                .collect(Collectors.toList());
        return Lists.random(cities);
    }

    public static Integer randomCountyCode() {
        return randomCountyCode(randomCityCode());
    }

    /**
     * 随机获取一位县级码
     *
     * @return 县
     */
    public static Integer randomCountyCode(int cityCode) {
        List<Integer> counties = AddressArea.COUNTY.keySet().stream()
                .filter(c -> c.toString().startsWith(cityCode + Strings.EMPTY))
                .collect(Collectors.toList());
        return Lists.random(counties);
    }

    /**
     * 获取省名称
     *
     * @param provinceCode provinceCode
     * @return 省名称
     */
    public static String getProvinceName(int provinceCode) {
        return AddressArea.PROVINCE.get(provinceCode);
    }

    /**
     * 获取市名称
     *
     * @param cityCode cityCode
     * @return 市名称
     */
    public static String getCityName(int cityCode) {
        return AddressArea.CITY.get(cityCode);
    }

    /**
     * 获取县名称
     *
     * @param countyCode countyCode
     * @return 县名称
     */
    public static String getCountyName(int countyCode) {
        return AddressArea.COUNTY.get(countyCode);
    }

    /**
     * 获取支持的县名称
     *
     * @param countyCode countyCode
     * @return 县名称
     */
    public static String getSupportCountyName(int countyCode) {
        return AddressArea.COUNTY.get(getSupportCountyCode(countyCode));
    }

    /**
     * 获取支持的县code
     *
     * @param countyCode countyCode
     * @return 县code
     */
    public static Integer getSupportCountyCode(int countyCode) {
        String county = AddressArea.COUNTY.get(countyCode);
        if (county != null) {
            return countyCode;
        }
        // 查询上级市
        String city = (countyCode + Strings.EMPTY).substring(0, 4);
        Integer randomCountyCode = randomCountyCode(Integer.parseInt(city));
        if (randomCountyCode != null) {
            return randomCountyCode;
        }
        // 查询上级省
        String province = city.substring(0, 2);
        Integer randomCityCode = randomCityCode(Integer.parseInt(province));
        if (randomCityCode == null) {
            return 0;
        }
        return randomCountyCode(randomCityCode);
    }

    /**
     * 随机获县地址
     *
     * @return 省市县
     */
    public static String randomCountyAddress() {
        return getCountyAddress(randomCountyCode());
    }

    /**
     * 获取三级地址
     *
     * @return 省, 市, 县
     */
    public static String[] randomAddressExt() {
        return getAddressExt(randomCountyCode());
    }

    /**
     * 获取三级地址
     *
     * @param countyCode countyCode
     * @return 省, 市, 县
     */
    public static String[] getAddressExt(int countyCode) {
        String city = (countyCode + Strings.EMPTY).substring(0, 4);
        String province = city.substring(0, 2);
        return new String[]{
                Strings.def(AddressArea.PROVINCE.get(Integer.parseInt(province))),
                Strings.def(AddressArea.CITY.get(Integer.parseInt(city))),
                Strings.def(AddressArea.COUNTY.get(countyCode))
        };
    }

    /**
     * 获取县地址
     *
     * @param countyCode countyCode
     * @return 省市县
     */
    public static String getCountyAddress(int countyCode) {
        String cityCode = (countyCode + Strings.EMPTY).substring(0, 4);
        String provinceCode = cityCode.substring(0, 2);
        String province = AddressArea.PROVINCE.get(Integer.parseInt(provinceCode));
        String city = AddressArea.CITY.get(Integer.parseInt(cityCode));
        String county = AddressArea.COUNTY.get(countyCode);
        return StringJoiner.of().with(province).with(city).with(county).skipBlank().build();
    }

}

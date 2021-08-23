package com.orion.generator.addres;

import com.orion.lang.builder.StringJoiner;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
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

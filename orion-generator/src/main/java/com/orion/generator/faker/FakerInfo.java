package com.orion.generator.faker;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据详情
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 18:08
 */
public class FakerInfo implements Serializable {

    private static final long serialVersionUID = 668834111187349582L;

    /**
     * 名称
     */
    private String name;

    /**
     * 英文名
     */
    private String enName;

    /**
     * 性别 true 男
     */
    private Boolean gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址 省级编码
     */
    private Integer provinceCode;

    /**
     * 地址 省级编码
     */
    private String provinceName;

    /**
     * 地址 市级编码
     */
    private Integer cityCode;

    /**
     * 地址 市级编码
     */
    private String cityName;

    /**
     * 地址 县级编码
     */
    private Integer countryCode;

    /**
     * 地址 县级编码
     */
    private String countryName;

    /**
     * 地址
     */
    private String address;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 身份证号码
     */
    private String idCardNo;

    /**
     * 身份证地址
     */
    private String idCardAddress;

    /**
     * 身份证
     */
    private String idCardIssueOrg;

    /**
     * 身份证有效期
     */
    private String idCardPeriod;

    /**
     * 身份证有效期开始时间
     */
    private Date idCardPeriodStart;

    /**
     * 身份证有效期结束时间
     */
    private Date idCardPeriodEnd;

    /**
     * 地址 省级编码
     */
    private Integer idCardProvinceCode;

    /**
     * 地址 省级名称
     */
    private String idCardProvinceName;

    /**
     * 地址 市级编码
     */
    private Integer idCardCityCode;

    /**
     * 地址 市级名称
     */
    private String idCardCityName;

    /**
     * 地址 县级编码
     */
    private Integer idCardCountryCode;

    /**
     * 地址 县级名称
     */
    private String idCardCountryName;

    /**
     * 储蓄卡 银行卡号
     */
    private String debitCardNo;

    /**
     * 储蓄卡 银行编码
     */
    private String debitBankCode;

    /**
     * 储蓄卡 银行名称
     */
    private String debitBankName;

    /**
     * 信用卡 银行卡号
     */
    private String creditCardNo;

    /**
     * 信用卡 银行编码
     */
    private String creditBankCode;

    /**
     * 信用卡 银行名称
     */
    private String creditBankName;

    /**
     * 学历
     */
    private String education;

    /**
     * 高校名称
     */
    private String university;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 行业
     */
    private String industry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress;
    }

    public String getIdCardIssueOrg() {
        return idCardIssueOrg;
    }

    public void setIdCardIssueOrg(String idCardIssueOrg) {
        this.idCardIssueOrg = idCardIssueOrg;
    }

    public String getIdCardPeriod() {
        return idCardPeriod;
    }

    public void setIdCardPeriod(String idCardPeriod) {
        this.idCardPeriod = idCardPeriod;
    }

    public Date getIdCardPeriodStart() {
        return idCardPeriodStart;
    }

    public void setIdCardPeriodStart(Date idCardPeriodStart) {
        this.idCardPeriodStart = idCardPeriodStart;
    }

    public Date getIdCardPeriodEnd() {
        return idCardPeriodEnd;
    }

    public void setIdCardPeriodEnd(Date idCardPeriodEnd) {
        this.idCardPeriodEnd = idCardPeriodEnd;
    }

    public Integer getIdCardProvinceCode() {
        return idCardProvinceCode;
    }

    public void setIdCardProvinceCode(Integer idCardProvinceCode) {
        this.idCardProvinceCode = idCardProvinceCode;
    }

    public String getIdCardProvinceName() {
        return idCardProvinceName;
    }

    public void setIdCardProvinceName(String idCardProvinceName) {
        this.idCardProvinceName = idCardProvinceName;
    }

    public Integer getIdCardCityCode() {
        return idCardCityCode;
    }

    public void setIdCardCityCode(Integer idCardCityCode) {
        this.idCardCityCode = idCardCityCode;
    }

    public String getIdCardCityName() {
        return idCardCityName;
    }

    public void setIdCardCityName(String idCardCityName) {
        this.idCardCityName = idCardCityName;
    }

    public Integer getIdCardCountryCode() {
        return idCardCountryCode;
    }

    public void setIdCardCountryCode(Integer idCardCountryCode) {
        this.idCardCountryCode = idCardCountryCode;
    }

    public String getIdCardCountryName() {
        return idCardCountryName;
    }

    public void setIdCardCountryName(String idCardCountryName) {
        this.idCardCountryName = idCardCountryName;
    }

    public String getDebitCardNo() {
        return debitCardNo;
    }

    public void setDebitCardNo(String debitCardNo) {
        this.debitCardNo = debitCardNo;
    }

    public String getDebitBankCode() {
        return debitBankCode;
    }

    public void setDebitBankCode(String debitBankCode) {
        this.debitBankCode = debitBankCode;
    }

    public String getDebitBankName() {
        return debitBankName;
    }

    public void setDebitBankName(String debitBankName) {
        this.debitBankName = debitBankName;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditBankCode() {
        return creditBankCode;
    }

    public void setCreditBankCode(String creditBankCode) {
        this.creditBankCode = creditBankCode;
    }

    public String getCreditBankName() {
        return creditBankName;
    }

    public void setCreditBankName(String creditBankName) {
        this.creditBankName = creditBankName;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

}

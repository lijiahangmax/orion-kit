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
package com.orion.generator.faker;

import com.orion.lang.utils.Strings;

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
     * 地址
     */
    private FakerAddress address;

    /**
     * 身份证
     */
    private FakerIdCard idCard;

    /**
     * 储蓄卡
     */
    private FakerBankCard debitCard;

    /**
     * 信用卡
     */
    private FakerBankCard creditCard;

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

    /**
     * 社会统一信用代码
     */
    private String companyCreditCode;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * ip
     */
    private String ip;


    /**
     * 地址对象
     */
    public static class FakerAddress extends AddressCode implements Serializable {

        private static final long serialVersionUID = -9473126547866L;

        /**
         * 地址
         */
        private String address;

        /**
         * 详细地址
         */
        private String detailAddress;

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

        @Override
        public String toString() {
            return address + detailAddress;
        }

    }

    /**
     * 身份证对象
     */
    public static class FakerIdCard extends AddressCode implements Serializable {

        private static final long serialVersionUID = 84122189856134796L;

        /**
         * 身份证号码
         */
        private String cardNo;

        /**
         * 身份证地址
         */
        private String address;

        /**
         * 身份证签发机关
         */
        private String issueOrg;

        /**
         * 身份证有效期
         */
        private String period;

        /**
         * 身份证有效期开始时间
         */
        private Date periodStart;

        /**
         * 身份证有效期结束时间
         */
        private Date periodEnd;

        /**
         * 民族
         */
        private String nation;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIssueOrg() {
            return issueOrg;
        }

        public void setIssueOrg(String issueOrg) {
            this.issueOrg = issueOrg;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Date getPeriodStart() {
            return periodStart;
        }

        public void setPeriodStart(Date periodStart) {
            this.periodStart = periodStart;
        }

        public Date getPeriodEnd() {
            return periodEnd;
        }

        public void setPeriodEnd(Date periodEnd) {
            this.periodEnd = periodEnd;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        @Override
        public String toString() {
            return cardNo;
        }

    }

    /**
     * 银行卡对象
     */
    public static class FakerBankCard extends AddressCode implements Serializable {

        private static final long serialVersionUID = 1114589632785632L;

        /**
         * 银行卡号
         */
        private String cardNo;

        /**
         * 银行编码
         */
        private String bankCode;

        /**
         * 银行名称
         */
        private String bankName;

        /**
         * 开户行
         */
        private String issueOrg;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getIssueOrg() {
            return issueOrg;
        }

        public void setIssueOrg(String issueOrg) {
            this.issueOrg = issueOrg;
        }

        @Override
        public String toString() {
            return bankName + Strings.SPACE + cardNo;
        }

    }

    /**
     * 地址对象
     */
    public static class AddressCode implements Serializable {

        private static final long serialVersionUID = 6415879632564L;

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

    }

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

    public FakerAddress getAddress() {
        return address;
    }

    public void setAddress(FakerAddress address) {
        this.address = address;
    }

    public FakerIdCard getIdCard() {
        return idCard;
    }

    public void setIdCard(FakerIdCard idCard) {
        this.idCard = idCard;
    }

    public FakerBankCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(FakerBankCard debitCard) {
        this.debitCard = debitCard;
    }

    public FakerBankCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(FakerBankCard creditCard) {
        this.creditCard = creditCard;
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

    public String getCompanyCreditCode() {
        return companyCreditCode;
    }

    public void setCompanyCreditCode(String companyCreditCode) {
        this.companyCreditCode = companyCreditCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}

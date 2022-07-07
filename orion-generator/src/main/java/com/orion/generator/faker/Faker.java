package com.orion.generator.faker;

import com.orion.generator.addres.AddressGenerator;
import com.orion.generator.addres.AddressSupport;
import com.orion.generator.addres.Nationalities;
import com.orion.generator.bank.BankCardGenerator;
import com.orion.generator.bank.BankCardType;
import com.orion.generator.bank.BankNameType;
import com.orion.generator.company.CompanyGenerator;
import com.orion.generator.education.EducationGenerator;
import com.orion.generator.education.UniversityGenerator;
import com.orion.generator.email.EmailGenerator;
import com.orion.generator.idcard.IdCardGenerator;
import com.orion.generator.industry.IndustryGenerator;
import com.orion.generator.mobile.MobileGenerator;
import com.orion.generator.name.EnglishNameGenerator;
import com.orion.generator.name.NameGenerator;
import com.orion.generator.plate.LicensePlateGenerator;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.identity.CreditCodes;
import com.orion.lang.utils.net.IPs;
import com.orion.lang.utils.random.Randoms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 18:07
 */
public class Faker {

    private static final int AGE_MIN, AGE_MAX;

    private Faker() {
    }

    static {
        AGE_MIN = 18;
        AGE_MAX = 50;
    }

    /**
     * 生成假数据 [18 ~ 50] 岁
     *
     * @param types 类型
     * @return 假数据
     */
    public static FakerInfo generator(FakerType... types) {
        return generator(AGE_MIN, AGE_MAX, Randoms.randomBoolean(), types);
    }

    public static FakerInfo generator(int age, FakerType... types) {
        return generator(age, Randoms.randomBoolean(), types);
    }

    public static FakerInfo generator(boolean gender, FakerType... types) {
        return generator(AGE_MIN, AGE_MAX, gender, types);
    }

    public static FakerInfo generator(int ageMin, int ageMax, FakerType... types) {
        return generator(ageMin, ageMax, Randoms.randomBoolean(), types);
    }

    public static FakerInfo generator(int ageMin, int ageMax, boolean gender, FakerType... types) {
        return generator(Randoms.randomInt(ageMin, ageMax), gender, types);
    }

    /**
     * 生成假数据
     *
     * @param age    年龄
     * @param gender 性别
     * @param types  类型
     * @return 假数据
     */
    public static FakerInfo generator(int age, boolean gender, FakerType... types) {
        FakerInfo fakerInfo = new FakerInfo();
        fakerInfo.setAge(age);
        fakerInfo.setGender(gender);
        if (Arrays1.isEmpty(types)) {
            return fakerInfo;
        }
        Arrays.sort(types, Comparator.comparing(Enum::ordinal));
        for (FakerType type : types) {
            if (FakerType.NAME.equals(type)) {
                // 中文名
                fakerInfo.setName(NameGenerator.generatorName(gender));
            } else if (FakerType.EN_NAME.equals(type)) {
                // 英文名
                fakerInfo.setEnName(EnglishNameGenerator.generatorName());
            } else if (FakerType.MOBILE.equals(type)) {
                // 手机号
                fakerInfo.setMobile(MobileGenerator.generateMobile());
            } else if (FakerType.EMAIL.equals(type)) {
                // 邮箱
                fakerInfo.setEmail(EmailGenerator.generatorEmail());
            } else if (FakerType.ADDRESS.equals(type)) {
                // 住址
                FakerInfo.FakerAddress fakerAddress = new FakerInfo.FakerAddress();
                generatorAddressCode(fakerAddress);
                String address = AddressSupport.getCountyAddress(fakerAddress.getCountryCode());
                String detailAddress = AddressGenerator.generatorAddress();
                fakerAddress.setAddress(address);
                fakerAddress.setDetailAddress(detailAddress);
                fakerInfo.setAddress(fakerAddress);
            } else if (FakerType.ID_CARD.equals(type)) {
                // 身份证
                String idCard;
                if (fakerInfo.getAddress() != null && Randoms.randomBoolean()) {
                    Integer provinceCode = fakerInfo.getAddress().getProvinceCode();
                    Integer cityCode = AddressSupport.randomCityCode(provinceCode);
                    Integer countyCode = AddressSupport.randomCountyCode(cityCode);
                    idCard = IdCardGenerator.generator(countyCode.toString(), age, gender);
                } else {
                    idCard = IdCardGenerator.generator(age, gender);
                }
                String fullAddress = IdCardGenerator.getFullAddress(idCard);
                String issueOrg = IdCardGenerator.getIssueOrg(idCard);
                String periodString = IdCardGenerator.getPeriodString(idCard);
                Date[] period = IdCardGenerator.getPeriod(idCard);
                Integer[] codeExt = IdCardGenerator.getAddressCodeExt(idCard);
                String[] addressExt = IdCardGenerator.getAddressExt(idCard);
                String nation = Nationalities.getNation(codeExt[0]);
                FakerInfo.FakerIdCard fakerIdCard = new FakerInfo.FakerIdCard();
                fakerIdCard.setCardNo(idCard);
                fakerIdCard.setAddress(fullAddress);
                fakerIdCard.setIssueOrg(issueOrg);
                fakerIdCard.setPeriod(periodString);
                fakerIdCard.setPeriodStart(period[0]);
                fakerIdCard.setPeriodEnd(period[1]);
                fakerIdCard.setProvinceCode(codeExt[0]);
                fakerIdCard.setProvinceName(addressExt[0]);
                fakerIdCard.setCityCode(codeExt[1]);
                fakerIdCard.setCityName(addressExt[1]);
                fakerIdCard.setCountryCode(codeExt[2]);
                fakerIdCard.setCountryName(addressExt[2]);
                fakerIdCard.setNation(nation);
                fakerInfo.setIdCard(fakerIdCard);
            } else if (FakerType.DEBIT_CARD.equals(type) || FakerType.CREDIT_CARD.equals(type)) {
                // 储蓄卡 | 信用卡
                BankCardType cardType = FakerType.DEBIT_CARD.equals(type) ? BankCardType.DEBIT : BankCardType.CREDIT;
                Pair<BankNameType, String> bankPair = BankCardGenerator.generatorCard(cardType);
                FakerInfo.FakerBankCard card = new FakerInfo.FakerBankCard();
                card.setCardNo(bankPair.getValue());
                card.setBankCode(bankPair.getKey().getCode());
                card.setBankName(bankPair.getKey().getName());
                // 地址
                FakerInfo.AddressCode addressCode = Optional.<FakerInfo.AddressCode>ofNullable(fakerInfo.getAddress()).orElse(fakerInfo.getIdCard());
                if (addressCode != null) {
                    card.setProvinceCode(addressCode.getProvinceCode());
                    card.setProvinceName(addressCode.getProvinceName());
                    card.setCityCode(addressCode.getCityCode());
                    card.setCityName(addressCode.getCityName());
                    card.setCountryCode(addressCode.getCountryCode());
                    card.setCountryName(addressCode.getCountryName());
                } else {
                    generatorAddressCode(card);
                }
                card.setIssueOrg(BankCardGenerator.generatorOpeningBank(bankPair.getKey(), card.getCountryCode()));
                if (FakerType.DEBIT_CARD.equals(type)) {
                    // 储蓄卡
                    fakerInfo.setDebitCard(card);
                } else {
                    // 信用卡
                    fakerInfo.setCreditCard(card);
                }
            } else if (FakerType.EDUCATION.equals(type)) {
                // 学历
                String education = EducationGenerator.generatorEducation(age);
                fakerInfo.setEducation(education);
            } else if (FakerType.UNIVERSITY.equals(type)) {
                // 高校名称
                String university = Optional.ofNullable(fakerInfo.getEducation())
                        .map(UniversityGenerator::generatorUniversity)
                        .orElseGet(UniversityGenerator::generatorUniversity);
                fakerInfo.setUniversity(university);
            } else if (FakerType.INDUSTRY.equals(type)) {
                // 行业
                String industry = IndustryGenerator.generatorIndustry(age);
                fakerInfo.setIndustry(industry);
            } else if (FakerType.LICENSE_PLATE.equals(type)) {
                // 车牌号
                int provinceCode = getProvinceCode(fakerInfo);
                String licensePlate = LicensePlateGenerator.generator(provinceCode);
                fakerInfo.setLicensePlate(licensePlate);
            } else if (FakerType.COMPANY_CREDIT_CODE.equals(type)) {
                // 社会统一信用代码
                fakerInfo.setCompanyCreditCode(CreditCodes.random());
            } else if (FakerType.COMPANY_NAME.equals(type)) {
                // 公司名称
                String managementType;
                if (fakerInfo.getIndustry() != null) {
                    managementType = IndustryGenerator.generatorManagementType(fakerInfo.getIndustry());
                } else {
                    managementType = IndustryGenerator.generatorManagementType(age);
                }
                int provinceCode = getProvinceCode(fakerInfo);
                fakerInfo.setCompanyName(CompanyGenerator.generatorCompanyName(provinceCode, managementType));
            } else if (FakerType.IP.equals(type)) {
                // ip
                fakerInfo.setIp(IPs.randomIp());
            }
        }
        return fakerInfo;
    }

    /**
     * 获取省编码
     *
     * @param fakerInfo fakerInfo
     * @return provinceCode
     */
    private static Integer getProvinceCode(FakerInfo fakerInfo) {
        return Optional.ofNullable(fakerInfo.getAddress())
                .map(FakerInfo.FakerAddress::getProvinceCode)
                .orElseGet(() -> Optional.ofNullable(fakerInfo.getIdCard())
                        .map(FakerInfo.FakerIdCard::getProvinceCode)
                        .orElseGet(AddressSupport::randomProvinceCode));
    }

    /**
     * 生成地址数据
     *
     * @param fakerAddress fakerAddress
     */
    private static void generatorAddressCode(FakerInfo.AddressCode fakerAddress) {
        Integer provinceCode = AddressSupport.randomProvinceCode();
        Integer cityCode = AddressSupport.randomCityCode(provinceCode);
        Integer countyCode = AddressSupport.randomCountyCode(cityCode);
        String[] addressExt = AddressSupport.getAddressExt(countyCode);
        fakerAddress.setProvinceName(addressExt[0]);
        fakerAddress.setCityName(addressExt[1]);
        fakerAddress.setCountryName(addressExt[2]);
        fakerAddress.setProvinceCode(provinceCode);
        fakerAddress.setCityCode(cityCode);
        fakerAddress.setCountryCode(countyCode);
    }

}

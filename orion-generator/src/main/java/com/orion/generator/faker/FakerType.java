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

/**
 * 数据类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 18:08
 */
public enum FakerType {

    /**
     * 名称
     */
    NAME,

    /**
     * 英文名
     */
    EN_NAME,

    /**
     * 手机号
     */
    MOBILE,

    /**
     * 邮箱
     */
    EMAIL,

    /**
     * 住址
     */
    ADDRESS,

    /**
     * 身份证信息
     */
    ID_CARD,

    /**
     * 储蓄卡
     */
    DEBIT_CARD,

    /**
     * 信用卡
     */
    CREDIT_CARD,

    /**
     * 学历
     */
    EDUCATION,

    /**
     * 学校名称
     */
    UNIVERSITY,

    /**
     * 行业
     */
    INDUSTRY,

    /**
     * 车牌号
     */
    LICENSE_PLATE,

    /**
     * 社会统一信用代码
     */
    COMPANY_CREDIT_CODE,

    /**
     * 公司名称
     */
    COMPANY_NAME,

    /**
     * 常用ip
     */
    IP,

    ;

    public static final FakerType[] BASE = {NAME, MOBILE, EMAIL, ADDRESS, ID_CARD};

    public static final FakerType[] ALL = values();

}

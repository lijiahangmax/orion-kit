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

/**
 * 详细地址
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 18:06
 */
public enum AddressType {

    /**
     * 小区
     */
    COMMUNITY(new String[]{"小区", "苑", "阁", "园", "轩", "府", "庄", "院", "岛", "城", "湾", "庭", "馆",
            "公馆", "东苑", "西苑", "南苑", "北苑", "东区", "西区", "南区", "北区"}),

    /**
     * 村
     */
    VILLAGE(new String[]{"村", "庒", "堡", "区", "新村", "镇"}),

    /**
     * 街道
     */
    STREET(new String[]{"街道", "巷", "街", "路", "桥"}),

    ;

    private final String[] suffix;

    AddressType(String[] suffix) {
        this.suffix = suffix;
    }

    public String[] getSuffix() {
        return suffix;
    }

}

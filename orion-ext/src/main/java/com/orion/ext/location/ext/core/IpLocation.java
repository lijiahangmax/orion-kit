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
package com.orion.ext.location.ext.core;

import com.orion.ext.location.LocationConst;
import com.orion.lang.utils.Strings;

/**
 * ip 所在的国家和地区
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/7 17:14
 */
public class IpLocation {

    /**
     * 国家
     */
    protected String country;

    /**
     * 地区
     */
    protected String area;

    protected IpLocation() {
        this.country = Strings.EMPTY;
        this.area = Strings.EMPTY;
    }

    protected IpLocation copy() {
        IpLocation ret = new IpLocation();
        ret.country = this.getCountry();
        ret.area = this.getArea();
        return ret;
    }

    public String getCountry() {
        return country.endsWith(LocationConst.CZ88_NET) ? LocationConst.UNKNOWN : country;
    }

    public String getArea() {
        return area.endsWith(LocationConst.CZ88_NET) ? LocationConst.UNKNOWN : area;
    }

    @Override
    public String toString() {
        return this.getCountry() + Strings.SPACE + this.getArea();
    }

}

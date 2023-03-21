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

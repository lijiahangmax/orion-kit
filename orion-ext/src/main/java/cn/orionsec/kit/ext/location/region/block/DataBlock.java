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
package cn.orionsec.kit.ext.location.region.block;

/**
 * @author Jiahang Li
 */
public class DataBlock {

    /**
     * city id
     */
    private int cityId;

    /**
     * region address
     */
    private String region;

    /**
     * region ptr in the db file
     */
    private int dataPtr;

    public DataBlock(int cityId, String region, int dataPtr) {
        this.cityId = cityId;
        this.region = region;
        this.dataPtr = dataPtr;
    }

    public DataBlock(int cityId, String region) {
        this(cityId, region, 0);
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setDataPtr(int dataPtr) {
        this.dataPtr = dataPtr;
    }

    public int getCityId() {
        return cityId;
    }

    public String getRegion() {
        return region;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    @Override
    public String toString() {
        return String.valueOf(cityId) + '|' + region + '|' + dataPtr;
    }

}

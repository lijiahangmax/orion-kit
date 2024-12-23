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
package cn.orionsec.kit.office.excel.reader;

import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.office.excel.annotation.ImportField;
import cn.orionsec.kit.office.excel.annotation.ImportIgnore;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/3 19:02
 */
public class ImportShop {

    @ImportField(index = 0, type = ExcelReadType.LONG)
    private Long shopId;

    @ImportField(index = 1)
    private String shopName;

    @ImportField(index = 2, type = ExcelReadType.DATE, parseFormat = "yyyy年MM月dd日")
    private Date createDate;

    @ImportField(index = 4, type = ExcelReadType.PICTURE)
    @ImportIgnore
    private byte[] businessPicture;

    @ImportField(index = 5, type = ExcelReadType.LINK_ADDRESS)
    private String businessFile;

    @ImportField(index = 5, type = ExcelReadType.COMMENT)
    private String comment;

    @ImportField(index = 6, type = ExcelReadType.DECIMAL, parseFormat = "#.###万元")
    private Double margin;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getBusinessPicture() {
        return businessPicture != null ? Base64s.imgEncode(businessPicture) : "";
    }

    public void setBusinessPicture(byte[] businessPicture) {
        this.businessPicture = businessPicture;
    }

    public String getBusinessFile() {
        return businessFile;
    }

    public void setBusinessFile(String businessFile) {
        this.businessFile = businessFile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        if (margin == null) {
            margin = 0.0;
        }
        this.margin = margin;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}

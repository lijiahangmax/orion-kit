package com.orion.office.excel.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.orion.office.excel.annotation.ImportField;
import com.orion.office.excel.annotation.ImportIgnore;
import com.orion.office.excel.type.ExcelReadType;
import com.orion.utils.codec.Base64s;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/3 19:02
 */
public class ImportShop {

    @ImportField(index = 0)
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
        return businessPicture != null ? Base64s.img64Encode(businessPicture) : "";
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

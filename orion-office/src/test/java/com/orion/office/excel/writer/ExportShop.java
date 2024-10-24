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
package com.orion.office.excel.writer;

import com.orion.office.excel.annotation.*;
import com.orion.office.excel.type.ExcelAlignType;
import com.orion.office.excel.type.ExcelFieldType;
import com.orion.office.excel.type.ExcelLinkType;
import com.orion.office.excel.type.ExcelUnderType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/30 9:44
 */
@ExportMeta(author = "MPV", manager = "kk", category = "门店")
@ExportSheet(name = "门店信息", filterHeader = true, freezeHeader = true, titleHeight = 30, rowHeight = 60, columnWidth = 20, headerUseColumnStyle = false, indexToSort = true)
@ExportTitle(title = "用户门店信息导出", useRow = 2, font = @ExportFont(fontSize = 22, fontName = "仿宋"))
public class ExportShop {

    @ExportField(index = 0, align = ExcelAlignType.CENTER, selectOptions = {"下拉1", "下拉2"}, header = "门店id")
    @ExportFont(fontName = "微软雅黑")
    // @ExportIgnore
    private Long shopId;

    @ExportField(index = 1, indent = 2, width = 30, wrapText = true, trim = true, header = "门店名称")
    @ExportFont(bold = true, color = "#5510d1", fontSize = 15, under = ExcelUnderType.SINGLE)
    private String shopName;

    @ExportField(index = 2, type = ExcelFieldType.DATE_FORMAT, format = "yyyy年MM月dd日", header = "创建时间")
    private Date createDate;

    @ExportField(index = 3, header = "资质代码", backgroundColor = "#d31111")
    private String businessCode;

    @ExportField(index = 4, header = "资质图片", width = 12)
    @ExportPicture(base64 = true, text = "!图片")
    private String businessPicture;

    @ExportField(index = 5, header = "资质文件")
    @ExportLink(type = ExcelLinkType.LINK_FILE, text = "!资质文件链接")
    @ExportComment(comment = "点击文件超链接可直接定位")
    private String businessFile;

    @ExportField(index = 7, type = ExcelFieldType.DECIMAL_FORMAT, format = "#.####万元", header = "保证金")
    private BigDecimal margin;

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

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessPicture() {
        return businessPicture;
    }

    public void setBusinessPicture(String businessPicture) {
        this.businessPicture = businessPicture;
    }

    public String getBusinessFile() {
        return businessFile;
    }

    public void setBusinessFile(String businessFile) {
        this.businessFile = businessFile;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

}

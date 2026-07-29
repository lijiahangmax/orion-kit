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
package cn.orionsec.kit.office.excel;

import cn.orionsec.kit.office.excel.annotation.ExportField;
import cn.orionsec.kit.office.excel.annotation.ExportSheet;
import cn.orionsec.kit.office.excel.annotation.ImportField;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.type.ExcelReadType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 读写回环测试 bean
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
@ExportSheet(name = "users")
public class RoundTripUser {

    @ExportField(index = 0, header = "ID", type = ExcelFieldType.NUMBER)
    @ImportField(index = 0, type = ExcelReadType.LONG)
    private Long id;

    @ExportField(index = 1, header = "名称", type = ExcelFieldType.TEXT)
    @ImportField(index = 1, type = ExcelReadType.TEXT)
    private String name;

    @ExportField(index = 2, header = "余额", type = ExcelFieldType.NUMBER)
    @ImportField(index = 2, type = ExcelReadType.DECIMAL)
    private BigDecimal balance;

    @ExportField(index = 3, header = "时间", type = ExcelFieldType.DATE)
    @ImportField(index = 3, type = ExcelReadType.DATE)
    private Date date;

    @ExportField(index = 4, header = "备注", type = ExcelFieldType.TEXT)
    @ImportField(index = 4, type = ExcelReadType.TEXT)
    private String remark;

    public RoundTripUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}

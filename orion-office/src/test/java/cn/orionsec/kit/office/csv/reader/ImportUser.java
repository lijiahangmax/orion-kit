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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.office.csv.annotation.ImportField;
import cn.orionsec.kit.office.csv.annotation.ImportIgnore;
import cn.orionsec.kit.office.csv.annotation.ImportSetting;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/7 15:55
 */
@ImportSetting(delimiter = ',')
public class ImportUser {

    @ImportField(value = 0)
    @ImportIgnore
    private int id;

    @ImportField(value = 1)
    private String name;

    @ImportField(value = 2)
    private String date;

    @ImportField(value = 3)
    private String desc;

    @ImportField(value = 4)
    private String empty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        if (empty == null) {
            empty = "null invoke";
        }
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "ImportUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", desc='" + desc + '\'' +
                ", empty='" + empty + '\'' +
                '}';
    }

}

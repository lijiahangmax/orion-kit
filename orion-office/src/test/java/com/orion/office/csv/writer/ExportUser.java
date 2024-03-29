package com.orion.office.csv.writer;

import com.orion.office.csv.annotation.ExportField;
import com.orion.office.csv.annotation.ExportIgnore;
import com.orion.office.csv.annotation.ExportSetting;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/30 1:24
 */
@ExportSetting(delimiter = ',', indexToSort = true)
public class ExportUser {

    @ExportField(value = 0, header = "序列")
    @ExportIgnore
    private int num;

    @ExportField(value = 1, header = "id")
    private int id;

    @ExportField(value = 2, header = "name")
    private String name;

    @ExportField(value = 4, header = "时间")
    private String date;

    @ExportField(value = 6)
    private String desc;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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
}


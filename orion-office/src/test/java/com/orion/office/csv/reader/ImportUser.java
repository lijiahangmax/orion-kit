package com.orion.office.csv.reader;

import com.orion.office.csv.annotation.ImportField;
import com.orion.office.csv.annotation.ImportIgnore;
import com.orion.office.csv.annotation.ImportSetting;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/7 15:55
 */
@ImportSetting(delimiter = ';')
public class ImportUser {

    @ImportField(value = 1)
    @ImportIgnore
    private int id;

    @ImportField(value = 2)
    private String name;

    @ImportField(value = 3)
    private String date;

    @ImportField(value = 4)
    private String desc;

    @ImportField(value = 5)
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

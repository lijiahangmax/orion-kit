package com.orion.test.script;

import com.orion.lang.utils.script.Bind;

import java.math.BigDecimal;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 14:51
 */
public class Exp {

    private int id;

    @Bind("name")
    private String name;

    @Bind("num")
    private BigDecimal num;

    @Bind("price")
    private BigDecimal price;

    @Bind("id")
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

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}

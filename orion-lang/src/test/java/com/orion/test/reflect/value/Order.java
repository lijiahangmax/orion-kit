package com.orion.test.reflect.value;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 14:06
 */
public class Order {

    @OrderAnno(value = "id f")
    int id;

    @OrderAnno(value = "name f")
    String name;

    String mobile;

    @OrderAnno(value = "222")
    public Order() {
    }

    public Order(int id) {
    }

    @OrderAnno
    public Order(int id, String name) {
    }

    @OrderAnno(value = "id get")
    public int getId() {
        return id;
    }

    @OrderAnno(value = "id set")
    public void setId(int id) {
        this.id = id;
    }

    @OrderAnno(value = "name get")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    @OrderAnno(value = "mobile set")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

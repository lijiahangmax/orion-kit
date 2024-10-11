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
package com.orion.test.reflect.value;

import java.math.BigDecimal;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/19 17:36
 */
@UserAnno1(value = "231231")
public class User extends BaseUser {

    public static Integer C = 1;

    @UserAnno1
    private Long id;

    @UserAnno2
    private int age;

    @UserAnno1
    @UserAnno2
    private boolean sex;

    private String name;

    private User father;

    public User() {
    }

    @UserAnno1
    public User(Long id) {
        this.id = id;
    }

    @UserAnno1
    @UserAnno2
    public User(int a, int a1) {
    }

    public void say(@UserAnno1 String name, @UserAnno2 int age) {
        System.out.println("say" + name + age);
    }

    @UserAnno1
    public Long getId() {
        return id;
    }

    @UserAnno2
    public void setId(Long id) {
        this.id = id;
    }

    @UserAnno1
    @UserAnno2
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getFather() {
        return father;
    }

    public void setFather(User father) {
        this.father = father;
    }

    public boolean isOk() {
        return true;
    }

    public int getTarget() {
        return 1;
    }

    public static Integer getC() {
        return C;
    }

    @Override
    public BigDecimal getBalance() {
        return super.getBalance();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", sex=" + sex +
                ", name='" + name + '\'' +
                ", father=" + father +
                "} " + super.toString();
    }
}

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
package cn.orionsec.kit.test.reflect.value;

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

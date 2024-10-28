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
package cn.orionsec.kit.test.location;

import cn.orionsec.kit.ext.location.ext.LocationExt;
import cn.orionsec.kit.ext.location.region.LocationRegions;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/10 11:23
 */
public class LocationTests {

    private static String[] ip = new String[]{
            "61.148.8.54",
            "64.2.45.96",
            "20.12.75.177",
            "129.168.146.238",
            "10.2.0.181"};

    public static void main(String[] args) {
        for (String s : ip) {
            System.out.println(s + " " + LocationExt.getAddress(s));
        }
        System.out.println("-----------");
        for (String s : ip) {
            System.out.println(s + " " + LocationRegions.getAddress(s));
        }
    }

}

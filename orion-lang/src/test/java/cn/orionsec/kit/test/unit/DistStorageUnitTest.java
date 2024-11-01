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
package cn.orionsec.kit.test.unit;

import cn.orionsec.kit.lang.utils.unit.DistStorageUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 10:55
 */
public class DistStorageUnitTest {

    public static void main(String[] args) {
        System.out.println(DistStorageUnit.BIT.toBit(8192));
        System.out.println(DistStorageUnit.BIT.toByte(8192));
        System.out.println(DistStorageUnit.BIT.toKilobyte(8192));
        System.out.println(DistStorageUnit.BIT.toMegabyte(8388608));
        System.out.println(DistStorageUnit.BIT.toGigabyte(8589934592L));
        System.out.println(DistStorageUnit.BIT.toTerabyte(8796093022208L));
        System.out.println("------");
        System.out.println(DistStorageUnit.B.toBit(1024));
        System.out.println(DistStorageUnit.B.toByte(1024));
        System.out.println(DistStorageUnit.B.toKilobyte(1024));
        System.out.println(DistStorageUnit.B.toMegabyte(1048576));
        System.out.println(DistStorageUnit.B.toGigabyte(1073741824L));
        System.out.println(DistStorageUnit.B.toTerabyte(1099511627776L));
        System.out.println("------");
        System.out.println(DistStorageUnit.KB.toBit(1));
        System.out.println(DistStorageUnit.KB.toByte(1));
        System.out.println(DistStorageUnit.KB.toKilobyte(1024));
        System.out.println(DistStorageUnit.KB.toMegabyte(1048576));
        System.out.println(DistStorageUnit.KB.toGigabyte(1073741824L));
        System.out.println(DistStorageUnit.KB.toTerabyte(1099511627776L));
        System.out.println("------");
        System.out.println(DistStorageUnit.MB.toBit(1));
        System.out.println(DistStorageUnit.MB.toByte(1));
        System.out.println(DistStorageUnit.MB.toKilobyte(1));
        System.out.println(DistStorageUnit.MB.toMegabyte(1));
        System.out.println(DistStorageUnit.MB.toGigabyte(1024));
        System.out.println(DistStorageUnit.MB.toTerabyte(1048576));
        System.out.println("------");
        System.out.println(DistStorageUnit.GB.toBit(1));
        System.out.println(DistStorageUnit.GB.toByte(1));
        System.out.println(DistStorageUnit.GB.toKilobyte(1));
        System.out.println(DistStorageUnit.GB.toMegabyte(1));
        System.out.println(DistStorageUnit.GB.toGigabyte(1024));
        System.out.println(DistStorageUnit.GB.toTerabyte(1048576));
        System.out.println("------");
        System.out.println(DistStorageUnit.TB.toBit(1));
        System.out.println(DistStorageUnit.TB.toByte(1));
        System.out.println(DistStorageUnit.TB.toKilobyte(1));
        System.out.println(DistStorageUnit.TB.toMegabyte(1));
        System.out.println(DistStorageUnit.TB.toGigabyte(1));
        System.out.println(DistStorageUnit.TB.toTerabyte(1));
    }

}

package com.orion.test.unit;

import com.orion.lang.utils.unit.DistStorageUnit;

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

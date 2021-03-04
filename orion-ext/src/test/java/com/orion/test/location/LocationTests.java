package com.orion.test.location;

import com.orion.location.ext.LocationExt;
import com.orion.location.region.LocationRegions;

/**
 * @author ljh15
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

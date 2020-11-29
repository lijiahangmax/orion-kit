package com;

import com.orion.lang.Console;
import com.orion.utils.collect.Lists;
import com.orion.utils.reflect.PackageScanners;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/23 18:33
 */
public class PackageScanTests {


    public static void main(String[] args) throws ClassNotFoundException, IOException {
        PackageScanners s = new PackageScanners();
        // s.getScanClasses().forEach(Console::trace);

        Enumeration<URL> resources = PackageScanTests.class.getClassLoader().getResources("");
        Lists.as(resources).forEach(Console::trace);

        System.out.println("-----");
        System.out.println("-----");
        System.out.println("-----");
    }

}

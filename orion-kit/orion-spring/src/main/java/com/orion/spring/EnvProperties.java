package com.orion.spring;

import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.collect.MutableHashSet;
import com.orion.utils.ext.PropertiesExt;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * maven多环境配置
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/5 23:22
 */
public class EnvProperties {

    /*
     pom.xml
     <build>
         <filters>
             <filter>src/main/java/com/orion/filter/${env}.properties</filter>
         </filters>

         <resources>
             <resource>
                 <directory>src/main/resources</directory>
                 <filtering>true</filtering>
             </resource>
         </resources>
     </build>

     application.xml
     <bean id="envPropertiesInitialize" class="com.orion.spring.EnvProperties$InitializeProperties">
        <constructor-arg index="0" value="env.properties"/> resources根目录
     </bean>
     */

    /**
     * 配置文件
     */
    private static PropertiesExt propertiesExt;

    private EnvProperties() {
    }

    public static String getValue(String key) {
        return propertiesExt.getValue(key);
    }

    public static String getValue(String key, String def) {
        return propertiesExt.getValue(key, def);
    }

    public static MutableHashMap getValues() {
        return propertiesExt.getValues();
    }

    public static MutableHashSet getKeys() {
        return propertiesExt.getKeys();
    }

    public static void setValue(String key, String value) {
        propertiesExt.setValue(key, value);
    }

    public static Properties getProperties() {
        return propertiesExt.getProperties();
    }

    public static PropertiesExt setProperties(Properties properties) {
        return propertiesExt.setProperties(properties);
    }

    public static PropertiesExt getPropertiesExt() {
        return EnvProperties.propertiesExt;
    }

    public static PropertiesExt setPropertiesExt(PropertiesExt propertiesExt) {
        return EnvProperties.propertiesExt = propertiesExt;
    }

    private static class InitializeProperties implements InitializingBean {

        private String propertiesPath;

        public InitializeProperties(String propertiesPath) {
            this.propertiesPath = propertiesPath;
            System.out.println("EnvProperties: load env properties file: " + propertiesPath);
        }

        @Override
        public void afterPropertiesSet() {
            try {
                EnvProperties.propertiesExt = new PropertiesExt(this.propertiesPath);
                System.out.println("EnvProperties: load env properties success...");
            } catch (Exception e) {
                System.err.println("EnvProperties: load env properties fail...");
                throw new RuntimeException(e);
            }
        }
    }

}

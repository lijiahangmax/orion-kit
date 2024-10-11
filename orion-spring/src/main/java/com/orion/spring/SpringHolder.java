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
package com.orion.spring;

import com.orion.lang.utils.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring 工具类
 * <p>
 * 需要配置 <bean id="springHolder" class="com.orion.spring.SpringHolder$ApplicationContextAwareStore"/>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/2/3 14:36
 */
public class SpringHolder {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringHolder.class);

    private static ApplicationContext applicationContext;

    private static ConfigurableListableBeanFactory beanFactory;

    private SpringHolder() {
    }

    public static class ApplicationContextAwareStore implements ApplicationContextAware, BeanFactoryPostProcessor {

        public ApplicationContextAwareStore() {
            LOGGER.info("init spring holder");
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            LOGGER.info("inject spring holder ApplicationContext");
            SpringHolder.applicationContext = applicationContext;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            LOGGER.info("inject spring holder BeanFactory");
            SpringHolder.beanFactory = configurableListableBeanFactory;
        }

    }

    /**
     * 获取上下文容器
     *
     * @return ignore
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 发布事件
     *
     * @param event event
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * 发布事件
     *
     * @param event event
     */
    public static void publishEvent(Object event) {
        applicationContext.publishEvent(event);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return ((T) applicationContext.getBean(beanName));
    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }

    public static <T> T getBean(String beanName, Class<T> type) {
        return applicationContext.getBean(beanName, type);
    }

    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }

    public static String[] getBeanNames() {
        return applicationContext.getBeanDefinitionNames();
    }

    public static boolean isSingletonBean(String beanName) {
        return applicationContext.isSingleton(beanName);
    }

    public static Class<?> getType(String beanName) {
        return applicationContext.getType(beanName);
    }

    public static boolean isType(String beanName, Class<?> beanType) {
        return applicationContext.isTypeMatch(beanName, beanType);
    }

    public static String[] getBeanAliases(String beanName) {
        return applicationContext.getAliases(beanName);
    }

    public static void close() {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) applicationContext).close();
        }
    }

    public static void refresh() {
        Valid.isInstanceOf(applicationContext, ConfigurableApplicationContext.class);
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringHolder.applicationContext;
        applicationContext.refresh();
    }

}

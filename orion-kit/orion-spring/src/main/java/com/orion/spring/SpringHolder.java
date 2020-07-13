package com.orion.spring;

import com.orion.utils.Valid;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * spring工具类
 * 需要配置 <bean id="springHolder" class="com.orion.spring.SpringHolder$ApplicationContextAwareStore"/>
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/3 14:36
 */
public class SpringHolder {

    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

    private SpringHolder() {
    }

    public static class ApplicationContextAwareStore implements ApplicationContextAware, BeanFactoryPostProcessor {

        public ApplicationContextAwareStore() {
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            System.out.println("SpringHolder: Inject ApplicationContext");
            SpringHolder.applicationContext = applicationContext;
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            System.out.println("SpringHolder: Inject BeanFactory");
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

    public static Class getType(String beanName) {
        return applicationContext.getType(beanName);
    }

    public static boolean isType(String beanName, Class beanType) {
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
        Valid.isInstanceOf(ConfigurableApplicationContext.class, applicationContext);
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringHolder.applicationContext;
        applicationContext.refresh();
    }

}

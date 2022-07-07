package com.orion.test.reflect;

import com.orion.lang.define.Console;
import com.orion.lang.utils.VariableStyles;
import com.orion.lang.utils.reflect.BeanMap;
import com.orion.lang.utils.reflect.BeanWrapper;
import com.orion.test.reflect.value.Shop;
import com.orion.test.reflect.value.User;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/20 10:56
 */
public class BeanTests {

    @Test
    public void beanMap() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        user.setBalance(BigDecimal.ONE);
        BeanMap.create(user, VariableStyles.BIG_HUMP).forEach(Console::trace);
    }

    @Test
    public void copy() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        User user1 = BeanWrapper.copyProperties(user, user.getClass());
        System.out.println(user1);
        user1 = BeanWrapper.copyProperties(user, user.getClass(), "name");
        System.out.println(user1);
    }

    @Test
    public void toBean1() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        user.setBalance(BigDecimal.ONE);
        BeanMap beanMap = BeanMap.create(user, true);
        HashMap<Object, Object> map = new HashMap<>();
        map.put(1, 2);
        map.put("x", 2);
        beanMap.put("map", map);
        User user1 = BeanWrapper.toBean(beanMap, user.getClass());
        System.out.println(user1);
    }

    @Test
    public void toBean2() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        user.setBalance(BigDecimal.ONE);
        BeanMap beanMap = BeanMap.create(user, true);
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setStatus(true);
        beanMap.put("map", shop);
        User user1 = BeanWrapper.toBean(beanMap, user.getClass());
        System.out.println(user1);
    }

    @Test
    public void toBean3() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        user.setBalance(BigDecimal.ONE);
        BeanMap beanMap = BeanMap.create(user, true);
        User father = new User();
        father.setVipType(1);
        beanMap.put("father", father);
        User user1 = BeanWrapper.toBean(beanMap, user.getClass());
        System.out.println(user1);
    }

    @Test
    public void toBean4() {
        User user = new User();
        user.setAge(1);
        user.setName("whh");
        user.setType(1);
        user.setVipType(2);
        user.setBalance(BigDecimal.ONE);
        BeanMap beanMap = BeanMap.create(user, true);
        Map<Object, Object> father = new HashMap<>();
        father.put("age", 33);
        father.put("type", 33);
        beanMap.put("father", father);
        User user1 = BeanWrapper.toBean(beanMap, user.getClass());
        System.out.println(user1);
    }

}

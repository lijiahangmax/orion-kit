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
package cn.orionsec.kit.test.reflect;

import cn.orionsec.kit.lang.define.Console;
import cn.orionsec.kit.lang.utils.VariableStyles;
import cn.orionsec.kit.lang.utils.reflect.BeanMap;
import cn.orionsec.kit.lang.utils.reflect.BeanWrapper;
import cn.orionsec.kit.test.reflect.value.Shop;
import cn.orionsec.kit.test.reflect.value.User;
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

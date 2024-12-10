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
package cn.orionsec.kit.generator;

import cn.orionsec.kit.generator.faker.Faker;
import cn.orionsec.kit.generator.faker.FakerInfo;
import cn.orionsec.kit.generator.faker.FakerType;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/14 1:22
 */
public class FakerTest {

    @Test
    public void faker1() {
        for (int i = 0; i < 20; i++) {
            FakerInfo faker = Faker.generator(FakerType.BASE);
            System.out.println(JSON.toJSONString(faker, true));
            System.out.println();
        }
    }

    @Test
    public void faker2() {
        for (int i = 0; i < 20; i++) {
            FakerInfo faker = Faker.generator(FakerType.ALL);
            System.out.println(JSON.toJSONString(faker, true));
            System.out.println();
        }
    }

    @Test
    public void faker3() {
        for (int i = 0; i < 20; i++) {
            FakerInfo faker = Faker.generator(FakerType.ID_CARD, FakerType.DEBIT_CARD);
            System.out.println(JSON.toJSONString(faker, true));
            System.out.println();
        }
    }

}
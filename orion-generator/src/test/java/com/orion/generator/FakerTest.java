package com.orion.generator;

import com.alibaba.fastjson.JSON;
import com.orion.generator.faker.Faker;
import com.orion.generator.faker.FakerInfo;
import com.orion.generator.faker.FakerType;
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

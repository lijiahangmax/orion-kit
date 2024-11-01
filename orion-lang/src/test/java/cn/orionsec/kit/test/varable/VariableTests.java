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
package cn.orionsec.kit.test.varable;

import cn.orionsec.kit.lang.utils.VariableStyles;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:20
 */
public class VariableTests {

    @Test
    public void to1() {
        System.out.println(VariableStyles.convert("A_user_NEW", VariableStyles.SMALL_HUMP));
        System.out.println(VariableStyles.convert("A-user-NEW", VariableStyles.SMALL_HUMP));
        System.out.println(VariableStyles.convert("UserNameDto", VariableStyles.SMALL_HUMP));
        System.out.println(VariableStyles.convert("UserNameDTO", VariableStyles.SMALL_HUMP));
        System.out.println(VariableStyles.convert("userNameDto", VariableStyles.SMALL_HUMP));
        System.out.println(VariableStyles.convert("userNameDTO", VariableStyles.SMALL_HUMP));
        System.out.println();
    }

    @Test
    public void to2() {
        System.out.println(VariableStyles.convert("A_user_NEW", VariableStyles.BIG_HUMP));
        System.out.println(VariableStyles.convert("A-user-NEW", VariableStyles.BIG_HUMP));
        System.out.println(VariableStyles.convert("UserNameDto", VariableStyles.BIG_HUMP));
        System.out.println(VariableStyles.convert("UserNameDTO", VariableStyles.BIG_HUMP));
        System.out.println(VariableStyles.convert("userNameDto", VariableStyles.BIG_HUMP));
        System.out.println(VariableStyles.convert("userNameDTO", VariableStyles.BIG_HUMP));
        System.out.println();
    }

    @Test
    public void to3() {
        System.out.println(VariableStyles.convert("A_user_NEW", VariableStyles.SERPENTINE));
        System.out.println(VariableStyles.convert("A-user-NEW", VariableStyles.SERPENTINE));
        System.out.println(VariableStyles.convert("UserNameDto", VariableStyles.SERPENTINE));
        System.out.println(VariableStyles.convert("UserNameDTO", VariableStyles.SERPENTINE));
        System.out.println(VariableStyles.convert("userNameDto", VariableStyles.SERPENTINE));
        System.out.println(VariableStyles.convert("userNameDTO", VariableStyles.SERPENTINE));
        System.out.println();
    }

    @Test
    public void to4() {
        System.out.println(VariableStyles.convert("A_user_NEW", VariableStyles.SPINE));
        System.out.println(VariableStyles.convert("A-user-NEW", VariableStyles.SPINE));
        System.out.println(VariableStyles.convert("UserNameDto", VariableStyles.SPINE));
        System.out.println(VariableStyles.convert("UserNameDTO", VariableStyles.SPINE));
        System.out.println(VariableStyles.convert("userNameDto", VariableStyles.SPINE));
        System.out.println(VariableStyles.convert("userNameDTO", VariableStyles.SPINE));
        System.out.println();
    }

    @Test
    public void to5() {
        System.out.println(VariableStyles.BIG_HUMP.toSmallHump("UserNameString"));
        System.out.println(VariableStyles.BIG_HUMP.toSerpentine("UserNameString"));
        System.out.println(VariableStyles.BIG_HUMP.toSpine("UserNameString"));
    }

}

package com.orion.test.varable;

import com.orion.lang.utils.VariableStyles;
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

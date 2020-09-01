package com.orion.test.varable;

import com.orion.enums.VariableStyleEnum;
import com.orion.utils.Strings;
import org.junit.Test;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/13 18:20
 */
public class VarableTests {

    @Test
    public void to1() {
        System.out.println(Strings.convertVariableStyle("A_user_NBW", VariableStyleEnum.SMALL_HUMP));
        System.out.println(Strings.convertVariableStyle("A-user-NBW", VariableStyleEnum.SMALL_HUMP));
        System.out.println(Strings.convertVariableStyle("UserNameDto", VariableStyleEnum.SMALL_HUMP));
        System.out.println(Strings.convertVariableStyle("UserNameDTO", VariableStyleEnum.SMALL_HUMP));
        System.out.println(Strings.convertVariableStyle("userNameDto", VariableStyleEnum.SMALL_HUMP));
        System.out.println(Strings.convertVariableStyle("userNameDTO", VariableStyleEnum.SMALL_HUMP));
    }

    @Test
    public void to2() {
        System.out.println(Strings.convertVariableStyle("A_user_NBW", VariableStyleEnum.BIG_HUMP));
        System.out.println(Strings.convertVariableStyle("A-user-NBW", VariableStyleEnum.BIG_HUMP));
        System.out.println(Strings.convertVariableStyle("UserNameDto", VariableStyleEnum.BIG_HUMP));
        System.out.println(Strings.convertVariableStyle("UserNameDTO", VariableStyleEnum.BIG_HUMP));
        System.out.println(Strings.convertVariableStyle("userNameDto", VariableStyleEnum.BIG_HUMP));
        System.out.println(Strings.convertVariableStyle("userNameDTO", VariableStyleEnum.BIG_HUMP));
    }

    @Test
    public void to3() {
        System.out.println(Strings.convertVariableStyle("A_user_NBW", VariableStyleEnum.SERPENTINE));
        System.out.println(Strings.convertVariableStyle("A-user-NBW", VariableStyleEnum.SERPENTINE));
        System.out.println(Strings.convertVariableStyle("UserNameDto", VariableStyleEnum.SERPENTINE));
        System.out.println(Strings.convertVariableStyle("UserNameDTO", VariableStyleEnum.SERPENTINE));
        System.out.println(Strings.convertVariableStyle("userNameDto", VariableStyleEnum.SERPENTINE));
        System.out.println(Strings.convertVariableStyle("userNameDTO", VariableStyleEnum.SERPENTINE));
    }

    @Test
    public void to4() {
        System.out.println(Strings.convertVariableStyle("A_user_NBW", VariableStyleEnum.THE_SPINE));
        System.out.println(Strings.convertVariableStyle("A-user-NBW", VariableStyleEnum.THE_SPINE));
        System.out.println(Strings.convertVariableStyle("UserNameDto", VariableStyleEnum.THE_SPINE));
        System.out.println(Strings.convertVariableStyle("UserNameDTO", VariableStyleEnum.THE_SPINE));
        System.out.println(Strings.convertVariableStyle("userNameDto", VariableStyleEnum.THE_SPINE));
        System.out.println(Strings.convertVariableStyle("userNameDTO", VariableStyleEnum.THE_SPINE));
    }

}

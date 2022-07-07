package com.orion.generator.mobile;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.random.Randoms;

/**
 * 手机号生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 12:19
 */
public class MobileGenerator {

    private static final int[] MOBILE_PREFIX;

    private MobileGenerator() {
    }

    static {
        MOBILE_PREFIX = new int[]{
                130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                145, 147,
                150, 151, 152, 153, 155, 156, 157, 158, 159,
                170, 176, 177, 178,
                180, 181, 182, 183, 184, 185, 186, 187, 188, 189
        };
    }

    /**
     * 随机获取前缀
     *
     * @return 前缀
     */
    public static String getMobilePrefix() {
        return Arrays1.random(MOBILE_PREFIX) + Strings.EMPTY;
    }

    /**
     * 生成手机号
     *
     * @return mobile
     */
    public static String generateMobile() {
        return generateMobile(getMobilePrefix());
    }

    /**
     * 生成手机号
     *
     * @param prefix 3位前缀
     * @return mobile
     */
    public static String generateMobile(String prefix) {
        return prefix + Strings.leftPad(Strings.EMPTY + Randoms.RANDOM.nextInt(99999999), 8, "0");
    }

}

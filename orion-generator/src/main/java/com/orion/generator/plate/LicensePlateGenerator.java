package com.orion.generator.plate;

import com.orion.generator.addres.AddressSupport;
import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;
import com.orion.utils.regexp.Matches;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 车牌生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/13 17:20
 */
public class LicensePlateGenerator {

    private static final Map<Integer, Character> PROVINCE_ABBR;

    private static final char[] LETTER;

    private LicensePlateGenerator() {
    }

    static {
        Map<Integer, Character> provinceAbbr = new HashMap<>();
        provinceAbbr.put(11, '京');
        provinceAbbr.put(31, '沪');
        provinceAbbr.put(12, '津');
        provinceAbbr.put(50, '渝');
        provinceAbbr.put(23, '黑');
        provinceAbbr.put(22, '吉');
        provinceAbbr.put(21, '辽');
        provinceAbbr.put(15, '蒙');
        provinceAbbr.put(13, '冀');
        provinceAbbr.put(65, '新');
        provinceAbbr.put(62, '甘');
        provinceAbbr.put(63, '青');
        provinceAbbr.put(61, '陕');
        provinceAbbr.put(64, '宁');
        provinceAbbr.put(41, '豫');
        provinceAbbr.put(37, '鲁');
        provinceAbbr.put(14, '晋');
        provinceAbbr.put(34, '皖');
        provinceAbbr.put(42, '鄂');
        provinceAbbr.put(43, '湘');
        provinceAbbr.put(32, '苏');
        provinceAbbr.put(51, '川');
        provinceAbbr.put(52, '贵');
        provinceAbbr.put(53, '云');
        provinceAbbr.put(45, '桂');
        provinceAbbr.put(54, '藏');
        provinceAbbr.put(33, '浙');
        provinceAbbr.put(36, '赣');
        provinceAbbr.put(44, '粤');
        provinceAbbr.put(35, '闽');
        provinceAbbr.put(46, '琼');
        provinceAbbr.put(81, '港');
        provinceAbbr.put(82, '澳');
        PROVINCE_ABBR = Collections.unmodifiableMap(provinceAbbr);
        LETTER = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    }

    public static String generator() {
        return generator(AddressSupport.randomProvinceCode());
    }

    /**
     * 生成车牌号
     *
     * @param provinceCode 省级编码
     * @return 车牌号
     */
    public static String generator(int provinceCode) {
        Character province = PROVINCE_ABBR.get(provinceCode);
        if (province == null) {
            return Strings.EMPTY;
        }
        StringBuilder sb = new StringBuilder()
                .append(province)
                .append(Arrays1.random(LETTER));
        int letterNum = 0;
        for (int i = 0; i < 5; i++) {
            if (letterNum >= 2) {
                // 控制字母数最多为2
                sb.append(Randoms.randomInt(10));
            } else if (Randoms.randomBoolean()) {
                // 字母
                sb.append(Arrays1.random(LETTER));
                letterNum++;
            } else if (i == 4 && letterNum == 0) {
                // 不能为纯数字
                sb.append(Arrays1.random(LETTER));
                letterNum++;
            } else {
                // 数字
                sb.append(Randoms.randomInt(10));
            }
        }
        String plat = sb.toString();
        // 不符合规范重新生成
        return Matches.isPlateNumber(plat) ? plat : generator(provinceCode);
    }

}

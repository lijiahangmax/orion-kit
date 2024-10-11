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
package com.orion.generator.addres;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.random.Randoms;

/**
 * 详细地址生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 18:23
 */
public class AddressGenerator {

    private static final String[] ADDRESS_CHAR;

    private static final String[] COMMUNITY;

    private AddressGenerator() {
    }

    static {
        ADDRESS_CHAR = new String[]{
                "门", "景", "山", "交", "道", "口", "安", "定", "桥", "书",
                "旧", "飞", "翔", "树", "华", "红", "源", "沙", "杨", "宋",
                "朝", "阳", "建", "国", "青", "和", "清", "香", "花", "长",
                "安", "方", "云", "平", "行", "昌", "流", "官", "各", "马",
                "龙", "潭", "海", "大", "小", "东", "南", "西", "北", "中",
                "上", "下", "苑", "碧", "腾", "轩", "城", "前", "辛", "园",
                "一", "二", "三", "四", "五", "六", "七", "八", "九", "十",
                "千", "万", "左", "右", "王", "石", "张", "李", "白", "顺",
                "玉", "金", "银", "渔", "风", "丰", "泉", "亦", "新", "山",
                "宫", "善", "良", "贤", "博", "荣", "兴", "怀", "宝", "兴",
                "军", "福", "荷", "河", "杭", "飞", "涑", "水", "家", "春",
                "夏", "秋", "冬", "尚", "彩", "虹", "富", "民", "登", "风"
        };

        COMMUNITY = new String[]{
                "金融", "天桥", "体育", "香河", "学院", "曙光", "温泉", "四季", "车站",
                "胜利", "麦子", "东街", "西街", "南街", "机场", "河滨", "璀璨", "碧水",
                "玉桥", "东风", "南风", "西风", "北风", "空港", "新华", "向阳", "迎风",
                "胜利", "星城", "经济", "仁和", "公园", "长虹", "双街", "大道", "富强",
                "北街", "高地", "红门", "大街", "平安", "文化", "文艺", "幸福", "太平",
                "城关", "通运", "榆树", "柳树", "小营", "全营", "荣华", "盛世", "江南",
                "航空", "综合", "彩虹", "光明", "飞翔", "春江", "摩卡", "满庭", "世代",
                "雅海", "太平", "葫芦", "文明"
        };
    }

    /**
     * 生成地址
     *
     * @return 地址
     */
    public static String generatorAddress() {
        int i = Randoms.randomInt(3);
        if (i == 0) {
            return generatorAddress(AddressType.COMMUNITY);
        } else if (i == 1) {
            return generatorAddress(AddressType.VILLAGE);
        } else {
            return generatorAddress(AddressType.STREET);
        }
    }

    /**
     * 生成地址
     *
     * @param type type
     * @return 地址
     */
    public static String generatorAddress(AddressType type) {
        if (AddressType.COMMUNITY.equals(type)) {
            // 小区
            return generatorCommunityAddress();
        } else if (AddressType.VILLAGE.equals(type)) {
            // 村
            return generatorVillageAddress();
        } else if (AddressType.STREET.equals(type)) {
            // 街道
            return generatorStreetAddress();
        } else {
            return Strings.EMPTY;
        }
    }

    /**
     * 生成小区地址
     *
     * @return 地址
     */
    public static String generatorCommunityAddress() {
        int i = Randoms.randomInt(8);
        StringBuilder sb = new StringBuilder(generatorName(AddressType.COMMUNITY));
        if (i >= 2) {
            // 拼接 楼 
            sb.append(Randoms.randomInt(19) + 1);
            int floor = Randoms.randomInt(3);
            if (floor == 0) {
                sb.append("栋");
            } else if (floor == 1) {
                sb.append("号楼");
            } else if (floor == 2) {
                sb.append("号院");
            }
        }
        if (i >= 4) {
            // 拼接 单元 
            sb.append(Randoms.randomInt(29) + 1)
                    .append("单元");
        }
        if (i >= 6) {
            // 拼接 户
            sb.append(Randoms.randomInt(101, 2500));
            if (Randoms.randomBoolean()) {
                sb.append("室");
            }
        }
        return sb.toString();
    }

    /**
     * 生成村地址
     *
     * @return 地址
     */
    public static String generatorVillageAddress() {
        int i = Randoms.randomInt(6);
        StringBuilder sb = new StringBuilder(generatorName(AddressType.VILLAGE));
        if (i == 0) {
            sb.append(Randoms.randomInt(200) + 1)
                    .append("户");
        } else if (i == 1) {
            sb.append(Randoms.randomInt(200) + 1)
                    .append("号");
        } else if (i == 2) {
            sb.append(Randoms.randomInt(30) + 1)
                    .append("组");
        } else if (i == 3) {
            sb.append(Randoms.randomInt(30) + 1)
                    .append("组")
                    .append(Randoms.randomInt(200) + 1)
                    .append("号");
        } else if (i == 4) {
            sb.append(Randoms.randomInt(30) + 1)
                    .append('-')
                    .append(Randoms.randomInt(200) + 1);
        }
        return sb.toString();
    }

    /**
     * 生成街道地址
     *
     * @return 地址
     */
    public static String generatorStreetAddress() {
        int i = Randoms.randomInt(4);
        StringBuilder sb = new StringBuilder(generatorName(AddressType.STREET));
        if (i == 0) {
            sb.append(Randoms.randomInt(400) + 1)
                    .append("户");
        } else if (i == 1) {
            sb.append(Randoms.randomInt(400) + 1)
                    .append("号");
        } else if (i == 2) {
            sb.append(Randoms.randomInt(50) + 1)
                    .append('-')
                    .append(Randoms.randomInt(400) + 1);
        }
        return sb.toString();
    }

    /**
     * 生成身份证地址
     *
     * @return 身份证地址
     */
    public static String generatorIdCardAddress() {
        int i = Randoms.randomInt(3);
        StringBuilder sb;
        if (i == 0) {
            sb = new StringBuilder(generatorName(AddressType.COMMUNITY))
                    .append(Randoms.randomInt(19) + 1);
            int floor = Randoms.randomInt(3);
            if (floor == 0) {
                sb.append("栋");
            } else if (floor == 1) {
                sb.append("号楼");
            } else if (floor == 2) {
                sb.append("号院");
            }
            sb.append(Randoms.randomInt(29) + 1)
                    .append("单元")
                    .append(Randoms.randomInt(101, 2500))
                    .append("室");
        } else if (i == 1) {
            sb = new StringBuilder(generatorName(AddressType.VILLAGE));
            int no = Randoms.randomInt(4);
            if (no == 0) {
                sb.append(Randoms.randomInt(200) + 1)
                        .append("户");
            } else if (no == 1) {
                sb.append(Randoms.randomInt(200) + 1)
                        .append("号");
            } else if (no == 2) {
                sb.append(Randoms.randomInt(30) + 1)
                        .append("组");
            } else if (no == 3) {
                sb.append(Randoms.randomInt(30) + 1)
                        .append("组")
                        .append(Randoms.randomInt(200) + 1)
                        .append("号");
            }
        } else {
            sb = new StringBuilder(generatorName(AddressType.STREET));
            int no = Randoms.randomInt(2);
            if (no == 0) {
                sb.append(Randoms.randomInt(400) + 1)
                        .append("号");
            } else if (no == 1) {
                sb.append(Randoms.randomInt(50) + 1)
                        .append('-')
                        .append(Randoms.randomInt(400) + 1)
                        .append("号");
            }
        }
        return sb.toString();
    }

    /**
     * 生成名称
     *
     * @return 名称
     */
    public static String generatorName() {
        int i = Randoms.randomInt(3);
        if (i == 0) {
            return generatorName(AddressType.COMMUNITY);
        } else if (i == 1) {
            return generatorName(AddressType.VILLAGE);
        } else {
            return generatorName(AddressType.STREET);
        }
    }

    /**
     * 生成名称
     *
     * @param type type
     * @return 名称
     */
    public static String generatorName(AddressType type) {
        return genName() + Arrays1.random(type.getSuffix());
    }

    /**
     * 生成名称
     *
     * @return 名称
     */
    private static String genName() {
        int i = Randoms.randomInt(100);
        if (i <= 10) {
            // 完全随机
            return Strings.randomChars(Randoms.randomInt(2, 6));
        } else if (i <= 20) {
            // 随机
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < Randoms.randomInt(2, 5) + 1; j++) {
                sb.append(Arrays1.random(ADDRESS_CHAR));
            }
            return sb.toString();
        } else if (i <= 30) {
            // 重复
            return Arrays1.random(COMMUNITY) + Arrays1.random(COMMUNITY);
        } else if (i <= 55) {
            // 左拼接
            return Arrays1.random(ADDRESS_CHAR) + Arrays1.random(COMMUNITY);
        } else if (i <= 80) {
            // 右拼接
            return Arrays1.random(COMMUNITY) + Arrays1.random(ADDRESS_CHAR);
        } else {
            // 直接
            return Arrays1.random(COMMUNITY);
        }
    }

}

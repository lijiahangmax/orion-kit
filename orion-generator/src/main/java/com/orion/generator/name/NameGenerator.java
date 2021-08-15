package com.orion.generator.name;

import com.orion.lang.collect.WeightRandomMap;
import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import com.orion.utils.random.Randoms;

/**
 * 中文名称生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/8 20:22
 */
public class NameGenerator {

    private static final WeightRandomMap<String> FIRST_NAME;

    private static final String[] BOY_NAME;

    private static final String[] GIRL_NAME;

    private NameGenerator() {
    }

    static {
        FIRST_NAME = new WeightRandomMap<>();
        FIRST_NAME.put("李", 7.94);
        FIRST_NAME.put("王", 7.41);
        FIRST_NAME.put("张", 7.07);
        FIRST_NAME.put("刘", 5.38);
        FIRST_NAME.put("陈", 4.53);
        FIRST_NAME.put("杨", 3.08);
        FIRST_NAME.put("趙", 2.29);
        FIRST_NAME.put("黄", 2.23);
        FIRST_NAME.put("周", 2.12);
        FIRST_NAME.put("吴", 2.05);
        FIRST_NAME.put("徐", 1.67);
        FIRST_NAME.put("孙", 1.54);
        FIRST_NAME.put("胡", 1.31);
        FIRST_NAME.put("朱", 1.26);
        FIRST_NAME.put("高", 1.21);
        FIRST_NAME.put("林", 1.18);
        FIRST_NAME.put("何", 1.17);
        FIRST_NAME.put("郭", 1.15);
        FIRST_NAME.put("岳", 1.15);
        FIRST_NAME.put("马", 1.05);
        FIRST_NAME.put("罗", 0.86);
        FIRST_NAME.put("梁", 0.84);
        FIRST_NAME.put("宋", 0.81);
        FIRST_NAME.put("郑", 0.78);
        FIRST_NAME.put("谢", 0.72);
        FIRST_NAME.put("韩", 0.68);
        FIRST_NAME.put("唐", 0.65);
        FIRST_NAME.put("冯", 0.64);
        FIRST_NAME.put("于", 0.62);
        FIRST_NAME.put("董", 0.61);
        FIRST_NAME.put("萧", 0.59);
        FIRST_NAME.put("程", 0.57);
        FIRST_NAME.put("曹", 0.57);
        FIRST_NAME.put("袁", 0.54);
        FIRST_NAME.put("邓", 0.54);
        FIRST_NAME.put("许", 0.54);
        FIRST_NAME.put("傅", 0.51);
        FIRST_NAME.put("沈", 0.50);
        FIRST_NAME.put("曾", 0.50);
        FIRST_NAME.put("彭", 0.49);
        FIRST_NAME.put("吕", 0.47);
        FIRST_NAME.put("苏", 0.47);
        FIRST_NAME.put("卢", 0.47);
        FIRST_NAME.put("蒋", 0.47);
        FIRST_NAME.put("蔡", 0.46);
        FIRST_NAME.put("魏", 0.45);
        FIRST_NAME.put("贾", 0.42);
        FIRST_NAME.put("丁", 0.42);
        FIRST_NAME.put("薛", 0.42);
        FIRST_NAME.put("叶", 0.42);
        FIRST_NAME.put("阎", 0.41);
        FIRST_NAME.put("余", 0.41);
        FIRST_NAME.put("潘", 0.41);
        FIRST_NAME.put("杜", 0.40);
        FIRST_NAME.put("戴", 0.39);
        FIRST_NAME.put("夏", 0.39);
        FIRST_NAME.put("钟", 0.38);
        FIRST_NAME.put("汪", 0.37);
        FIRST_NAME.put("田", 0.37);
        FIRST_NAME.put("任", 0.37);
        FIRST_NAME.put("姜", 0.36);
        FIRST_NAME.put("范", 0.36);
        FIRST_NAME.put("方", 0.36);
        FIRST_NAME.put("石", 0.35);
        FIRST_NAME.put("姚", 0.35);
        FIRST_NAME.put("谭", 0.34);
        FIRST_NAME.put("廖", 0.34);
        FIRST_NAME.put("邹", 0.33);
        FIRST_NAME.put("熊", 0.32);
        FIRST_NAME.put("金", 0.32);
        FIRST_NAME.put("陆", 0.31);
        FIRST_NAME.put("郝", 0.30);
        FIRST_NAME.put("孔", 0.29);
        FIRST_NAME.put("白", 0.29);
        FIRST_NAME.put("崔", 0.28);
        FIRST_NAME.put("康", 0.28);
        FIRST_NAME.put("毛", 0.27);
        FIRST_NAME.put("邱", 0.27);
        FIRST_NAME.put("秦", 0.26);
        FIRST_NAME.put("江", 0.26);
        FIRST_NAME.put("史", 0.25);
        FIRST_NAME.put("顾", 0.25);
        FIRST_NAME.put("侯", 0.25);
        FIRST_NAME.put("邵", 0.24);
        FIRST_NAME.put("孟", 0.24);
        FIRST_NAME.put("龙", 0.24);
        FIRST_NAME.put("万", 0.24);
        FIRST_NAME.put("段", 0.23);
        FIRST_NAME.put("雷", 0.23);
        FIRST_NAME.put("钱", 0.22);
        FIRST_NAME.put("汤", 0.19);
        FIRST_NAME.put("尹", 0.19);
        FIRST_NAME.put("易", 0.19);
        FIRST_NAME.put("黎", 0.19);
        FIRST_NAME.put("常", 0.18);
        FIRST_NAME.put("武", 0.18);
        FIRST_NAME.put("乔", 0.18);
        FIRST_NAME.put("贺", 0.18);
        FIRST_NAME.put("赖", 0.18);
        FIRST_NAME.put("龚", 0.17);
        FIRST_NAME.put("文", 0.17);

        BOY_NAME = Arrays1.of("伟", "刚", "勇", "毅", "俊", "峰", "强", "军", "平", "保", "东", "文", "辉", "力",
                "明", "永", "健", "世", "广", "志", "义", "兴", "良", "海", "山", "仁", "波", "宁",
                "贵", "福", "生", "龙", "元", "全", "国", "胜", "学", "祥", "才", "发", "武", "新",
                "利", "清", "飞", "彬", "富", "顺", "信", "子", "杰", "涛", "昌", "成", "康", "星",
                "光", "天", "达", "安", "岩", "中", "茂", "进", "林", "有", "坚", "和", "彪", "博",
                "诚", "先", "敬", "震", "振", "壮", "会", "思", "群", "豪", "心", "邦", "承", "乐",
                "绍", "功", "松", "善", "厚", "庆", "磊", "民", "友", "裕", "河", "哲", "江", "超",
                "浩", "亮", "政", "谦", "亨", "奇", "固", "之", "轮", "翰", "朗", "伯", "宏", "言",
                "若", "鸣", "朋", "斌", "梁", "栋", "维", "启", "克", "伦", "翔", "旭", "鹏", "泽",
                "晨", "辰", "士", "以", "建", "家", "致", "树", "炎", "德", "行", "时", "泰", "盛",
                "凯", "轩", "宇", "坤", "航", "铭", "波", "君", "一", "显", "忠", "通", "硕");

        GIRL_NAME = Arrays1.of("俞", "倩", "倪", "倰", "偀", "偲", "妆", "佳", "亿", "仪", "寒",
                "宜", "妶", "好", "妃", "姗", "姝", "姹", "姿", "婵", "佳", "璐",
                "姑", "姜", "姣", "嫂", "嫦", "嫱", "姬", "娇", "娟", "嫣", "婕",
                "婧", "娴", "婉", "姐", "姞", "姯", "姲", "姳", "娘", "娜", "妹",
                "妍", "妙", "妹", "娆", "娉", "娥", "媚", "媱", "嫔", "婷", "玟",
                "环", "珆", "珊", "珠", "玲", "珴", "瑛", "琼", "瑶", "瑾", "瑞",
                "珍", "琦", "玫", "琪", "琳", "环", "琬", "瑗", "琰", "薇", "珂",
                "芬", "芳", "芯", "花", "茜", "荭", "荷", "莲", "莉", "莹", "菊",
                "芝", "萍", "燕", "苹", "荣", "草", "蕊", "芮", "蓝", "莎", "菀",
                "菁", "苑", "芸", "芊", "茗", "荔", "菲", "蓉", "英", "蓓", "蕾",
                "薰", "颖", "芃", "蔓", "莓", "曼", "水", "淼", "滟", "滢", "淑",
                "洁", "清", "澜", "波", "淞", "渺", "漩", "漪", "涟", "湾", "汇",
                "冰", "冷", "冽", "霜", "雪", "霞", "霖", "香", "馡", "馥", "秋",
                "秀", "露", "飘", "育", "滢", "馥", "筠", "柔", "竹", "霭", "凝",
                "晓", "欢", "枫", "巧", "美", "静", "惠", "翠", "雅", "红", "春",
                "岚", "嵘", "兰", "羽", "素", "云", "华", "丽", "俪", "叆", "呤",
                "咛", "囡", "彩", "彤", "彨", "怜", "晴", "月", "明", "晶", "昭",
                "星", "卿", "毓", "可", "璧", "青", "灵", "彩", "慧", "盈", "眉",
                "艳", "凡", "凤", "风", "贞", "勤", "叶", "雁", "钰", "嘉", "锦",
                "黛", "怡", "情", "林", "梦", "越", "悦", "希", "宁", "欣", "容",
                "丹", "彤", "颜", "影", "韵", "音", "银", "纯", "纹", "思", "丝",
                "纤", "爽", "舒", "伊", "依", "亚", "融", "园", "文", "心", "火",
                "炎", "烁", "炫", "煜", "烟", "炅", "然", "冉", "平", "屏", "评",
                "宛", "玉", "雨", "樱", "梅", "媛", "微");
    }

    /**
     * 所及获取一个姓氏
     *
     * @return 姓氏
     */
    public static String generatorFirstName() {
        return FIRST_NAME.next();
    }

    /**
     * 获取名称 男 1个字
     *
     * @return 男名字
     */
    public static String generatorBoyName1() {
        return Arrays1.random(BOY_NAME);
    }

    /**
     * 获取名称 女 1个字
     *
     * @return 女名字
     */
    public static String generatorGirlName1() {
        return Arrays1.random(GIRL_NAME);
    }

    /**
     * 获取名称 男 2个字
     *
     * @return 男名字
     */
    public static String generatorBoyName2() {
        if (Randoms.randomBoolean(5)) {
            return Arrays1.random(GIRL_NAME) + Arrays1.random(BOY_NAME);
        } else {
            return Arrays1.random(BOY_NAME) + Arrays1.random(BOY_NAME);
        }
    }

    /**
     * 获取名称 女 2个字
     *
     * @return 女名字
     */
    public static String generatorGirlName2() {
        if (Randoms.randomBoolean(5)) {
            return Arrays1.random(BOY_NAME) + Arrays1.random(GIRL_NAME);
        } else {
            return Arrays1.random(GIRL_NAME) + Arrays1.random(GIRL_NAME);
        }
    }

    /**
     * 获取随机一个中文
     *
     * @return 中文
     */
    public static String generatorChar() {
        return Randoms.randomChineseChar() + Strings.EMPTY;
    }

    /**
     * 获取一个随机男名称 2字
     *
     * @return 男名
     */
    public static String generatorBoyNameHasRandom() {
        return Randoms.randomChineseChar() + Arrays1.random(BOY_NAME);
    }

    /**
     * 获取一个随机女名称 2字
     *
     * @return 女名
     */
    public static String generatorGirlNameHasRandom() {
        return Randoms.randomChineseChar() + Arrays1.random(GIRL_NAME);
    }

    /**
     * 随机名称
     *
     * @return 名称
     */
    public static String generatorName() {
        return generatorName(Randoms.randomBoolean(2));
    }

    /**
     * 随机名称
     *
     * @param sex 性别 男true
     * @return 名称
     */
    public static String generatorName(boolean sex) {
        return generatorName(sex, Randoms.randomBoolean(3));
    }

    /**
     * 随机名称
     *
     * @param sex     性别 男true
     * @param oneChar 是否是单字
     * @return 名称
     */
    public static String generatorName(boolean sex, boolean oneChar) {
        if (sex) {
            return FIRST_NAME.next() + (oneChar ? generatorBoyName1() : generatorBoyName2());
        } else {
            return FIRST_NAME.next() + (oneChar ? generatorGirlName1() : generatorGirlName2());
        }
    }

}

package com.orion.generator.industry;

import com.orion.lang.define.collect.WeightRandomMap;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Strings;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 行业生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/19 16:21
 */
public class IndustryGenerator {

    private static final Map<String, String[]> INDUSTRY;

    private static final WeightRandomMap<String> INDUSTRY_WEIGHT;

    private static final String STUDENT;

    private IndustryGenerator() {
    }

    static {
        STUDENT = "学生";
        Map<String, String[]> tempIndustry = new HashMap<String, String[]>() {{
            put("计算机/互联网/通信", new String[]{
                    "信息科技", "网络科技", "智能科技",
                    "电子科技", "环保科技", "机电科技",
                    "光电科技", "通讯科技", "化工科技",
                    "自动化技术", "信息技术", "大数据科技",
                    "云计算科技", "软件"
            });

            put("生产/工艺/制造", new String[]{
                    "园艺", "园林景观设计", "暖通工程",
                    "船舶技术", "建材", "电器",
                    "家具", "门窗", "化工",
                    "化工生产", "图书", "刀具生产",
                    "阀门生产", "石材生产", "布艺生产",
                    "木材生产", "涂料生产", "管业",
                    "箱包", "防水工程", "食品生产",
                    "墙体材料", "办公设备", "自控仪器",
                    "玻璃制品", "塑料制品", "日用百货",
                    "电子仪器", "家居用品", "包装材料",
                    "工艺礼品", "汽摩配件", "化妆品",
                    "金属材料", "制冷设备", "电子设备",
                    "机电设备", "环保设备", "自动化设备",
                    "制冷设备", "润滑油", "仪器仪",
                    "零配件制造", "鞋业", "服装"
            });

            put("医疗/护理/制药", new String[]{
                    "生物医药", "生物工程", "医药",
                    "制药", "医疗", "卫生生物",
                    "医疗护理", "医疗器械", "医疗用品",
                    "医疗器材"
            });

            put("金融/银行/投资/保险", new String[]{
                    "金融", "金融服务", "金融信息",
                    "投资", "保险"
            });

            put("商业/服务业/个体经营", new String[]{
                    "商务咨询", "投资咨询", "投资管理",
                    "劳务派遣", "汽车租赁", "工程造价咨询",
                    "货物运输代理", "建筑装潢设计", "建筑装饰工程",
                    "礼仪服务", "婚庆服务", "会务服务", "快递服务",
                    "翻译服务", "贸易", "商贸", "实业",
                    "产品设计", "企业管理咨询", "图文设计",
                    "膳食餐饮", "对外贸易", "房地产经纪",
                    "物流"
            });

            put("文化/广告/传媒", new String[]{
                    "广告传媒", "文化传播", "传媒",
                    "广告设计", "市场策划", "企业形象策划",
                    "市场营销策划", "国际文化", "国际传媒",
                    "文化传媒"
            });

            put("娱乐/艺术/表演", new String[]{
                    "数码动画", "文化娱乐", "艺术",
                    "影视", "娱乐"
            });

            put("律师/法务", new String[]{
                    "律师事务", "法律咨询服务"
            });

            put("培训/教育", new String[]{
                    "教育培训", "教育科技", "舞蹈培训",
                    "艺术培训", "英语培训", "体育培训",
                    "少儿培训", "少儿教育", "音乐培训"
            });

            put("公务员/行政/事业单位", new String[]{
                    "水务管理", "电力管理", "土地管理", "财政管理",
                    "教育管理", "科研管理", "卫生管理", "体育", "新闻出版"
            });

            put("模特", new String[]{
                    "模特文化", "国际文化", "国际传媒",
                    "国际模特"
            });

            put("其他", new String[]{
                    "资本管理", "资产"
            });
        }};
        INDUSTRY = Collections.unmodifiableMap(tempIndustry);
    }

    static {
        INDUSTRY_WEIGHT = new WeightRandomMap<>();
        INDUSTRY.forEach((k, v) -> {
            INDUSTRY_WEIGHT.put(k, v.length);
        });
    }

    public static String generatorIndustry() {
        return Arrays1.random(INDUSTRY_WEIGHT.next().split("/"));
    }

    /**
     * 随机生成一个行业
     *
     * @param age age
     * @return 行业
     */
    public static String generatorIndustry(int age) {
        if (age <= 18) {
            return STUDENT;
        }
        return generatorIndustry();
    }

    public static String generatorManagementType() {
        return Arrays1.random(INDUSTRY.get(INDUSTRY_WEIGHT.next()));
    }

    /**
     * 随机生成一个经营类型
     *
     * @param age age
     * @return 经营类型
     */
    public static String generatorManagementType(int age) {
        if (age <= 18) {
            return Strings.EMPTY;
        }
        return Arrays1.random(INDUSTRY.get(INDUSTRY_WEIGHT.next()));
    }

    /**
     * 随机生成一个经营类型
     *
     * @param industry 行业
     * @return 经营类型
     */
    public static String generatorManagementType(String industry) {
        String key = null;
        for (String i : INDUSTRY.keySet()) {
            for (String detail : i.split("/")) {
                if (detail.equals(industry)) {
                    key = i;
                    break;
                }
            }
        }
        if (key == null) {
            return Strings.EMPTY;
        }
        String[] arr = INDUSTRY.get(key);
        if (arr == null) {
            return Strings.EMPTY;
        }
        return Arrays1.random(arr);
    }

}

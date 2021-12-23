package com.orion.generator.addres;

/**
 * 详细地址
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/12 18:06
 */
public enum AddressType {

    /**
     * 小区
     */
    COMMUNITY(new String[]{"小区", "苑", "阁", "园", "轩", "府", "庄", "院", "岛", "城", "湾", "庭", "馆",
            "公馆", "东苑", "西苑", "南苑", "北苑", "东区", "西区", "南区", "北区"}),

    /**
     * 村
     */
    VILLAGE(new String[]{"村", "庒", "堡", "区", "新村", "镇"}),

    /**
     * 街道
     */
    STREET(new String[]{"街道", "巷", "街", "路", "桥"}),

    ;

    AddressType(String[] suffix) {
        this.suffix = suffix;
    }

    private final String[] suffix;

    public String[] getSuffix() {
        return suffix;
    }

}

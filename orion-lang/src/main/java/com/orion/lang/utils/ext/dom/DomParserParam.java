package com.orion.lang.utils.ext.dom;

/**
 * XML 解析参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/24 10:02
 */
class DomParserParam {

    /**
     * dom 名称
     */
    private String name;

    /**
     * dom 下标
     */
    private int index;

    /**
     * dom 属性
     */
    private String key;

    /**
     * dom 属性值
     */
    private String value;

    DomParserParam() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

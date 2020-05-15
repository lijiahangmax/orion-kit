package com.orion.dom;

/**
 * XML解析参数
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/24 10:02
 */
class DomParserParam {

    /**
     * dom名称
     */
    private String name;

    /**
     * dom下标
     */
    private int index;

    /**
     * dom属性
     */
    private String key;

    /**
     * dom属性值
     */
    private String value;

    DomParserParam() {
    }

    public String getName() {
        return name;
    }

    public DomParserParam setName(String name) {
        this.name = name;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public DomParserParam setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DomParserParam setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DomParserParam setValue(String value) {
        this.value = value;
        return this;
    }

}

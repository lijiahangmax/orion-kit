package com.orion.utils.script;

/**
 * 脚本类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/1 23:55
 */
public enum ScriptType {

    /**
     * java script
     */
    JAVA_SCRIPT("js"),

    /**
     * lua
     */
    LUA("lua"),

    /**
     * groovy
     */
    GROOVY("groovy"),

    /**
     * python
     * 需要设置 python.import.site 为 false
     */
    PYTHON("python");

    private final String type;

    ScriptType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

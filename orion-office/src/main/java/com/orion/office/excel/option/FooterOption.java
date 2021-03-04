package com.orion.office.excel.option;

import java.io.Serializable;

/**
 * Excel 页脚配置
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/25 17:28
 */
public class FooterOption implements Serializable {

    /**
     * 页脚左侧
     */
    private String left;

    /**
     * 页脚中间
     */
    private String center;

    /**
     * 页脚右侧
     */
    private String right;

    public FooterOption() {
    }

    public FooterOption(String left, String center, String right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public FooterOption setLeft(String left) {
        this.left = left;
        return this;
    }

    public String getCenter() {
        return center;
    }

    public FooterOption setCenter(String center) {
        this.center = center;
        return this;
    }

    public String getRight() {
        return right;
    }

    public FooterOption setRight(String right) {
        this.right = right;
        return this;
    }

}

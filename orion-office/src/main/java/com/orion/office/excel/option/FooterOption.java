package com.orion.office.excel.option;

import java.io.Serializable;

/**
 * excel 页脚配置
 *
 * @author Jiahang Li
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

    public void setLeft(String left) {
        this.left = left;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

}

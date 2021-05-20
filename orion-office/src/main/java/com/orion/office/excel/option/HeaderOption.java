package com.orion.office.excel.option;

import java.io.Serializable;

/**
 * Excel 页眉配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/25 17:27
 */
public class HeaderOption implements Serializable {

    /**
     * 页眉左侧
     */
    private String left;

    /**
     * 页眉中间
     */
    private String center;

    /**
     * 页眉右侧
     */
    private String right;

    public HeaderOption() {
    }

    public HeaderOption(String left, String center, String right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public HeaderOption setLeft(String left) {
        this.left = left;
        return this;
    }

    public String getCenter() {
        return center;
    }

    public HeaderOption setCenter(String center) {
        this.center = center;
        return this;
    }

    public String getRight() {
        return right;
    }

    public HeaderOption setRight(String right) {
        this.right = right;
        return this;
    }

}

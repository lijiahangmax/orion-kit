package com.orion.excel.option;

import java.io.Serializable;

/**
 * Excel 页眉配置
 *
 * @author ljh15
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

package com.orion.excel.option;

import java.io.Serializable;

/**
 * Excel 批注参数
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/12/25 15:59
 */
public class CommentOption implements Serializable {

    /**
     * 作者
     */
    private String author;

    /**
     * 批注
     */
    private String comment;

    /**
     * 是否可见
     */
    private boolean visible;

    public CommentOption() {
    }

    public CommentOption(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}

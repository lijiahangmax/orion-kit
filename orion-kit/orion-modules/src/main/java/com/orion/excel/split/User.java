package com.orion.excel.split;

import com.orion.excel.annotation.ExportField;
import com.orion.excel.annotation.ExportSheet;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/20 11:05
 */
@ExportSheet(value = "用户信息", rowWidth = 50, rowHeight = 30, headerHeight = 20)
public class User {

    @ExportField(value = 0, header = "用户id", align = 0)
    private Long userId;

    @ExportField(value = 1, header = "用户名")
    private String userName;

    @ExportField(value = 2, align = 0)
    private String password;

    public Long getUserId() {
        return userId;
    }

    public User setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}

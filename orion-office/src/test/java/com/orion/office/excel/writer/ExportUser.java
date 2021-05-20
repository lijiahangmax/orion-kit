package com.orion.office.excel.writer;

import com.orion.office.excel.annotation.*;
import com.orion.office.excel.type.*;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/21 17:51
 */
@ExportSheet(name = "用户信息", columnWidth = 30, headerHeight = 50, rowHeight = 30, zoom = 120,
        headerUseColumnStyle = true, skipFieldHeader = false, skipComment = false, skipSelectOption = false,
        filterHeader = true, freezeHeader = true, selected = true, hidden = false)
@ExportHeader(left = "页眉左侧", center = "页眉中间", right = "页眉右侧")
@ExportFooter(left = "页脚左侧", center = "页脚中间", right = "页脚右侧")
public class ExportUser {

    @ExportField(index = 0,
            align = ExcelAlignType.CENTER,
            border = ExcelBorderType.MEDIUM,
            verticalAlign = ExcelVerticalAlignType.CENTER,
            header = "用户id",
            width = 20,
            wrapText = false,
            lock = true,
            type = ExcelFieldType.NUMBER,
            borderColor = "#2c2c96",
            backgroundColor = "#a13a3a")
    @ExportFont(fontName = "Candara", fontSize = 19, under = ExcelUnderType.DOUBLE_ACCOUNTING, delete = true, color = "#4514a1")
    private Long id;

    @ExportField(index = 1,
            align = ExcelAlignType.LEFT,
            verticalAlign = ExcelVerticalAlignType.TOP,
            header = "用户名称",
            width = 25,
            wrapText = true,
            indent = 5,
            type = ExcelFieldType.TEXT,
            borderColor = "#2c965e")
    private String name;

    @ExportField(index = 2,
            align = ExcelAlignType.RIGHT,
            border = ExcelBorderType.THICK,
            verticalAlign = ExcelVerticalAlignType.BOTTOM,
            header = "注册时间",
            type = ExcelFieldType.DATE_FORMAT,
            format = "E HH:mm:ss",
            skipHeaderStyle = true,
            borderColor = "#2c2c96",
            backgroundColor = "#223d20")
    @ExportFont(fontName = "微软雅黑", fontSize = 22, bold = true, italic = true, color = "#6e1842")
    private Date date;

    private String compute;

    @ExportField(index = 4, type = ExcelFieldType.AUTO, width = 10, header = "年龄", backgroundColor = "#168a63")
    @ExportFont(fontName = "Cascadia Code SemiLight", color = "#112921")
    // @ExportIgnore
    private Integer age;

    @ExportField(index = 5, width = 15, header = "博客地址", selectOptions = {"opt1", "opt2"})
    @ExportComment(comment = "点击博客可以直接跳转", author = "ljh")
    @ExportLink(type = ExcelLinkType.LINK_URL, text = "$realName")
    private String blogUrl;

    @ExportField(index = 6, width = 15, header = "邮箱地址")
    @ExportLink(type = ExcelLinkType.LINK_EMAIL, text = "!邮箱")
    @ExportFont(bold = true)
    private String emailUrl;

    @ExportField(index = 7, width = 10, header = "简历文件")
    @ExportLink(type = ExcelLinkType.LINK_FILE, text = "!简历")
    private String file;

    @ExportField(index = 8, width = 20, header = "超链接不赋值1")
    @ExportLink(type = ExcelLinkType.LINK_DOC, text = "!不赋值", address = "!'用户信息'!A22")
    private String url1;

    @ExportField(index = 9, width = 20, header = "超链接不赋值2")
    @ExportLink(type = ExcelLinkType.LINK_FILE, text = "$id", address = "$blogUrl")
    private String url2;

    private String realName;

    public Long getId() {
        return id;
    }

    public ExportUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExportUser setName(String name) {
        this.name = name;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public ExportUser setDate(Date date) {
        this.date = date;
        return this;
    }

    @ExportField(index = 3, type = ExcelFieldType.FORMULA, hidden = true)
    public String getCompute() {
        return compute;
    }

    public ExportUser setCompute(String compute) {
        this.compute = compute;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getEmailUrl() {
        return emailUrl;
    }

    public void setEmailUrl(String emailUrl) {
        this.emailUrl = emailUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }
}

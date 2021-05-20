package com.orion.office.excel.option;

import java.io.Serializable;
import java.util.Date;

/**
 * Excel 属性参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/22 18:59
 */
public class PropertiesOption implements Serializable {

    /**
     * 作者
     */
    private String author;

    /**
     * 标题
     */
    private String title;

    /**
     * 主体
     */
    private String subject;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 修订
     */
    private String revision;

    /**
     * 描述
     */
    private String description;

    /**
     * 分类
     */
    private String category;

    /**
     * 公司
     */
    private String company;

    /**
     * 经理
     */
    private String manager;

    /**
     * 应用
     */
    private String application;

    /**
     * 修订人
     */
    private String modifiedUser;

    /**
     * 内容状态
     */
    private String contentStatus;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date modified;

    public String getAuthor() {
        return author;
    }

    public PropertiesOption setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PropertiesOption setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public PropertiesOption setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getKeywords() {
        return keywords;
    }

    public PropertiesOption setKeywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public String getRevision() {
        return revision;
    }

    public PropertiesOption setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PropertiesOption setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public PropertiesOption setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public PropertiesOption setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getManager() {
        return manager;
    }

    public PropertiesOption setManager(String manager) {
        this.manager = manager;
        return this;
    }

    public String getApplication() {
        return application;
    }

    public PropertiesOption setApplication(String application) {
        this.application = application;
        return this;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public PropertiesOption setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
        return this;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public PropertiesOption setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public PropertiesOption setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public PropertiesOption setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public PropertiesOption setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getModified() {
        return modified;
    }

    public PropertiesOption setModified(Date modified) {
        this.modified = modified;
        return this;
    }

}

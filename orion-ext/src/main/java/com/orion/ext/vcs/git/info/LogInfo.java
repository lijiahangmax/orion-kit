package com.orion.ext.vcs.git.info;

import com.orion.utils.time.Dates;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/2 11:36
 */
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 12956908923748L;

    /**
     * 提交id
     */
    private String id;

    /**
     * email
     */
    private String email;

    /**
     * 提交名称
     */
    private String name;

    /**
     * 提交日期
     */
    private Date time;

    /**
     * 消息
     */
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setTime(int time) {
        this.time = Dates.date(time);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", message='" + message + '\'' +
                '}';
    }

}

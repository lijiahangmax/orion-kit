package com.orion.test.reflect.value;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/26 11:58
 */
public class BaseTypeUser {

    public int id;

    public long time;

    public boolean sex;

    public char name;

    @Override
    public String toString() {
        return "BaseTypeUser{" +
                "id=" + id +
                ", time=" + time +
                ", sex=" + sex +
                ", name=" + name +
                '}';
    }

}

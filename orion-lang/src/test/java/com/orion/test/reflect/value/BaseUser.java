package com.orion.test.reflect.value;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/19 17:49
 */
public class BaseUser {

    private Map<Object, Object> map;

    private int type;

    private Integer vipType;

    private BigDecimal balance;

    public int getType() {
        return type;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getVipType() {
        return vipType;
    }

    public void setVipType(Integer vipType) {
        this.vipType = vipType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "map=" + map +
                ", type=" + type +
                ", vipType=" + vipType +
                ", balance=" + balance +
                '}';
    }

}

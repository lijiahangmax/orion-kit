package com.orion.test.reflect.value;

import java.io.Serializable;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/19 17:40
 */
public class Shop implements Serializable {

    private String shopName;

    private Long shopId;

    private Long userId;

    private Boolean status;

    public Shop() {
    }

    public Shop(String shopName, Long shopId) {
        this.shopName = shopName;
        this.shopId = shopId;
    }

    public Shop(Long shopId) {
        this.shopId = shopId;
    }

    public Shop(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopName='" + shopName + '\'' +
                ", shopId=" + shopId +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }

}

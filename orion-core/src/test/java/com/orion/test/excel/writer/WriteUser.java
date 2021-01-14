package com.orion.test.excel.writer;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/14 9:53
 */
public class WriteUser {

    private Long id;

    private String name;

    private String formula;

    private Date date;

    private BigDecimal balance;

    private String disable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

}

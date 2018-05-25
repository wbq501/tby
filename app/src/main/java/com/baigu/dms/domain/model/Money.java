package com.baigu.dms.domain.model;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class Money {
    private double amount;//总金额
    private double frozenAmount;//冻结金额
    private int status;//0未冻结1冻结

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

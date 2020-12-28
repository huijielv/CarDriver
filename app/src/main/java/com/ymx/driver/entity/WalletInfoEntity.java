package com.ymx.driver.entity;

public class WalletInfoEntity extends BaseEntity {
    private String balacne;
    private String monthIncome;
    private String weekIncome;
    private String withdrawDesc;
    private String monthPayOrderNumber ;



    private String monthPayOrderNumberDesc;

    public String getMonthPayOrderNumber() {
        return monthPayOrderNumber;
    }

    public void setMonthPayOrderNumber(String monthPayOrderNumber) {
        this.monthPayOrderNumber = monthPayOrderNumber;
    }

    public String getBalacne() {
        return balacne;
    }

    public void setBalacne(String balacne) {
        this.balacne = balacne;
    }

    public String getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(String monthIncome) {
        this.monthIncome = monthIncome;
    }

    public String getWeekIncome() {
        return weekIncome;
    }

    public void setWeekIncome(String weekIncome) {
        this.weekIncome = weekIncome;
    }

    public String getWithdrawDesc() {
        return withdrawDesc;
    }

    public void setWithdrawDesc(String withdrawDesc) {
        this.withdrawDesc = withdrawDesc;
    }
    public String getMonthPayOrderNumberDesc() {
        return monthPayOrderNumberDesc;
    }

    public void setMonthPayOrderNumberDesc(String monthPayOrderNumberDesc) {
        this.monthPayOrderNumberDesc = monthPayOrderNumberDesc;
    }
}

package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

import java.util.List;

public class WithdrawDetailEntity extends BaseEntity {
    private String money;
    private String bankName;
    private String bankCard;
    private List<WithdrawDetailListItemEntity> list ;

    public List<WithdrawDetailListItemEntity> getList() {
        return list;
    }

    public void setList(List<WithdrawDetailListItemEntity> list) {
        this.list = list;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }


}

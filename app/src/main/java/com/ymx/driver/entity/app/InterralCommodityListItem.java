package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class InterralCommodityListItem extends BaseEntity {

    /**
     * commodityId : 1
     * commodityUrl : http://www.yimaxiankeji.com/upload/20200917/0dc7de90.jpg
     * commodityName : 保温杯
     * exchangeIntegral : 200
     * commodityDesc : 商品描述
     */

    private int commodityId;
    private String commodityUrl;
    private String commodityName;
    private String exchangeIntegral;
    private String commodityDesc;
    private int stocksNumber;



    public String getExchangeIntegral() {
        return exchangeIntegral;
    }

    public void setExchangeIntegral(String exchangeIntegral) {
        this.exchangeIntegral = exchangeIntegral;
    }



    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityUrl() {
        return commodityUrl;
    }

    public void setCommodityUrl(String commodityUrl) {
        this.commodityUrl = commodityUrl;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }



    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public int getStocksNumber() {
        return stocksNumber;
    }

    public void setStocksNumber(int stocksNumber) {
        this.stocksNumber = stocksNumber;
    }
}

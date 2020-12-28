package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

public class OperationLineListItem extends BaseEntity {
    private  String startSite;
    private String  endSite;

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

    private  int stationType ;

    public String getStartSite() {
        return startSite;
    }

    public void setStartSite(String startSite) {
        this.startSite = startSite;
    }

    public String getEndSite() {
        return endSite;
    }

    public void setEndSite(String endSite) {
        this.endSite = endSite;
    }
}

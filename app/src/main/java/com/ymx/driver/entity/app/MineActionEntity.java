package com.ymx.driver.entity.app;


import com.ymx.driver.entity.BaseEntity;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public class MineActionEntity extends BaseEntity {
    private int iconId;
    private int titleId;
    private Class<?> cls;

    public MineActionEntity(int iconId, int titleId, Class<?> cls) {
        this.iconId = iconId;
        this.titleId = titleId;
        this.cls = cls;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}

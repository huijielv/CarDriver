package com.ymx.driver.entity.app;

import com.ymx.driver.entity.BaseEntity;

/**
 * Created by wuwei
 * 2020/6/5
 * 佛祖保佑       永无BUG
 */
public class UpdateEntity extends BaseEntity {


    /**
     * versionCode : 1
     * versionName : 发现新版本 v1.1.1
     * versionDesc : 1.修复Bug
     * fileUrl : http://oss.diyue123.com/upload/apk/20200528/c6c2e05012d247508571207a37fc27c2.apk
     * upgradeMode : 0
     * fileMd5 : WSEOIFEWOFJ1290392023
     */

    private int versionCode;
    private String versionName;
    private String versionDesc;
    private String fileUrl;
    private int upgradeMode;
    private String fileMd5;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getUpgradeMode() {
        return upgradeMode;
    }

    public void setUpgradeMode(int upgradeMode) {
        this.upgradeMode = upgradeMode;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}

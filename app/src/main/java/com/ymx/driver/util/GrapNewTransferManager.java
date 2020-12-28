package com.ymx.driver.util;

import android.app.Activity;

import com.ymx.driver.base.AppManager;
import com.ymx.driver.dialog.GrapNewTransferDialog;
import com.ymx.driver.entity.app.TransferNewOrderEntity;

import java.lang.ref.WeakReference;

public class GrapNewTransferManager {
    private WeakReference<Activity> currentActivityWeakRef;
    public GrapNewTransferDialog grapNewTransferDialog;
    private static GrapNewTransferManager instance;

    public TransferNewOrderEntity getTransferNewOrderEntity() {
        return transferNewOrderEntity;
    }

    public void setTransferNewOrderEntity(TransferNewOrderEntity transferNewOrderEntity) {
        this.transferNewOrderEntity = transferNewOrderEntity;
    }

    private TransferNewOrderEntity transferNewOrderEntity;

    public static GrapNewTransferManager getInstance() {
        if (instance == null) {
            synchronized (GrapNewTransferManager.class) {
                if (instance == null) {
                    instance = new GrapNewTransferManager();
                }
            }
        }
        return instance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (currentActivityWeakRef != null) {
            currentActivity = currentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivityWeakRef = new WeakReference(activity);

    }

    public GrapNewTransferManager() {

    }

    public void showDialog() {
        if (grapNewTransferDialog != null && grapNewTransferDialog.isShowing()) {
            if (grapNewTransferDialog.getContext() == AppManager.getAppManager().currentActivity()) {

                return;
            }
        }

        if (getCurrentActivity() != null) {
            grapNewTransferDialog = new GrapNewTransferDialog(getCurrentActivity(), transferNewOrderEntity);
            grapNewTransferDialog.show();
        }

    }

    public boolean isShow() {
        if (grapNewTransferDialog != null && grapNewTransferDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        if (grapNewTransferDialog != null && grapNewTransferDialog.isShowing()) {
            grapNewTransferDialog.dismiss();
            grapNewTransferDialog = null;
        }
    }



}


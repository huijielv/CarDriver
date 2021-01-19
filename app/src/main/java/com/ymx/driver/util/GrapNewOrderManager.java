package com.ymx.driver.util;
import android.app.Activity;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.entity.BaseGrabOrderEntity;
import com.ymx.driver.ui.main.Dialog.GrabNewOrderDialog;


import java.lang.ref.WeakReference;

public class GrapNewOrderManager {
    private WeakReference<Activity> currentActivityWeakRef;
    public GrabNewOrderDialog grabNewOrderDialog;
    private static GrapNewOrderManager instance;

    public BaseGrabOrderEntity getBaseGrabOrderEntity() {
        return baseGrabOrderEntity;
    }

    public void setBaseGrabOrderEntity(BaseGrabOrderEntity baseGrabOrderEntity) {
        this.baseGrabOrderEntity = baseGrabOrderEntity;
    }

    private BaseGrabOrderEntity baseGrabOrderEntity;



    public static GrapNewOrderManager getInstance() {
        if (instance == null) {
            synchronized (GrapNewOrderManager.class) {
                if (instance == null) {
                    instance = new GrapNewOrderManager();
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

    public GrapNewOrderManager() {

    }

    public void showDialog() {
        if (grabNewOrderDialog != null && grabNewOrderDialog.isShowing()) {
            if (grabNewOrderDialog.getContext() == AppManager.getAppManager().currentActivity()) {

                return;
            }
        }

        if (getCurrentActivity() != null) {
            grabNewOrderDialog = new GrabNewOrderDialog(getCurrentActivity(), baseGrabOrderEntity);
            grabNewOrderDialog.show();
        }

    }

    public boolean isShow() {
        if (grabNewOrderDialog != null && grabNewOrderDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        if (grabNewOrderDialog!= null && grabNewOrderDialog.isShowing()) {
            grabNewOrderDialog.dismiss();
            grabNewOrderDialog= null;
        }
    }



}


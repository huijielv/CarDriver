package com.ymx.driver.ui.main.Dialog.interfaces;

import android.content.Intent;

import com.ymx.driver.base.YmxApp;
import com.ymx.driver.entity.app.CarpoolGrabOrderEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.travel.activity.CarPoolDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.util.UIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CarPoolGrabOrder implements GrabOrderInterface {
    @Override
    public void GrapOrder(String orderNo, GrabResult grabResult) {
        RetrofitFactory.sApiService.carpoolGrabOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CarpoolGrabOrderEntity>() {
                    @Override
                    protected void onRequestStart() {
                        grabResult.onRequestStart();
                    }

                    @Override
                    protected void onRequestEnd() {
                        grabResult.onRequestEnd();
                    }

                    @Override
                    protected void onSuccees(CarpoolGrabOrderEntity carpoolGrabOrderEntity) {

                        grabResult.onSuccees(carpoolGrabOrderEntity);
                        grabResult.onSuccees();

                        if (carpoolGrabOrderEntity.getCategoryType() == 2) {
                            Intent intent = new Intent();
                            intent.putExtra(CarPoolDetailsActivity.ORDERI_ID, carpoolGrabOrderEntity.getOrderNodeNo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(YmxApp.getInstance(), CarPoolDetailsActivity.class);
                            YmxApp.getInstance().startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(TravelActivity.ORDERI_ID, carpoolGrabOrderEntity.getOrderNodeNo());
                            intent.putExtra(TravelActivity.CATEGORY_TYPE, String.valueOf(carpoolGrabOrderEntity.getCategoryType()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(YmxApp.getInstance(), TravelActivity.class);
                            YmxApp.getInstance().startActivity(intent);
                        }

                    }

                    @Override
                    protected void onFailure(String message) {
                        grabResult.onFailure(message);


                    }
                });
    }

    @Override
    public void GrapOrder(String orderNo) {

    }
}

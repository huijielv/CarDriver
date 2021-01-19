package com.ymx.driver.ui.main.Dialog.interfaces;

import android.content.Intent;
import android.text.TextUtils;

import com.ymx.driver.base.YmxApp;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderListActivity;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransferGrabOrder implements GrabOrderInterface {
    @Override
    public void GrapOrder(String orderNo, GrabResult grabResult) {
        RetrofitFactory.sApiService.transferStationGrabOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationGrabOrder>() {
                    @Override
                    protected void onRequestStart() {
                        grabResult.onRequestStart();
                    }

                    @Override
                    protected void onRequestEnd() {
                        grabResult.onRequestEnd();
                    }

                    @Override
                    protected void onSuccees(TransferStationGrabOrder transferStationGrabOrder) {
                        grabResult.onSuccees(transferStationGrabOrder);
                        grabResult.onSuccees();
                        switch (transferStationGrabOrder.getIsChooseTrip()) {

                            case 0:

                                if (!TextUtils.isEmpty(transferStationGrabOrder.getOrderNodeNo())) {
                                    Intent intent = new Intent();
                                    intent.putExtra(TransferStationTripOrderListActivity.ORDER_NO, transferStationGrabOrder.getOrderNodeNo());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setClass(YmxApp.getInstance(), TransferStationTripOrderDetailsActivity.class);
                                    YmxApp.getInstance().startActivity(intent);


                                }


                                break;
                            case 1:
                                // 跳转行程界面

                                Intent goTo = new Intent();
                                goTo.putExtra(TransferStationTripOrderListActivity.ORDER_NO, transferStationGrabOrder.getOrderNo());
                                goTo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                goTo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                goTo.setClass(YmxApp.getInstance(), TransferStationTripOrderListActivity.class);
                                YmxApp.getInstance().startActivity(goTo);


                                break;
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

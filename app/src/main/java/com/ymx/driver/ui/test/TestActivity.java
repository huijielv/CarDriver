package com.ymx.driver.ui.test;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.BaseViewModel;


import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.databinding.TestActivityBinding;
import com.ymx.driver.dialog.UpdateCarStateDialog;
import com.ymx.driver.entity.BaseGrabOrderEntity;


import com.ymx.driver.entity.app.GrabNewOrderEntity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.NewOrderTTSController;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class TestActivity extends BaseActivity<TestActivityBinding, BaseViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TestActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.test_activity;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    int i = 1;

    @Override
    public void initView(Bundle savedInstanceState) {


        //创建观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> s) throws Exception {
                s.onNext("a");
                s.onNext("b");
                s.onNext("c");
                s.onNext("d");
            }
        });
        //创建被观察者
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String info) {
                LogUtil.d("Rxjava--Test-----------", info);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //绑定观察者和被观察者
        observable.subscribe(observer);


        binding.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BaseGrabOrderEntity baseGraoOrderEntity  = new BaseGrabOrderEntity();

//                UpdateCarStateDialog updateCarStateDialog = new UpdateCarStateDialog(TestActivity.this);
//                updateCarStateDialog.show();

//                new DefaultStyleDialog(activity)
//                        .setBody("您确定要锁定吗？锁定后系统将不再为您匹配新的乘客")
//                        .setTitle("行程安全提示")
//                        .setNegativeText("取消")
//                        .setPositiveText("确定锁定")
//                        .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
//                            @Override
//                            public void negative(Dialog dialog) {
//                                dialog.dismiss();
//
//                            }
//
//                            @Override
//                            public void positive(Dialog dialog) {
//                                dialog.dismiss();
//
//                            }
//                        }).show();

                GrabNewOrderEntity grabNewOrderEntity = new GrabNewOrderEntity();
                grabNewOrderEntity.setTips("南山区");
                grabNewOrderEntity.setCarPoolDescription("拼车(1 人)");
                grabNewOrderEntity.setPriceDescription("价格12.24元");
                grabNewOrderEntity.setMarkupPriceDescription("加价10元");
                BaseGrabOrderEntity baseGrabOrderEntity = new BaseGrabOrderEntity.
                        Builder().
                        setTtsMsg(grabNewOrderEntity.getTips()).
                        setNewOrder(grabNewOrderEntity).
                        setOrderType(2).
                        build();
                NewOrderTTSController.getInstance(YmxApp.getInstance()).onGetText(baseGrabOrderEntity);
            }
        });


    }

    @Override
    public void initData() {


    }


    @Override
    public void initViewObservable() {

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

}

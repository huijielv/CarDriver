package com.ymx.driver.ui.chartercar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;

import com.ymx.driver.databinding.ActivityCharterDrivingBinding;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.chartercar.CharterDrivingViewModel;


public class CharterDrivingActivity extends BaseActivity<ActivityCharterDrivingBinding, CharterDrivingViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, CharterDrivingActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_charter_driving;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnSubmit.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                if (viewModel.remoteState != null && viewModel.remoteState.get() != null && viewModel.remoteState.get() == 0) {
                    viewModel.updateFerryRemoteInfo(1);
                }
            }
        });


    }

    @Override
    public void initData() {
        viewModel.getCharteredInfo();


    }


    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}

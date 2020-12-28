package com.ymx.driver.ui.longrange.driving;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.Observer;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.databinding.ActivityLongRangeFerryDrvingBinding;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.UpdateFerryRemoteInfoEntity;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.longrangdriving.LongRangeFerryDrvingViewModel;

public class LongRangeFerryDrvingDetails extends BaseActivity<ActivityLongRangeFerryDrvingBinding, LongRangeFerryDrvingViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, LongRangeFerryDrvingDetails.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_long_range_ferry_drving;
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
                    viewModel.updateFerryRemoteInfo(1, -1);
                }
            }
        });

    }

    @Override
    public void initData() {
        viewModel.getRemoteInfo();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucLockDrvier.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.lockDrvierState != null && viewModel.lockDrvierState.get() == 1) {
                    viewModel.updateFerryRemoteInfo(-1, 0);
                } else if (viewModel.lockDrvierState != null && viewModel.lockDrvierState.get() == 0) {
                    new DefaultStyleDialog(activity)
                            .setBody("您确定要锁定吗？锁定后系统将不再为您匹配新的乘客")
                            .setNegativeText("取消")
                            .setPositiveText("确定锁定")
                            .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                @Override
                                public void negative(Dialog dialog) {
                                    dialog.dismiss();

                                }

                                @Override
                                public void positive(Dialog dialog) {
                                    dialog.dismiss();
                                    viewModel.updateFerryRemoteInfo(-1, 1);
                                }
                            }).show();
                }

            }
        });


        viewModel.uc.ucStopWork.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.remoteState != null && viewModel.remoteState.get() != 0) {
                    viewModel.updateFerryRemoteInfo(0, -1);
                }

            }
        });

        viewModel.uc.ucInitData.observe(this, new Observer<RemoteInfoEntity>() {
            @Override
            public void onChanged(RemoteInfoEntity remoteInfoEntity) {
                if (remoteInfoEntity.getRemoteState() == 0) {
                    binding.btnSubmit.setText("滑动出车");
                }
            }
        });
        viewModel.uc.ucInit.observe(this, new Observer<UpdateFerryRemoteInfoEntity>() {
            @Override
            public void onChanged(UpdateFerryRemoteInfoEntity remoteInfoEntity) {
                if (remoteInfoEntity.getRemoteState() == 0) {
                    binding.btnSubmit.setText("滑动出车");
                }
            }
        });

    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

}


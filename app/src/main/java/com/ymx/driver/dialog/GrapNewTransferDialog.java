package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.databinding.DialogGrabTransferNewOrderBinding;
import com.ymx.driver.entity.app.TransferNewOrderEntity;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderListActivity;
import com.ymx.driver.util.UIUtils;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GrapNewTransferDialog extends Dialog {
    private DialogGrabTransferNewOrderBinding binding;
    private TransferNewOrderEntity transferNewOrderEntity;
    private static Timer timer;
    private static CountdownTask countdownTask;
    private int mTime = 10;
    private long lastClickTime;
    private Context context;

    public GrapNewTransferDialog(@NonNull Context context, TransferNewOrderEntity transferNewOrderEntity) {
        super(context);
        this.transferNewOrderEntity = transferNewOrderEntity;
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.getDecorView().setBackgroundColor(UIUtils.getColor(R.color.rx_transparent));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.0f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_grab_transfer_new_order, null, false);
        setContentView(binding.getRoot());

        if (transferNewOrderEntity != null) {
            binding.address.setText(transferNewOrderEntity.getTips());

        }
        List<String> noShowDialogDriverList = transferNewOrderEntity.getNoShowDialogDriverList();
        boolean isVisity = false;
        UserEntity userEntity = LoginHelper.getUserEntity();


        if (noShowDialogDriverList != null && noShowDialogDriverList.size() > 0) {
            for (String listitem : noShowDialogDriverList) {
                if (listitem.equals(userEntity.getIdNo())) {
                    isVisity = true;
                    break;
                }
            }
        }


        if (isVisity) {
            binding.grabTv.setVisibility(View.GONE);
        } else {
            binding.grabTv.setVisibility(View.VISIBLE);
            binding.grabTv.setBackground(UIUtils.getDrawable(R.drawable.bg_grey));
            binding.grabTv.setText("接单 (" + mTime + ")");

        }
        timer = new Timer();
        countdownTimer();

        binding.grabTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    UIUtils.showToast("操作太频繁了");
                    return;
                }


                if (!TextUtils.isEmpty(transferNewOrderEntity.getOrderNo())) {
                    ransferStationGrabOrder(transferNewOrderEntity.getOrderNo());
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        cancal();
        UIUtils.removeTask(runnable);
        context = null;

    }


    public void cancal() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;

        }
        if (timer != null) {
            timer.cancel();
            timer = null;

        }


    }

    public class CountdownTask extends TimerTask {
        int time;

        public CountdownTask(int time) {
            this.time = time;
        }

        @Override
        public void run() {

//            UIUtils.postTask(runnable );

            UIUtils.postTask(runnable);
        }
    }

    public void countdownTimer() {
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        countdownTask = new CountdownTask(mTime);
        timer.schedule(countdownTask, 1000, 1000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           try {
               mTime--;

               if (mTime < 0) {
                   cancal();
                   if (context != null) {
                       dismiss();
                   }

               } else if (mTime <= 5) {
                   if (binding.grabTv != null) {
                       binding.grabTv.setEnabled(true);
                       binding.grabTv.setBackground(UIUtils.getDrawable(R.drawable.bg_soli_item));
                       binding.grabTv.setText("接单");
                   }
               } else {
                   if (binding.grabTv != null) {
                       binding.grabTv.setEnabled(false);
                       binding.grabTv.setBackground(UIUtils.getDrawable(R.drawable.bg_grey));
                       binding.grabTv.setText("接单 (" + mTime + ")");
                   }
               }
           }catch (Exception e){
               e.printStackTrace();
           }
        }
    };


    public void ransferStationGrabOrder(String orderNo) {
        RetrofitFactory.sApiService.transferStationGrabOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<TransferStationGrabOrder>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(TransferStationGrabOrder transferStationGrabOrder) {
                        if (mTime >= 0) {
                            dismiss();
                        }
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
                        UIUtils.showToast(message);
                        if (binding != null && binding.grabTv != null) {
                            binding.grabTv.setEnabled(true);

                        }
                    }
                });
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {       //1000毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

}

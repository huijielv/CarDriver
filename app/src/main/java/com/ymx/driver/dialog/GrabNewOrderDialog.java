package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.databinding.DialogGrabNewOrderBinding;
import com.ymx.driver.entity.BaseGrabOrderEntity;
import com.ymx.driver.entity.app.CarpoolGrabOrderEntity;
import com.ymx.driver.entity.app.GrabNewOrderEntity;
import com.ymx.driver.entity.app.TransferStationGrabOrder;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderListActivity;
import com.ymx.driver.ui.travel.activity.CarPoolDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.util.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 系统全局Dialog
public class GrabNewOrderDialog extends Dialog {
    private DialogGrabNewOrderBinding binding;
    private GrabNewOrderEntity grabNewOrderEntity;
    private static Timer timer;
    private static CountdownTask countdownTask;
    private int mTime = 10;
    private long lastClickTime;
    private Context context;

    // orderType==1  处理接送站逻辑  orderType==2 网约车拼车或者出租车拼车
    private int orderType;


    public GrabNewOrderDialog(@NonNull Context context, BaseGrabOrderEntity grabNewOrderEntity) {
        super(context);
        this.grabNewOrderEntity = (GrabNewOrderEntity) grabNewOrderEntity.getNewOrder();
        this.context = context;
        this.orderType = grabNewOrderEntity.getOrderType();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.getDecorView().setBackgroundColor(UIUtils.getColor(R.color.rx_transparent));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.BOTTOM);
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
                R.layout.dialog_grab_new_order, null, false);
        setContentView(binding.getRoot());

        if (orderType == 1) {
            initTransferOrderView();
        } else {
            initcarPoolOrder();
        }

        if (!TextUtils.isEmpty(grabNewOrderEntity.getOrderTypeDescription())) {
            binding.orderTypeDescriptionTv.setText(grabNewOrderEntity.getOrderTypeDescription());
        }

        binding.rideNumberLl.setVisibility(grabNewOrderEntity.getRideNumber() == 0 ? View.GONE : View.VISIBLE);
        if (grabNewOrderEntity.getRideNumber() > 0) {
            binding.rideNumberTv.setText(grabNewOrderEntity.getRideNumber());
        }

        if (grabNewOrderEntity.getPrice() > 0) {
            binding.priceLl.setVisibility(View.VISIBLE);
            binding.priceTv.setText(String.valueOf(grabNewOrderEntity.getPrice()));

        } else {
            binding.priceLl.setVisibility(View.GONE);
        }


        if (grabNewOrderEntity.getMarkupPrice() > 0) {
            binding.markupPriceLl.setVisibility(View.VISIBLE);
            binding.markupPriceTv.setText(String.valueOf(grabNewOrderEntity.getMarkupPrice()));

        } else {
            binding.markupPriceLl.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(grabNewOrderEntity.getStartAddress())) {
            binding.startAddressTv.setText(grabNewOrderEntity.getStartAddress());
        }

        if (!TextUtils.isEmpty(grabNewOrderEntity.getEndAddress())) {
            binding.endAddressTv.setText(grabNewOrderEntity.getEndAddress());
        }


        binding.grabTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    UIUtils.showToast("操作太频繁了");
                    return;
                }


                if (!TextUtils.isEmpty(grabNewOrderEntity.getOrderNo())) {
                    if (orderType == 1) {
                        ransferStationGrabOrder(grabNewOrderEntity.getOrderNo());
                    } else if (orderType == 2) {
                        carpoolGrabOrder(grabNewOrderEntity.getOrderNo());
                    }

                }

            }
        });

        timer = new Timer();
        countdownTimer();

    }

    public void initTransferOrderView() {
        List<String> noShowDialogDriverList = grabNewOrderEntity.getNoShowDialogDriverList();
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
            binding.grabLl.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
            binding.grabTv.setVisibility(View.GONE);
        } else {
            binding.grabLl.setVisibility(View.VISIBLE);
            binding.line.setVisibility(View.VISIBLE);
            binding.grabTv.setVisibility(View.VISIBLE);
            binding.grabTv.setText("接单 (" + mTime + ")");

        }

    }

    public void initcarPoolOrder() {
        binding.grabTv.setVisibility(View.VISIBLE);

    }

    public void initGrabTv(boolean enabled, Drawable drawable, String info) {
        if (binding.grabTv != null) {
            binding.grabLl.setBackground(drawable);
            binding.grabTv.setEnabled(enabled);
            binding.grabTv.setText(info);
        }
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

                    initGrabTv(true, UIUtils.getDrawable(R.drawable.bg_grab_order_type_2), "接单");

                } else {
                    if (binding.grabTv != null) {
                        initGrabTv(false, UIUtils.getDrawable(R.drawable.bg_grab_order_type_1), "接单 (" + mTime + ")");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void carpoolGrabOrder(String orderNo) {
        RetrofitFactory.sApiService.carpoolGrabOrder(orderNo)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<CarpoolGrabOrderEntity>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(CarpoolGrabOrderEntity carpoolGrabOrderEntity) {
                        if (mTime >= 0) {
                            dismiss();
                        }

                        if (carpoolGrabOrderEntity.getCategoryType() == 2) {
                            Intent intent = new Intent();
                            intent.putExtra(CarPoolDetailsActivity.ORDERI_ID, carpoolGrabOrderEntity.getOrderNodeNo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(YmxApp.getInstance(), CarPoolDetailsActivity.class);
                            YmxApp.getInstance().startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(CarPoolDetailsActivity.ORDERI_ID, carpoolGrabOrderEntity.getOrderNodeNo());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(YmxApp.getInstance(), TravelActivity.class);
                            YmxApp.getInstance().startActivity(intent);
                        }

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);

                    }
                });
    }

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

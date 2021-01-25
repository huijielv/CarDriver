package com.ymx.driver.ui.main.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;
import com.ymx.driver.databinding.DialogGrabNewOrderBinding;
import com.ymx.driver.entity.BaseGrabOrderEntity;
import com.ymx.driver.entity.app.GrabNewOrderEntity;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.main.Dialog.interfaces.CarPoolGrabOrder;
import com.ymx.driver.ui.main.Dialog.interfaces.GrabOrderInterface;
import com.ymx.driver.ui.main.Dialog.interfaces.GrabResult;
import com.ymx.driver.ui.main.Dialog.interfaces.TransferGrabOrder;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.NewOrderTTSController;
import com.ymx.driver.util.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


// 系统全局Dialog
public class GrabNewOrderDialog extends Dialog {
    private DialogGrabNewOrderBinding binding;
    private GrabNewOrderEntity grabNewOrderEntity;
    private int mTime = 10;
    private long lastClickTime;
    private Context context;

    // orderType==1  处理接送站逻辑  orderType==2 网约车拼车或者出租车拼车
    private int orderType;
    private static long autoUpdateTime = 1000;
    private Handler handler = new Handler();

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
            binding.rideNumberTv.setText(String.valueOf(grabNewOrderEntity.getRideNumber()));
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

                    return;
                }

                GrabOrderInterface grabOrderInterface = null;
                if (!TextUtils.isEmpty(grabNewOrderEntity.getOrderNo())) {
                    if (orderType == 1) {
                        grabOrderInterface = new TransferGrabOrder();
                    } else if (orderType == 2) {
                        grabOrderInterface = new CarPoolGrabOrder();
                    }
                    grabOrderInterface.GrapOrder(grabNewOrderEntity.getOrderNo(), new GrabResult() {

                        @Override
                        public void onRequestStart() {

                        }

                        @Override
                        public void onRequestEnd() {

                        }

                        @Override
                        public void onSuccees(Object o) {

                        }

                        @Override
                        public void onSuccees() {
                            if (mTime >= 0 && isShowing()) {
                                dismiss();
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            if (mTime >= 0 && isShowing()) {
                                dismiss();
                            }
                            if (binding != null && binding.grabTv != null) {
                                binding.grabTv.setEnabled(true);

                            }
                            UIUtils.showToast(message);
                        }
                    });
                }

            }
        });

//        timer = new Timer();
//        countdownTimer();

        if (handler != null) {
            handler.postDelayed(runnable, autoUpdateTime);
        }

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
            initGrabTv(false, UIUtils.getDrawable(R.drawable.bg_grab_order_type_1), "接单 (" + String.valueOf(mTime - 5) + ")");
        }

    }

    public void initcarPoolOrder() {
        binding.grabLl.setVisibility(View.VISIBLE);
        binding.line.setVisibility(View.VISIBLE);
        binding.grabTv.setVisibility(View.VISIBLE);
        initGrabTv(false, UIUtils.getDrawable(R.drawable.bg_grab_order_type_1), "接单 (" + String.valueOf(mTime - 5) + ")");

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

        NewOrderTTSController.getInstance(UIUtils.getContext()).stop();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        handler = null;
        context = null;

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                mTime--;

                if (mTime < 0) {
//                    cancal();
                    if (context != null) {
                        dismiss();
                    }

                } else if (mTime <= 5) {

                    initGrabTv(true, UIUtils.getDrawable(R.drawable.bg_grab_order_type_2), "接单");

                } else {
                    if (binding.grabTv != null) {
                        binding.grabTv.setEnabled(false);
                        binding.grabTv.setText("接单 (" + String.valueOf(mTime - 5) + ")");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mTime >= 0) {
                handler.postDelayed(this, autoUpdateTime);
            }
        }
    };

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 4500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}

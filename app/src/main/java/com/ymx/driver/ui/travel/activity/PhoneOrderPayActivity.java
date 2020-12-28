package com.ymx.driver.ui.travel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import androidx.lifecycle.Observer;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityPhoneOrderPayBinding;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.entity.app.mqtt.PhoneOrderSuccessEntity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.view.ScrollSwithViewButton;
import com.ymx.driver.viewmodel.orderdetails.PhoneOrderPayViewModel;

import org.greenrobot.eventbus.EventBus;

import static com.ymx.driver.ui.travel.activity.TravelActivity.ORDERI_ID;

public class PhoneOrderPayActivity extends BaseActivity<ActivityPhoneOrderPayBinding, PhoneOrderPayViewModel> {
    public static final String TYPE = "type";
    private String orderNo;

    private int type;

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, PhoneOrderPayActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_phone_order_pay;
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
        binding.scrollSwithViewButton.setOnSrollSwichListener(new ScrollSwithViewButton.OnSrollSwichListener() {
            @Override
            public void onSrollingMoreThanCritical() {

            }

            @Override
            public void onSrollingLessThanCriticalX() {

            }

            @Override
            public void onSlideFinishSuccess() {


                viewModel.updateOrderDetailsStatus(orderNo, "", String.valueOf(6), "");


//                }

            }

            @Override
            public void onSlideFinishCancel() {

            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDERI_ID)) {
            orderNo = intent.getStringExtra(ORDERI_ID);
            viewModel.getOrderDetails(orderNo);
            viewModel.orderPay(orderNo);

        }
        type = intent.getIntExtra(TYPE, 0);

        switch (type) {
            case 1:
                binding.scrollSwithViewButton.setVisibility(View.VISIBLE);
                viewModel.buttonVisity.set(true);
                break;
            case 2:
                binding.scrollSwithViewButton.setVisibility(View.GONE);
                viewModel.buttonVisity.set(false);
                break;

        }


    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucLoadImage.observe(this, new Observer<String>() {


            @Override
            public void onChanged(String s) {

                byte[] decodedString = Base64.decode(s, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.payOrderIv.setImageBitmap(decodedByte);

            }
        });

        viewModel.uc.ucCallPhone.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CALL_PHONE, false, Manifest.permission.CALL_PHONE);
            }
        });
        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucPhoneOrderSuccess.observe(this, new Observer<PhoneOrderSuccessEntity>() {


            @Override
            public void onChanged(PhoneOrderSuccessEntity phoneOrderSuccessEntity) {

                binding.payButton.setVisibility(View.GONE);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_PAY_RESULT));
                viewModel.paySuccess.set(1);

                if (type == 2) {
                    close();
                }


            }
        });

        viewModel.uc.ucShowPayDialog.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        viewModel.orderId.get());
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());

            }
        });


    }


    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                UserEntity userEntity = LoginHelper.getUserEntity();
                if (TextUtils.isEmpty(userEntity.getServicePhone())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data;
                if (viewModel.isCall.get()) {
                    data = Uri.parse("tel:" + viewModel.passengerCall.get());

                } else {
                    data = Uri.parse("tel:" + userEntity.getServicePhone());

                }
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);


    }


    public void close() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                AppManager.getAppManager().finishActivity(activity);

            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRAVEL_ORDERS_FINSH_DATA_CODE));

    }
}

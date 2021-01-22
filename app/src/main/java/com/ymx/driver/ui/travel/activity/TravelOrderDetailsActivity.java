package com.ymx.driver.ui.travel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;

import androidx.lifecycle.Observer;


import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;


import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.ActivityOrderDetailsBinding;

import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.entity.app.mqtt.PhoneOrderSuccessEntity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.mine.Frament.PayDialogFragment;
import com.ymx.driver.view.CashierInputFilter;
import com.ymx.driver.view.ScrollSwithViewButton;
import com.ymx.driver.viewmodel.orderdetails.OrderDetailsViewModel;
import com.ymx.driver.viewmodel.travel.TravelViewModel;

import org.greenrobot.eventbus.EventBus;


public class TravelOrderDetailsActivity extends BaseActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> {
    public static final String ORDERI_ID = "orderNo";
    public static final int DRIVER_END_SERVICE = 6;
    public static final String ACTION_TYPE = "actionType";
    public static final String CATEGORY_TYPE = "categoryType";

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TravelOrderDetailsActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_order_details;
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
        InputFilter[] filters = {new CashierInputFilter()};
        binding.otherFeeEt.setFilters(filters);
        binding.scrollOrderDetailsSwithViewButton.setOnSrollSwichListener(new ScrollSwithViewButton.OnSrollSwichListener() {
            @Override
            public void onSrollingMoreThanCritical() {

            }

            @Override
            public void onSrollingLessThanCriticalX() {

            }

            @Override
            public void onSlideFinishSuccess() {

                if (viewModel.orderStatus.get() == DRIVER_END_SERVICE) {

                    viewModel.updateOrderDetailsStatus(viewModel.orderId.get(), "", String.valueOf(viewModel.orderStatus.get()), viewModel.etPrice.get());
                } else if (viewModel.orderStatus.get() == 5) {
                    showOrderPriceDialog();
                } else if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_ORDER_FINISH) {
                    doSthIsExit();
                }
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
            String orderNo = intent.getStringExtra(ORDERI_ID);
            int actionType = intent.getIntExtra(ACTION_TYPE, 0);
            if (intent.hasExtra(CATEGORY_TYPE) && !TextUtils.isEmpty(intent.getStringExtra(CATEGORY_TYPE))) {
                viewModel.categoryType.set(Integer.parseInt(intent.getStringExtra(CATEGORY_TYPE)));
            }

            viewModel.orderStatus.set(actionType);
            viewModel.orderId.set(orderNo);
            viewModel.getOrderDetails(orderNo);

        }

    }

    @Override
    public void initViewObservable() {
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

        viewModel.uc.ucInitButtonText.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.isVip.get() == 1) {
                    binding.vip.setVisibility(View.VISIBLE);
                }

                if ((viewModel.businessType.get() == 10 && viewModel.categoryType != null && viewModel.categoryType.get() != null && viewModel.categoryType.get() == 0) || (viewModel.businessType.get() == 5 && viewModel.driverType.get() == 6)) {
                    binding.taxiLl.setVisibility(View.VISIBLE);
                    binding.priceInfoLl.setVisibility(View.GONE);
                }


                if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_CONFIRM_COST) {
                    if (viewModel.orderSource.get() == 5 && viewModel.businessType.get() != 10) {
                        binding.scrollOrderDetailsSwithViewButton.setText(getString(R.string.pay_order_details_button_text));
                    } else {
                        binding.scrollOrderDetailsSwithViewButton.setText(getString(R.string.phone_order_pay_title));
                    }
                } else if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_TO_PAY) {
                    binding.scrollOrderDetailsSwithViewButton.setText("完成");
                    viewModel.payOrder.set("未付款");
                    viewModel.orderPay(viewModel.orderId.get());

                } else if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_ORDER_FINISH) {
                    doSthIsExit();
                }

            }
        });

        viewModel.uc.ucUpdateButtonText.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.scrollOrderDetailsSwithViewButton.setText(viewModel.buttonText.get());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_ORDER_SERVICE_COMPLETED));

                if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_STATE_TO_PAY) {
                    // 电话叫车逻辑

                    if (viewModel.orderSource.get() == TravelViewModel.DRIVER_STATE_CONFIRM_COST && viewModel.businessType.get() != 10) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, viewModel.orderId.get());
                        intent.putExtra(PhoneOrderPayActivity.TYPE, 1);

                        PhoneOrderPayActivity.start(activity, intent);
                        doSthIsExit();
                    }
                    if (viewModel.businessType.get() == 10 || (viewModel.businessType.get() == 5 && viewModel.driverType.get() == 6)) {
                        return;
                    } else {
                        if (viewModel.orderId != null && !TextUtils.isEmpty(viewModel.orderId.get())) {
                            viewModel.payOrder.set("未付款");
                            viewModel.orderPay(viewModel.orderId.get());
                        }
                        viewModel.totalFee.set("");
                        viewModel.getOrderDetails(viewModel.orderId.get());
                    }

                } else if (viewModel.orderStatus.get() == TravelViewModel.DRIVER_ORDER_FINISH) {
                    doSthIsExit();
                }


            }
        });

        viewModel.uc.ucLoadQcode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String url) {
                byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.qRcode.setImageBitmap(decodedByte);

            }
        });

        viewModel.uc.ucPay.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putString(PayDialogFragment.ORDER_NO,
                        viewModel.orderId.get());
                PayDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        PayDialogFragment.class.getSimpleName());

            }
        });


        viewModel.uc.ucQcodeVisible.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.payOrdetStatusTv.setVisibility(View.GONE);
                binding.qRcodeTv.setVisibility(View.GONE);
                binding.qRcode.setVisibility(View.VISIBLE);

            }
        });

        viewModel.uc.ucPaySuccess.observe(this, new Observer<PhoneOrderSuccessEntity>() {
            @Override
            public void onChanged(PhoneOrderSuccessEntity phoneOrderSuccessEntity) {

                if (phoneOrderSuccessEntity.getOrderState() == TravelViewModel.DRIVER_ORDER_FINISH) {
                    viewModel.payOrder.set("已收款");
                }
//                viewModel.totalFee.set(phoneOrderSuccessEntity.getPayAmount());

                binding.payOrdetStatusTv.setVisibility(View.VISIBLE);
                binding.qRcode.setVisibility(View.GONE);
                binding.qRcodeTv.setVisibility(View.GONE);
                binding.payIv.setVisibility(View.GONE);


            }
        });


    }

    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CALL_PHONE) {
            try {
                UserEntity userEntity = LoginHelper.getUserEntity();
                if (TextUtils.isEmpty(userEntity.getPhone())) {
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
        overridePendingTransition(R.anim.slide_enter_right, R.anim.slide_exit_left);
    }


    private void showOrderPriceDialog() {
        new DefaultStyleDialog(activity)
                .setBody(getString(R.string.order_details_dialog_desc))
                .setNegativeText(getString(R.string.order_details_dialog_cancal))
                .setPositiveText(getString(R.string.order_details_dialog_confirm))
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        viewModel.updateOrderDetailsStatus(viewModel.orderId.get(), "", String.valueOf(viewModel.orderStatus.get()), viewModel.etPrice.get());

                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_TRIP_REFRESH_DATA_CODE));
    }


    @Override
    protected boolean isExit() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}

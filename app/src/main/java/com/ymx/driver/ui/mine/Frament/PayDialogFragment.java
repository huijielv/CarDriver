package com.ymx.driver.ui.mine.Frament;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.lifecycle.Observer;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;

import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.DialogFragmentPayBinding;
import com.ymx.driver.entity.app.WechatPayEntity;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.SystemUtils;

import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.main.PayViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


public class PayDialogFragment extends BaseDialogFragment<DialogFragmentPayBinding, PayViewModel> {

    public static final String ORDER_NO = "order_no";
    public static final String TOTAL_PRICE = "total_price";

    public static PayDialogFragment newInstance() {
        return newInstance(null);
    }

    public static PayDialogFragment newInstance(Bundle bundle) {
        PayDialogFragment fragment = new PayDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.dialog_fragment_pay;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int setWindowAnimations() {
        return R.style.FromBottomDialogWindowStyle;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            String orderNo = bundle.getString(ORDER_NO);
            viewModel.orderNo.set(orderNo);


            String totalPrice = bundle.getString(TOTAL_PRICE);
            viewModel.totalPrice.set(totalPrice);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucDismiss.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });

        viewModel.uc.ucGetWechatPayOrderInfoSuccess.observe(this, new Observer<WechatPayEntity>() {
            @Override
            public void onChanged(WechatPayEntity entity) {
                if (!SystemUtils.isWechatInstalled(activity)) {
                    return;
                }
                IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, entity.getAppid());
                PayReq req = new PayReq();
                req.appId = entity.getAppid();
                req.partnerId = entity.getPartnerid();
                req.prepayId = entity.getPrepayid();
                req.nonceStr = entity.getNoncestr();
                req.timeStamp = entity.getTimestamp();
                req.packageValue = entity.getPkg();
                req.sign = entity.getSign();
                wxapi.sendReq(req);
            }
        });

        viewModel.uc.ucGetAliPayOrderInfoSuccess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderInfo) {
                if (!SystemUtils.isAliPayInstalled(activity)) {
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(activity);

                        Map<String, String> payResult = alipay.payV2(orderInfo, true);


                        String resultStatus = payResult.get("resultStatus");
                        String result = payResult.get("result");
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            UIUtils.showToast(UIUtils.getString(R.string.pay_success));
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_PAY_RESULT));
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            UIUtils.showToast(UIUtils.getString(R.string.pay_fail));
                        }
                    }
                }).start();
            }
        });
    }
}


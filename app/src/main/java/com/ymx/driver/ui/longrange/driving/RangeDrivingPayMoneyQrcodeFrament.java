package com.ymx.driver.ui.longrange.driving;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import androidx.lifecycle.Observer;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.FramentRangeDrivingPayMoneyQrcodeBinding;
import com.ymx.driver.viewmodel.longrangdriving.RangeDrivingPayMoneyQrcodeViewModel;

import org.greenrobot.eventbus.EventBus;

public class RangeDrivingPayMoneyQrcodeFrament extends BaseDialogFragment<FramentRangeDrivingPayMoneyQrcodeBinding, RangeDrivingPayMoneyQrcodeViewModel> {

    private String orderId;

    public static final String ORDER_ID = "order_id";

    public static RangeDrivingPayMoneyQrcodeFrament newInstance() {
        return newInstance(null);
    }

    public static RangeDrivingPayMoneyQrcodeFrament newInstance(Bundle bundle) {
        RangeDrivingPayMoneyQrcodeFrament fragment = new RangeDrivingPayMoneyQrcodeFrament();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_range_driving_pay_money_qrcode;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            orderId = bundle.getString(ORDER_ID);
            if (!TextUtils.isEmpty(ORDER_ID)) {
                viewModel.orderPay(orderId);
            }
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucLoadQcode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String url) {
                byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.qRcode.setImageBitmap(decodedByte);

            }
        });

        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });

        viewModel.uc.ucShowPayDialog.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_SHOW_PAY_DIALOG, orderId));

            }
        });
        viewModel.uc.ucPaySuccess.observe(this, new Observer<Void>() {

            @Override
            public void onChanged(Void aVoid) {
                dismiss();

            }
        });


    }
}

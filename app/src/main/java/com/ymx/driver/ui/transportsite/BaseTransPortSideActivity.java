package com.ymx.driver.ui.transportsite;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.SelectTransPortSideTripListTypeBinding;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.ClassicPopupWindow;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseTransPortSideActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<V, VM> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void selectTripOrderPop(View view, int selectType) {
        ClassicPopupWindow.Builder popWindow = new ClassicPopupWindow.Builder(activity, 1);
        SelectTransPortSideTripListTypeBinding popWindowBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.select_trans_port_side_trip_list_type,
                null, false);

        if (selectType == 1) {
            popWindowBinding.meetTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.rx_ff5093E1)));
            popWindowBinding.allTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
            popWindowBinding.galconTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
            popWindowBinding.allType.setTextColor(UIUtils.getColor(R.color.rx_333333));
            popWindowBinding.meetType.setTextColor(UIUtils.getColor(R.color.white));
            popWindowBinding.galconType.setTextColor(UIUtils.getColor(R.color.rx_333333));
        } else if (selectType == 2) {
            popWindowBinding.meetTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
            popWindowBinding.allTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
            popWindowBinding.galconTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.rx_ff5093E1)));
            popWindowBinding.allType.setTextColor(UIUtils.getColor(R.color.rx_333333));
            popWindowBinding.meetType.setTextColor(UIUtils.getColor(R.color.rx_333333));
            popWindowBinding.galconType.setTextColor(UIUtils.getColor(R.color.white));
        }else if ( selectType == 0){
            popWindowBinding.allType.setTextColor(UIUtils.getColor(R.color.white));
            popWindowBinding.allTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.rx_ff5093E1)));
            popWindowBinding.meetType.setTextColor(UIUtils.getColor(R.color.rx_333333));
            popWindowBinding.meetTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
            popWindowBinding.galconType.setTextColor(UIUtils.getColor(R.color.rx_333333));
            popWindowBinding.galconTypeLl.setBackground(new ColorDrawable(UIUtils.getColor(R.color.white)));
        }


        popWindowBinding.allTypeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SELECT_TYPE_CODE, 0));
            }
        });
        popWindowBinding.meetTypeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SELECT_TYPE_CODE, 1));
            }
        });
        popWindowBinding.galconType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_SELECT_TYPE_CODE, 2));
            }
        });

        popWindow.setView(popWindowBinding.getRoot()).build().showAsBottom_Anchor_Same_Width(view, 0);
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}

package com.ymx.driver.viewmodel.main;

import android.graphics.drawable.Drawable;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.mqtt.PassengerInfoEntity;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.databinding.ObservableField;

public class GrapOrderItemViewModel extends ItemViewModel<BaseViewModel> {
    public ObservableField<PassengerInfoEntity> entity = new ObservableField<>();

    public GrapOrderItemViewModel(BaseViewModel viewModel, PassengerInfoEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public BindingCommand grabOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GRAB_GOOD_FIREND_ORDER, entity.get()));
        }
    });

    public Drawable getStatusBg() {
        if (entity.get() == null) {
            return UIUtils.getDrawable(R.drawable.bg_order_type);


        }

        if (entity.get().getChannel() == 5) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_3);
        } else {
            if (entity.get().getBusinessType() == 10) {
                return UIUtils.getDrawable(R.drawable.bg_order_type_5);
            }else if (entity.get().getBusinessType() == 9) {
                return UIUtils.getDrawable(R.drawable.bg_order_type_4);
            } else if (entity.get().getBusinessType() == 6) {
                return UIUtils.getDrawable(R.drawable.bg_order_type_appointment);
            } else {
                if (entity.get().getOrderType() == 1) {
                    return UIUtils.getDrawable(R.drawable.bg_order_type_2);

                } else if (entity.get().getOrderType() == 2) {
                    return UIUtils.getDrawable(R.drawable.bg_order_type);
                }
            }
        }


        return UIUtils.getDrawable(R.drawable.bg_order_type);
    }


}

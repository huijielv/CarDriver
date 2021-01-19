package com.ymx.driver.viewmodel.triporderlist;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.greenrobot.eventbus.EventBus;

public class TripNotFinishOrderListItemViewModel extends ItemViewModel<NotFinishTripViewModel> {
    public ObservableField<TripOrderListItem> entity = new ObservableField<>();

    public TripNotFinishOrderListItemViewModel(@NonNull NotFinishTripViewModel viewModel, TripOrderListItem entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> goTo = new SingleLiveEvent<>();


    }

    public BindingCommand goOrderActivity = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_ORDER_ACTIVITY, entity.get()));
        }
    });


    public Drawable getStatusBg() {
        if (entity.get() == null) {
            return UIUtils.getDrawable(R.drawable.bg_order_type);
        }
        if (entity.get().getBusinessType() == 1) {
            if (entity.get().getCategoryType() == 0) {
                return UIUtils.getDrawable(R.drawable.bg_order_type);
            } else if (entity.get().getCategoryType() == 2) {
                return UIUtils.getDrawable(R.drawable.bg_order_type_8);
            } else {
                return UIUtils.getDrawable(R.drawable.bg_order_type_7);
            }

        } else if
        (entity.get().getBusinessType() == 10) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_5);
        } else if (entity.get().getColor() == 1) {
            return UIUtils.getDrawable(R.drawable.bg_order_type);
        } else if (entity.get().getColor() == 2) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_2);
        } else if (entity.get().getColor() == 3) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_3);
        } else if (entity.get().getBusinessType() == 9) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_4);
        } else if (entity.get().getBusinessType() == 11) {
            return UIUtils.getDrawable(R.drawable.bg_order_type_6);
        } else if (entity.get().getColor() == 4) {

            return UIUtils.getDrawable(R.drawable.bg_order_type_appointment);

        }

        return UIUtils.getDrawable(R.drawable.bg_order_type);
    }
}

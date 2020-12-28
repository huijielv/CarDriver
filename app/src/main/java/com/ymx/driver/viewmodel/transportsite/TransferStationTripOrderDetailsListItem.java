package com.ymx.driver.viewmodel.transportsite;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.TransferCancalOrderInfo;
import com.ymx.driver.entity.app.TransferStationRecoverLiteItemEntity;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

public class TransferStationTripOrderDetailsListItem extends ItemViewModel<TransferStationTripOrderDetailsViewModel> {
    public ObservableField<TransferStationRecoverLiteItemEntity> entity = new ObservableField<>();
    public ObservableField<Integer> driverState = new ObservableField<>();

    public TransferStationTripOrderDetailsListItem(@NonNull TransferStationTripOrderDetailsViewModel viewModel, TransferStationRecoverLiteItemEntity entity, int driverState) {
        super(viewModel);
        this.entity.set(entity);
        this.driverState.set(driverState);
    }

    public BindingCommand callPassengerPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_PASSENGER_PHONE_CODE, entity.get().getPhone()));

        }
    });

    //    TransferCancalOrderInfo
    public BindingCommand cancalOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_TRANSFER_ORDER_DETAILS_CANCAL_ORDER_CODE, new TransferCancalOrderInfo(entity.get().getOrderNo(), getPhoneNumber())));

        }
    });

    public BindingCommand updatePassengerType = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MINE_TRANSFER_ORDER_UPDATE_PASSENGER_TYPE_CODE, entity.get()));

        }
    });


    public BindingCommand selectTime = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (entity.get().getAfterDrivingTimeModifyNumber() == 0) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MINE_TRANSFER_ORDER_SELECT_TIME_CODE, entity.get().getOrderNo()));
            }

        }
    });

    public BindingCommand navi = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MINE_TRANSFER_ORDER_NAVI_CODE, entity.get()));


        }
    });

    public BindingCommand qrCode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MINE_TRANSFER_ORDER_SHOW_PAY_TYPE_CODE, entity.get()));


        }
    });


    public String getPassengerType() {
        if (entity.get() == null) {
            return "";
        }

        if (entity.get().getPassengerState() == 2) {
            return UIUtils.getString(R.string.driver_transfer_type_1);
        } else if (entity.get().getPassengerState() == 3) {
            return UIUtils.getString(R.string.driver_transfer_type_2);
        } else if (entity.get().getPassengerState() == 4) {
            if (driverState.get() == 4) {
                return UIUtils.getString(R.string.driver_transfer_type_3);
            } else if (driverState.get() == 2) {
                return "";
            } else {
                return "";
            }
        } else if (entity.get().getPassengerState() == 7) {
            return UIUtils.getString(R.string.driver_transfer_type_4);
        } else {
            return "";
        }

    }

    public String getPhoneNumber() {

        if (!TextUtils.isEmpty(entity.get().getPhone()) && entity.get().getPhone().length() == 11) {
            String phone = entity.get().getPhone();
            StringBuffer sb = new StringBuffer();
            sb.append("尾号");
            sb.append(phone.substring(7));
            return sb.toString();
        } else {
            return "";
        }
    }

    public Drawable getPassengerIv() {

        if (entity.get().getPassengerState() == 2) {
            return UIUtils.getDrawable(R.drawable.icon_daodashangchedian);
        } else if (entity.get().getPassengerState() == 3) {
            return UIUtils.getDrawable(R.drawable.icon_yishangche);
        } else if (entity.get().getPassengerState() == 4) {
            return UIUtils.getDrawable(R.drawable.icon_daodamudidi);
        } else {
            return UIUtils.getDrawable(R.drawable.icon_daodamudidi);
        }

    }
}

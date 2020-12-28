package com.ymx.driver.viewmodel.transportsite;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.SiteOrderList;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

public class FindSiteOrderListItemView extends ItemViewModel<TransferFindSiteOrderListViewModel> {
    public ObservableField<SiteOrderList> entity = new ObservableField<>();


    public FindSiteOrderListItemView(@NonNull TransferFindSiteOrderListViewModel viewModel, SiteOrderList siteOrderList) {
        super(viewModel);
        this.entity.set(siteOrderList);
    }

    public Drawable getPayBg() {
        if (entity.get() == null) {
            return UIUtils.getDrawable(R.drawable.bg_transfer_order_hotel);
        }
        if (entity.get().getIsMonthPay() == 1) {
            return UIUtils.getDrawable(R.drawable.bg_month_pay);
        } else if (entity.get().getIsMonthPay() == 2) {
            return UIUtils.getDrawable(R.drawable.bg_now_pay);
        } else {
            return UIUtils.getDrawable(R.drawable.bg_transfer_order_hotel);
        }

    }

    public String getPayTv() {
        if (entity.get() == null) {
            return "";
        }
        if (entity.get().getIsMonthPay() == 1) {
            return "月付";
        } else if (entity.get().getIsMonthPay() == 2) {
            return "现付";
        } else {
            return "";
        }
    }

    private long lastpausetime;
    public BindingCommand grabOrder = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            long time = System.currentTimeMillis();
//            if (time - lastpausetime < 1000) {
//                UIUtils.showToast("你点击太快了");
//                return;
//            }
//            lastpausetime = time;


            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GO_TO_TRANSFER_GRAP_ORDER_CODE, entity.get().getOrderNo()));
        }
    });


}

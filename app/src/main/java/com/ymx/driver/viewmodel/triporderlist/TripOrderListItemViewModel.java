package com.ymx.driver.viewmodel.triporderlist;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.DetailsCostListEntity;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.main.MineActionItemViewModel;
import com.ymx.driver.viewmodel.orderdetails.OrderDetailsViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class TripOrderListItemViewModel extends ItemViewModel<NotFinishTripViewModel> {

    public ObservableField<TripOrderList> entity = new ObservableField<>();
    public ObservableField<Boolean> isShow = new ObservableField<>();

    public void setIsShow(Boolean   isShow) {
        this.isShow.set(isShow);
    }

    public ObservableList<TripNotFinishOrderListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<TripNotFinishOrderListItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.trip_not_finsh_order_item_ll);

    public TripOrderListItemViewModel(@NonNull NotFinishTripViewModel viewModel, TripOrderList entity   ) {
        super(viewModel);
        this.entity.set(entity);
        List<TripOrderListItem> tripOrderListItemList = entity.getOrderList();

        if (tripOrderListItemList.isEmpty() || tripOrderListItemList.size() == 0) {
            return;
        }
        for (TripOrderListItem tripOrderListItem : tripOrderListItemList) {
            itemList.add(new TripNotFinishOrderListItemViewModel(viewModel, tripOrderListItem));
        }



    }


}

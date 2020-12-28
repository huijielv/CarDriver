package com.ymx.driver.viewmodel.chartercar;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.CharterOrderDataListEntity;
import com.ymx.driver.entity.app.TravelListEntity;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.viewmodel.triporderlist.TripNotFinishOrderListItemViewModel;


import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class CharterDayFinishItem extends ItemViewModel<CharterDetailsViewModel> {

    public ObservableField<TravelListEntity> entity = new ObservableField<>();
    public ObservableList<CharterDayFinishListItem> itemList = new ObservableArrayList<>();
    public ItemBinding<CharterDayFinishListItem> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.charter_order_list_item_ll);

    public CharterDayFinishItem(@NonNull CharterDetailsViewModel viewModel ,TravelListEntity travelListEntity) {
        super(viewModel);

        this.entity.set(travelListEntity);
        List<CharterOrderDataListEntity> dataList = travelListEntity.getDataList();

        if (dataList.isEmpty() || dataList.size() == 0) {
            return;
        }
        itemList.clear();

        for (CharterOrderDataListEntity charterOrderDataListEntity: dataList) {
            itemList.add(new CharterDayFinishListItem(viewModel, charterOrderDataListEntity));
        }

    }
}

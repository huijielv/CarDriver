package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.IncomeDetailEntity;
import com.ymx.driver.entity.app.IncomeDetailItem;
import com.ymx.driver.entity.app.TripOrderList;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.viewmodel.triporderlist.NotFinishTripViewModel;
import com.ymx.driver.viewmodel.triporderlist.TripNotFinishOrderListItemViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class WalletBalanceListItemViewModel extends ItemViewModel<WalletBalanceFramentViewModel> {

    public ObservableField<IncomeDetailEntity> entity = new ObservableField<>();
    public ObservableList<BanlanceListItem> itemList = new ObservableArrayList<>();
    public ObservableField<Boolean> isShow = new ObservableField<>();

    public void setIsShow(Boolean   isShow) {
        this.isShow.set(isShow);
    }
    public ItemBinding<BanlanceListItem> itembinding = ItemBinding.of(BR.viewModel, R.layout.banlance_list_item_view);

    public WalletBalanceListItemViewModel(@NonNull WalletBalanceFramentViewModel viewModel, IncomeDetailEntity entity) {
        super(viewModel);
        this.entity.set(entity);
        List<IncomeDetailItem> incomeDetailItemsList = entity.getList();

        if (incomeDetailItemsList.isEmpty() || incomeDetailItemsList.size() == 0) {
            return;
        }
        for (IncomeDetailItem incomeDetailItem : incomeDetailItemsList) {
            itemList.add(new BanlanceListItem(viewModel, incomeDetailItem));
        }


    }


}

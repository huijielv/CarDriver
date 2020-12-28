package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.RemoteIncomeListItem;


public class RemoteIncomeListItemViewModel extends ItemViewModel<DayIncomeViewModel> {

    public ObservableField<RemoteIncomeListItem> entity = new ObservableField<>();

    public RemoteIncomeListItemViewModel(@NonNull DayIncomeViewModel viewModel, RemoteIncomeListItem item) {
        super(viewModel);
        this.entity.set(item);
    }
}

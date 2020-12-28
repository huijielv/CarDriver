package com.ymx.driver.viewmodel.driverinterral;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.IntegralDetailListItem;


public class IntegralDetailListItemViewModel extends ItemViewModel<IntegralDetailListViewModel> {
    public ObservableField<IntegralDetailListItem> entity = new ObservableField<>();

    public IntegralDetailListItemViewModel(@NonNull IntegralDetailListViewModel viewModel  ,IntegralDetailListItem entity ) {
        super(viewModel);
        this.entity.set(entity);
    }
}

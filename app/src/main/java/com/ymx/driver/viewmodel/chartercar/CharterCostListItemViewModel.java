package com.ymx.driver.viewmodel.chartercar;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.CharteredOrderListItem;

public class CharterCostListItemViewModel extends ItemViewModel<CharterDetailsViewModel> {
    public ObservableField<CharteredOrderListItem> entity = new ObservableField<>();

    public CharterCostListItemViewModel(@NonNull CharterDetailsViewModel viewModel, CharteredOrderListItem listItem) {
        super(viewModel);
        this.entity.set(listItem);
    }
}

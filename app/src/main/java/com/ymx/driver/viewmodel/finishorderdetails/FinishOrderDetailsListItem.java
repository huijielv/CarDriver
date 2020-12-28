package com.ymx.driver.viewmodel.finishorderdetails;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.DetailsCostListEntity;
import com.ymx.driver.viewmodel.orderdetails.OrderDetailsViewModel;

public class FinishOrderDetailsListItem extends ItemViewModel<TripOrderDetailsFinshViewModel> {
    public ObservableField<DetailsCostListEntity> entity = new ObservableField<>();

    public FinishOrderDetailsListItem(@NonNull TripOrderDetailsFinshViewModel viewModel, DetailsCostListEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}

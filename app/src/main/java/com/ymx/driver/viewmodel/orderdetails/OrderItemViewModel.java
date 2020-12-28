package com.ymx.driver.viewmodel.orderdetails;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.DetailsCostListEntity;
import com.ymx.driver.util.DateUtils;

public class OrderItemViewModel extends ItemViewModel<OrderDetailsViewModel> {
    public ObservableField<DetailsCostListEntity> entity = new ObservableField<>();

    public OrderItemViewModel(@NonNull OrderDetailsViewModel viewModel, DetailsCostListEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }

}

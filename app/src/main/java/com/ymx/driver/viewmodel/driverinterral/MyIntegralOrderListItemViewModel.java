package com.ymx.driver.viewmodel.driverinterral;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;

import com.ymx.driver.entity.app.MyIntegralOrderListItemEntity;

public class MyIntegralOrderListItemViewModel extends ItemViewModel<MyIntegralOrderListViewModel> {
  public   ObservableField<MyIntegralOrderListItemEntity> entity = new ObservableField<>();

    public MyIntegralOrderListItemViewModel(@NonNull MyIntegralOrderListViewModel viewModel, MyIntegralOrderListItemEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}

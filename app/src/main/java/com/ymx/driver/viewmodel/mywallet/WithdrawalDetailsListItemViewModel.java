package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.WithdrawDetailListItemEntity;

public class WithdrawalDetailsListItemViewModel extends ItemViewModel<WithdrawalProgressViewModel> {


    public ObservableField<WithdrawDetailListItemEntity> entity = new ObservableField<>();

    public WithdrawalDetailsListItemViewModel(@NonNull WithdrawalProgressViewModel viewModel, WithdrawDetailListItemEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}

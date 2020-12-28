package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.IncomeDetailItem;


public class BanlanceListItem  extends ItemViewModel<WalletBalanceFramentViewModel> {
    public ObservableField<IncomeDetailItem > entity = new ObservableField<>();
    public BanlanceListItem(@NonNull WalletBalanceFramentViewModel  viewModel  , IncomeDetailItem entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}

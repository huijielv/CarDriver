package com.ymx.driver.viewmodel.chartercar;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.CharterOrderDataListEntity;


public class CharterDayFinishListItem extends ItemViewModel<CharterDetailsViewModel> {

    public ObservableField<CharterOrderDataListEntity> entity = new ObservableField<>();

    public CharterDayFinishListItem(@NonNull CharterDetailsViewModel viewModel, CharterOrderDataListEntity charterOrderDataListEntity) {
        super(viewModel);
        this.entity.set(charterOrderDataListEntity);
    }
}

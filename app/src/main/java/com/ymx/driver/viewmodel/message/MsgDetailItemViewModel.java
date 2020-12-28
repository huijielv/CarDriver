package com.ymx.driver.viewmodel.message;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.MsgInfoEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Created by xuweihua
 * 2020/6/12
 */
public class MsgDetailItemViewModel extends ItemViewModel<MsgDetailViewModel> {
    public ObservableField<MsgInfoEntity> entity = new ObservableField<>();

    public MsgDetailItemViewModel(@NonNull MsgDetailViewModel viewModel, MsgInfoEntity entity) {
        super(viewModel);
        this.entity.set(entity);
    }
}

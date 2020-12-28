package com.ymx.driver.viewmodel.transportsite;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.base.ItemViewModel;

public class SiteListItemViewModel extends ItemViewModel<TranSportSiteViewModelUpdateStateViewModel> {
    public ObservableField<String> entity = new ObservableField<>();

    public SiteListItemViewModel(@NonNull TranSportSiteViewModelUpdateStateViewModel viewModel, String item) {
        super(viewModel);
        this.entity.set(item);
    }
}

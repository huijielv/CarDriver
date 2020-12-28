package com.ymx.driver.viewmodel.longrangdriving;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.app.OperationLineListItem;
import com.ymx.driver.util.UIUtils;


public class OperationLineListItemViewModel  extends ItemViewModel <LongRangeFerryDrvingViewModel>{

    public ObservableField<OperationLineListItem> entity = new ObservableField<>();

    public OperationLineListItemViewModel(@NonNull LongRangeFerryDrvingViewModel viewModel, OperationLineListItem entity) {
        super(viewModel);
        this.entity.set(entity);
    }

    public Drawable getStationBg( ) {
        if (entity.get() == null) {
            return UIUtils.getDrawable(R.drawable.icon_ferry_changgui);
        }
        if (entity.get().getStationType() == 1) {
            return UIUtils.getDrawable(R.drawable.icon_ferry_putong);
        } else if (entity.get().getStationType() == 2) {
            return UIUtils.getDrawable(R.drawable.icon_ferry_gaosu);
        }

        return UIUtils.getDrawable(R.drawable.icon_ferry_changgui);
    }

}

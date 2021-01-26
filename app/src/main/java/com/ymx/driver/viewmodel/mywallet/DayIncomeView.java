package com.ymx.driver.viewmodel.mywallet;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.DayIncomItemEntity;
import com.ymx.driver.entity.app.RemoteIncomeListItem;
import com.ymx.driver.entity.app.TripOrderListItem;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.triporderlist.TripNotFinishOrderListItemViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class DayIncomeView extends ItemViewModel<DayIncomeViewModel> {
    public ObservableField<DayIncomItemEntity> entity = new ObservableField<>();

    public ObservableField<Boolean> turn = new ObservableField<>();
    public ObservableList<RemoteIncomeListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<RemoteIncomeListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.remote_income_listitem_view);


    public DayIncomeView(@NonNull DayIncomeViewModel viewModel, DayIncomItemEntity entity) {
        super(viewModel);
        turn.set(false);
        this.entity.set(entity);


        List<RemoteIncomeListItem> tripOrderListItemList = entity.getRemoteIncomeList();

        if (tripOrderListItemList.isEmpty() || tripOrderListItemList.size() == 0) {
            return;
        }
        for (RemoteIncomeListItem tripOrderListItem : tripOrderListItemList) {
            itemList.add(new RemoteIncomeListItemViewModel(viewModel, tripOrderListItem));
        }

    }

    public BindingCommand turnUp = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (!turn.get()) {
                turn.set(true);
            } else {
                turn.set(false);
            }

        }
    });


    public boolean getBusinessTypeStatus() {
        if (entity.get() == null) {
            return false;
        }
        if (entity.get().getBusinessType() == 6 || entity.get().getBusinessType() == 7) {
            return true;
        } else {
            return false;
        }


    }
}

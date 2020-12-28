package com.ymx.driver.viewmodel.mywallet;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.entity.MonthIncomeDetailItemEntity;
import com.ymx.driver.entity.app.MonthIncomeDayItemEntity;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MonthIncomeDetailsItemView extends ItemViewModel<MonthIncomeDetailViewModel> {

    public ObservableField<MonthIncomeDetailItemEntity> entity = new ObservableField<>();


    public ObservableList<DayIncomeItemViewModel> monthIncomeList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<DayIncomeItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.day_income_item_view);
    public ObservableField<Integer> status = new ObservableField<>(0);

    public MonthIncomeDetailsItemView(@NonNull MonthIncomeDetailViewModel viewModel, MonthIncomeDetailItemEntity entity) {
        super(viewModel);
        this.entity.set(entity);

        List<MonthIncomeDayItemEntity> dayList = entity.getDayList();
        if (!dayList.isEmpty()) {
            status.set(1);
            for (MonthIncomeDayItemEntity monthIncomeDayItemEntity : dayList) {
                monthIncomeList.add(new DayIncomeItemViewModel(viewModel, monthIncomeDayItemEntity));
            }
        } else {
            status.set(0);
        }
    }
}

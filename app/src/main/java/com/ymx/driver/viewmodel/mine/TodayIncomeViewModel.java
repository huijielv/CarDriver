package com.ymx.driver.viewmodel.mine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.entity.app.DayIncomItemEntity;
import com.ymx.driver.entity.app.DayIncomeDetailEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;

import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mywallet.DayIncomeView;



import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class TodayIncomeViewModel extends BaseViewModel {
    public TodayIncomeViewModel(@NonNull Application application) {
        super(application);
    }


    public ObservableField<String> totalIncome = new ObservableField<>();
    public ObservableField<String> monthData = new ObservableField<>();
    public ObservableField<String> loadDay = new ObservableField<>();

    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    //给RecyclerView添加ObservableList
    public ObservableList<DayIncomeView> dayIncomeList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<DayIncomeView> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.day_incomedetails_item_view);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    private void onDayIncomeListLoadSuccess(List<DayIncomItemEntity> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

        for (DayIncomItemEntity entity : list) {
//            dayIncomeList.add(new DayIncomeView(this, entity));
        }
    }


    public void getDayIncomeDetail(String day, int pageSize, int currentPage) {
//        RetrofitFactory.sApiService.getDayIncomeDetail(day, pageSize, currentPage)
//                .map(new TFunc<>())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new TObserver<DayIncomeDetailEntity>() {
//                    @Override
//                    protected void onRequestStart() {
//                        getUC().getShowDialogEvent().call();
//                    }
//
//                    @Override
//                    protected void onRequestEnd() {
//                        getUC().getDismissDialogEvent().call();
//                    }
//
//                    @Override
//                    protected void onSuccees(DayIncomeDetailEntity dayIncomeDetailEntity) {
//                        totalIncome.set(dayIncomeDetailEntity.getTotalIncome());
//
//                        onDayIncomeListLoadSuccess(dayIncomeDetailEntity.getList());
//                    }
//
//                    @Override
//                    protected void onFailure(String message) {
//                        UIUtils.showToast(message);
//                    }
//                });
    }



}

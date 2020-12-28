package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;

import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;


import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.MonthIncomeDetailItemEntity;
import com.ymx.driver.entity.app.DayIncomItemEntity;
import com.ymx.driver.entity.app.DayIncomeDetailEntity;
import com.ymx.driver.entity.app.MonthIncomeDetailEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;


import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class DayIncomeViewModel extends BaseViewModel {

    public static final int nextDay = 1;
    public static final int priousDay = -1;
    public static final int priousSexMonth = -181;
    public ObservableField<String> title = new ObservableField<>("");
    public ObservableField<Integer> incomeType = new ObservableField<>(0);

    public DayIncomeViewModel(@NonNull Application application) {
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

    public ObservableInt status = new ObservableInt(StatusView.STATUS_NORMAL);

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucShow = new SingleLiveEvent<>();
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
    }

    public BindingCommand show = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucShow.call();
        }
    });

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    private void onDayIncomeListLoadSuccess(List<DayIncomItemEntity> list) {
        if (list == null || list.size() <= 0) {


            if (list.size() == 0) {
                status.set(StatusView.STATUS_EMPTY);
            }

            return;
        }

            status.set(StatusView.STATUS_NORMAL);


        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }

        for (DayIncomItemEntity entity : list) {
            dayIncomeList.add(new DayIncomeView(this, entity));
        }
    }


    public void getDayIncomeDetail(String day, int pageSize, int currentPage ,int  incomeType) {
        RetrofitFactory.sApiService.getDayIncomeDetail(day, pageSize, currentPage,incomeType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<DayIncomeDetailEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(DayIncomeDetailEntity dayIncomeDetailEntity) {
                        totalIncome.set(dayIncomeDetailEntity.getTotalIncome());
                        monthData.set(dayIncomeDetailEntity.getDay());
                        onDayIncomeListLoadSuccess(dayIncomeDetailEntity.getList());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public BindingCommand nextMonthDay = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if (DateUtils.getDate(DateUtils.setDay(loadDay.get(), nextDay)).after(DateUtils.getDate(DateUtils.getDate()))) {
                UIUtils.showToast("超出今天不可选");
            } else {
                loadDay.set(DateUtils.setDay(loadDay.get(), nextDay));
                pagerIndex.set(1);
                dayIncomeList.clear();
                uc.ucCanLoadmore.setValue(true);
                getDayIncomeDetail(loadDay.get(), 10, pagerIndex.get(),incomeType.get());
            }


//            }
        }
    });

    public BindingCommand previousDay = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //  现在时间
            Date now = DateUtils.getDate(DateUtils.getDate());
            // 往前算6个月
            Date priousSexMonthDay = DateUtils.getDate(DateUtils.setDay(DateUtils.getDate(), priousSexMonth));
            Date b = DateUtils.getDate(DateUtils.setDay(loadDay.get(), priousDay));

            if (DateUtils.belongCalendar(b, priousSexMonthDay, now)) {

                loadDay.set(DateUtils.setDay(loadDay.get(), priousDay));
                pagerIndex.set(1);
                dayIncomeList.clear();
                uc.ucCanLoadmore.setValue(true);
                getDayIncomeDetail(loadDay.get(), 10, pagerIndex.get(),incomeType.get());
            } else {
                UIUtils.showToast("历史日期最大可往前选择180天");
            }


        }
    });

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {

            // 系统自动派单消息
            case MessageEvent.MSG_CHARTTER_TODAY_INCOME_CODE:

                String time = (String) event.src;
                loadDay.set(time);
                dayIncomeList.clear();
                pagerIndex.set(1);
                getDayIncomeDetail(time, 10, pagerIndex.get(),incomeType.get());
                break;
        }
    }


}

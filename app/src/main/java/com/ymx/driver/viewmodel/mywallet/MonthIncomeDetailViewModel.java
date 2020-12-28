package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.MonthIncomeDetailItemEntity;
import com.ymx.driver.entity.app.MonthIncomeDayItemEntity;
import com.ymx.driver.entity.app.MonthIncomeDetailEntity;
import com.ymx.driver.entity.app.MsgInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.message.MsgDetailItemViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class MonthIncomeDetailViewModel extends BaseViewModel {

    public MonthIncomeDetailViewModel(@NonNull Application application) {
        super(application);
    }


    public ObservableField<Integer> incomeType= new ObservableField<>(0);
    public ObservableField<String> totalIncome = new ObservableField<>();
    public ObservableField<String> monthData = new ObservableField<>();
    public ObservableField<String> time = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    @Override
    public void onCreate() {
        super.onCreate();
        time.set(DateUtils.getDate());

    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);

        switch (event.type) {

            case MessageEvent.MSG_MYMSG_DAY_INCOME_DETAILS_CODE:

                MonthIncomeDayItemEntity monthIncomeDayItemEntity = (MonthIncomeDayItemEntity) event.src;
                uc.ucMonthIncomeDayItemEntity.setValue(monthIncomeDayItemEntity);

                break;
        }
    }

    public ObservableList<MonthIncomeDetailsItemView> monthIncomeList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MonthIncomeDetailsItemView> itembinding = ItemBinding.of(BR.viewModel, R.layout.month_income_details_item_view);


    private void onMonthIncomeListLoadSuccess(List<MonthIncomeDetailItemEntity> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        for (MonthIncomeDetailItemEntity entity : list) {
            monthIncomeList.add(new MonthIncomeDetailsItemView(this, entity));
        }
    }


    public void getMonthIncomeDetail(String year, String month ,int incomeType) {
        RetrofitFactory.sApiService.getMonthIncomeDetail(year, month ,incomeType)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<MonthIncomeDetailEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(MonthIncomeDetailEntity monthIncomeDetailItemEntity) {
                        totalIncome.set(monthIncomeDetailItemEntity.getTotalIncome());
                        monthData.set(monthIncomeDetailItemEntity.getMonth());
                        onMonthIncomeListLoadSuccess(monthIncomeDetailItemEntity.getList());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<MonthIncomeDayItemEntity> ucMonthIncomeDayItemEntity = new SingleLiveEvent<>();


    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });

    public BindingCommand nextMonth = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            int month = DateUtils.getMonth(DateUtils.nextMonth(time.get()));
            int year = DateUtils.getYear(DateUtils.nextMonth(time.get()));
            int nowMonth = DateUtils.getMonth(DateUtils.getDate());
            int nowYear = DateUtils.getYear(DateUtils.getDate());

            if (month > nowMonth && year == nowYear) {
                LogUtil.d("MonthIncomeDetailViewModel", time.get());
                UIUtils.showToast("超出当前月不可选");
                return;
            } else if (month < nowMonth && year > nowYear) {
                UIUtils.showToast("超出当前月不可选");
                return;
            } else {
                time.set(DateUtils.nextMonth(time.get()));
                monthIncomeList.clear();
                getMonthIncomeDetail(String.valueOf(DateUtils.getYear(time.get())), String.valueOf(DateUtils.getMonth(time.get())),incomeType.get());
            }
        }
    });

    public BindingCommand previousMoth = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            int month = DateUtils.getMonth(DateUtils.priousMonth(time.get()));
            int year = DateUtils.getYear(DateUtils.priousMonth(time.get()));

            int nowMonth = DateUtils.getMonth(DateUtils.getDate());
            int nowYear = DateUtils.getYear(DateUtils.getDate());

            if (Math.abs(month - nowMonth) < 6) {
                monthIncomeList.clear();
                time.set(DateUtils.priousMonth(time.get()));
                getMonthIncomeDetail(String.valueOf(DateUtils.getYear(time.get())), String.valueOf(DateUtils.getMonth(time.get())),incomeType.get());
            } else {
                UIUtils.showToast("历史月份最大可往前选择6个月");
            }
        }
    });


}

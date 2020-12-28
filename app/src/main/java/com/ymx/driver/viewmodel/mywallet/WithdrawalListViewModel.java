package com.ymx.driver.viewmodel.mywallet;

import android.app.Application;

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
import com.ymx.driver.entity.app.IncomeDetailItem;
import com.ymx.driver.entity.app.WithdrawInfoEntity;
import com.ymx.driver.entity.app.WithdrawListModel;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class WithdrawalListViewModel extends BaseViewModel {
    public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    public ObservableList<WithdrawalListViewModelItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<WithdrawalListViewModelItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.withdrawal_list_item_view);


    public WithdrawalListViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Boolean> ucCanLoadmore = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();

        public SingleLiveEvent<WithdrawListModel> ucWithdrawListModel = new SingleLiveEvent<>();


    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });


    public void getWithdrawalList(List<WithdrawListModel> list) {
        List<WithdrawListModel> incomeDetailItemsList = list;

        if (incomeDetailItemsList.isEmpty() || incomeDetailItemsList.size() == 0) {
            return;
        }
        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(true);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }


        for (WithdrawListModel withdrawListModel : incomeDetailItemsList) {
            itemList.add(new WithdrawalListViewModelItemViewModel(this, withdrawListModel));
        }
    }


    public void getWithdrawListInfo(int pageSize, int currentPage) {
        RetrofitFactory.sApiService.getWithdrawList(pageSize, currentPage)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<WithdrawListModel>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<WithdrawListModel> list) {
                        getWithdrawalList(list);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        switch (event.type) {
            case MessageEvent.MSG_WIDLLET_CODE:
                WithdrawListModel withdrawListModel = (WithdrawListModel) event.src;
                uc.ucWithdrawListModel.setValue(withdrawListModel);
                break;
        }
    }

}

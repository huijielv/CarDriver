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
import com.ymx.driver.entity.app.WithdrawDetailEntity;
import com.ymx.driver.entity.app.WithdrawDetailListItemEntity;
import com.ymx.driver.entity.app.WithdrawListModel;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class WithdrawalProgressViewModel extends BaseViewModel {

    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<String> bankName = new ObservableField<>();
    public ObservableField<String> bankCard = new ObservableField<>();

    public ObservableList<WithdrawalDetailsListItemViewModel> itemList = new ObservableArrayList<>();
    public ItemBinding<WithdrawalDetailsListItemViewModel> itembinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.withdrawal_details_list_item_view);


    public WithdrawalProgressViewModel(@NonNull Application application) {
        super(application);
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();

    }

    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.back.call();
        }
    });


    public void getWithdrawalList(List<WithdrawDetailListItemEntity> list) {
        List<WithdrawDetailListItemEntity> withdrawDetailListItemEntityList = list;

        if (withdrawDetailListItemEntityList.isEmpty() || withdrawDetailListItemEntityList.size() <= 0) {
            return;
        }


        for (WithdrawDetailListItemEntity withdrawDetailListItemEntity : withdrawDetailListItemEntityList) {
            itemList.add(new WithdrawalDetailsListItemViewModel(this, withdrawDetailListItemEntity));
        }
    }


    public void getWithdrawListInfo(int id) {
        RetrofitFactory.sApiService.getWithdrawDetail(id)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<WithdrawDetailEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(WithdrawDetailEntity withdrawDetailEntity) {
                        money.set(withdrawDetailEntity.getMoney());
                        bankName.set(withdrawDetailEntity.getBankName());
                        bankCard.set(withdrawDetailEntity.getBankCard());

                        getWithdrawalList(withdrawDetailEntity.getList());
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


}

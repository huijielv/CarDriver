package com.ymx.driver.viewmodel.message;

import android.app.Application;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.BaseEntity;
import com.ymx.driver.entity.app.MsgInfoEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class MsgDetailViewModel extends BaseViewModel {
    public MsgDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableInt msgId = new ObservableInt();
    public ObservableField<String> msgTitle = new ObservableField<>();

    public ObservableField<Integer> pagerIndex = new ObservableField<>(1);
    //给RecyclerView添加ObservableList
    public ObservableList<MsgDetailItemViewModel> msgDetailList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MsgDetailItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.item_msg_detail);

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

    private void onOrderListLoadSuccess(List<MsgInfoEntity> list) {
        if (list == null || list.size() <= 0) {
            uc.ucCanLoadmore.setValue(false);
            return;
        }
        if (list.size() < 10) {
            uc.ucCanLoadmore.setValue(false);
        } else {
            uc.ucCanLoadmore.setValue(true);
        }
        for (MsgInfoEntity entity : list) {
            msgDetailList.add(new MsgDetailItemViewModel(this, entity));
        }
    }

    public void getMsgDetailList() {
        RetrofitFactory.sApiService.getMsgDetailList(msgId.get(), pagerIndex.get(),10)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<MsgInfoEntity>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<MsgInfoEntity> list) {
                        onOrderListLoadSuccess(list);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void setMsgRead(int id) {
        RetrofitFactory.sApiService.setMsgRead(id)
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<BaseEntity>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(BaseEntity entity) {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_REFRESH_MSG_READ_STATUS, id));
                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
    }
}

package com.ymx.driver.viewmodel.main;

import android.app.Application;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.MsgEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.StatusView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by xuweihua
 * 2020/5/4
 */
public class MsgViewModel extends BaseViewModel {
    public MsgViewModel(@NonNull Application application) {
        super(application);
        msgList.clear();
        getMsgList();
    }

    public ObservableInt status = new ObservableInt(StatusView.STATUS_NORMAL);
    //给RecyclerView添加ObservableList
    public ObservableList<MsgItemViewModel> msgList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MsgItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.msg_list_item);

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<MsgEntity> ucToMsgDetail = new SingleLiveEvent<>();
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_REFRESH_MSG_READ_STATUS:
                refreshMsgStatus((Integer) event.src);
                break;
            case MessageEvent.MSG_MYMSG_REFRESH_DATA_CODE:
                msgList.clear();
                getMsgList();
                break;

            case MessageEvent.MSG_HOME_MESSAGE_DATA_CODE:

                msgList.clear();
                getMsgList();
                break;

        }
    }

    private void onMsgListLoadSuccess(List<MsgEntity> list) {
        if (list == null || list.size() <= 0) {
            if (msgList.size() == 0) {
                status.set(StatusView.STATUS_EMPTY);
            }
            return;
        }
        for (MsgEntity entity : list) {
            msgList.add(new MsgItemViewModel(this, entity));
        }
    }

    // 0--表示全部设为已读
    private void refreshMsgStatus(int id) {
        for (MsgItemViewModel itemViewModel : msgList) {
            if (id != 0) {
                if (itemViewModel.entity.get() != null
                        && itemViewModel.entity.get().getId() == id) {
                    itemViewModel.entity.get().setNum(0);
                    msgList.set(getItemPosition(itemViewModel), itemViewModel);
                    break;
                }
            } else {
                if (itemViewModel.entity.get() != null) {
                    itemViewModel.entity.get().setNum(0);
                    msgList.set(getItemPosition(itemViewModel), itemViewModel);
                }
            }
        }
    }

    private int getItemPosition(MsgItemViewModel itemViewModel) {
        return msgList.indexOf(itemViewModel);
    }

    public void getMsgList() {
        RetrofitFactory.sApiService.getMsgList()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<List<MsgEntity>>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(List<MsgEntity> list) {
                        msgList.clear();
                        onMsgListLoadSuccess(list);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    public void loadData() {

    }
}

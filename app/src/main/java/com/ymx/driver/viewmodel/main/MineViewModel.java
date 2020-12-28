package com.ymx.driver.viewmodel.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.app.FindCarpoolingSiteResultEntity;
import com.ymx.driver.entity.app.GetCharteredInfoEntity;
import com.ymx.driver.entity.app.MineActionEntity;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.UserEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;
import com.ymx.driver.ui.chartercar.CharterDrivingActivity;
import com.ymx.driver.ui.integral.InterralActivity;
import com.ymx.driver.ui.launch.activity.QcodeActivity;
import com.ymx.driver.ui.login.LoginHelper;
import com.ymx.driver.ui.longrange.driving.LongRangeDrivingActivity;
import com.ymx.driver.ui.mine.activity.ContactUsActivity;
import com.ymx.driver.ui.mine.activity.SettingActivity;
import com.ymx.driver.ui.transportsite.TransportSiteUpdateCarStateActivity;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.zxinglibrary.android.CaptureActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by xuweihua
 * 2020/5/4
 */
public class MineViewModel extends BaseViewModel {


    public MineViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        mineActionList.clear();
        this.entity.set(null);
        loadInfoData();
        loadActionData();
    }

    private void loadInfoData() {
        UserEntity userEntity = LoginHelper.getUserEntity();
        if (userEntity != null) {
            this.entity.set(userEntity);
        }
        driverIntegral.set(YmxCache.getDriverIntegral());
    }

    public ObservableField<UserEntity> entity = new ObservableField<>();
    public ObservableField<String> driverIntegral = new ObservableField<>();

    //给RecyclerView添加ObservableList
    public ObservableList<MineActionItemViewModel> mineActionList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<MineActionItemViewModel> mineActionItembinding = ItemBinding.of(BR.viewModel,
            R.layout.item_mine_action);

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Class<?>> ucItemClick = new SingleLiveEvent<>();
        public SingleLiveEvent<RemoteInfoEntity> ucGo = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucGoScan = new SingleLiveEvent<>();
        public SingleLiveEvent<GetCharteredInfoEntity> ucGoCharter = new SingleLiveEvent<>();
        public SingleLiveEvent<FindCarpoolingSiteResultEntity> ucGoTransferStationSite= new SingleLiveEvent<>();
    }


    public int getItemPosition(MineActionItemViewModel mineActionItemViewModel) {
        return mineActionList.indexOf(mineActionItemViewModel);
    }

    public void loadActionData() {
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.ic_contact, R.string.contact_us, ContactUsActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.ic_setting, R.string.settings, SettingActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.ic_qrcode, R.string.qrcode, QcodeActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.icon_yuancheng_chuche, R.string.yuan_cheng_chu_che, LongRangeDrivingActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.icon_personal_saoyisao, R.string.shao_yi_shao, CaptureActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.icon_baochechuche, R.string.driver_charter_car, CharterDrivingActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.icon_my_integral, R.string.my_integral, InterralActivity.class)));
        mineActionList.add(new MineActionItemViewModel(this,
                new MineActionEntity(R.drawable.icon_car_pooling_side, R.string.my_car_pooling_site, TransportSiteUpdateCarStateActivity.class)));


    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_GO_TO_LONG_DRVING:
                getRemoteInfo();

                break;

            case MessageEvent.MSG_GOTO_SCAN_CODE:
                uc.ucGoScan.call();

                break;
            case MessageEvent.MSG_GOTO_CHARTERCAR_CODE:
                getCharteredInfo();
                break;
            case MessageEvent.MSG_SHOW_CONFIRM_EXCHANGE_SUCCESS_CODE:
                driverIntegral.set((String) event.src);
                break;
            case MessageEvent.MSG_MINE_GOTO_TRANSFER_ORDER_CODE:
                findCarpoolingSite();
                break;

        }
    }

    public void getRemoteInfo() {
        RetrofitFactory.sApiService.getRemoteInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<RemoteInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(RemoteInfoEntity remoteInfo) {
                        uc.ucGo.setValue(remoteInfo);

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });


    }

    public void getCharteredInfo() {
        RetrofitFactory.sApiService.getCharteredInfo()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<GetCharteredInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(GetCharteredInfoEntity getCharteredInfoEntity) {
                        uc.ucGoCharter.setValue(getCharteredInfoEntity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });


    }

    // 接送站拼车
    public void findCarpoolingSite() {
        RetrofitFactory.sApiService.findCarpoolingSite()
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<FindCarpoolingSiteResultEntity>() {
                    @Override
                    protected void onRequestStart() {
                        getUC().getShowDialogEvent().call();
                    }

                    @Override
                    protected void onRequestEnd() {
                        getUC().getDismissDialogEvent().call();
                    }

                    @Override
                    protected void onSuccees(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                      uc.ucGoTransferStationSite.setValue(findCarpoolingSiteResultEntity);
                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);


                    }
                });
    }


}

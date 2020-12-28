package com.ymx.driver.viewmodel.longrangdriving;

import android.app.Application;
import android.text.TextUtils;


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
import com.ymx.driver.binding.command.BindingConsumer;

import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.entity.RangDriverPassgerFinishOederDetails;
import com.ymx.driver.entity.app.RangDrivingDayChoose;
import com.ymx.driver.entity.app.RangeDrivingSelectTimeEntity;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.SelectRangeDrivingConFirmEntity;
import com.ymx.driver.entity.app.UpdateRemoteInfoBodyEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;


import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.LogUtil;
import com.ymx.driver.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class LongRangeDrivingViewModel extends BaseViewModel {

    public ObservableField<String> startName = new ObservableField<>();
    public ObservableField<String> endName = new ObservableField<>();
    public ObservableField<String> startSelectTime = new ObservableField<>();
    public ObservableField<String> endSelectTime = new ObservableField<>();
    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> endTime = new ObservableField<>();
    public ObservableField<Integer> remoteState = new ObservableField<>();
    public ObservableField<Integer> dayType = new ObservableField<>();
    public ObservableField<String> departureTime = new ObservableField<>();
    public ObservableField<String> selectDay = new ObservableField<>();
    public ObservableField<String> todayDayTime = new ObservableField<>();
    public ObservableField<String> todayEndTime = new ObservableField<>();
    public ObservableField<String> todayStartTime = new ObservableField<>();
    public ObservableField<String> todayNowTime = new ObservableField<>();

    public ObservableField<Integer> stationType = new ObservableField<>();

    public ObservableField<Integer> lockDrvierState = new ObservableField<>();

    public ObservableField<String> lockTips = new ObservableField<>();
    public ObservableList<DayChooseItemViewModel> dayChooseItemViewModels = new ObservableArrayList<>();

    public ItemBinding<DayChooseItemViewModel> itembinding = ItemBinding.of(BR.viewModel, R.layout.item_range_driving_day_choose);

    public LongRangeDrivingViewModel(@NonNull Application application) {
        super(application);

    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> ucBack = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucRangeDrivingSelectTimeDialog = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucSelect = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucButtonStatus = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucImage = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> ucLockDrvier = new SingleLiveEvent<>();
    }


    public BindingCommand back = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucBack.call();
        }
    });

    public BindingCommand rangeDrivingSelectTimeDialog = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (remoteState.get() != null && remoteState.get() == 0) {


                uc.ucRangeDrivingSelectTimeDialog.call();
            } else if (remoteState.get() != null && remoteState.get() == 1) {
                UIUtils.showToast("出车接单中");
            } else if (remoteState.get() != null && remoteState.get() == 2) {
                UIUtils.showToast("正在行程中");
            }

        }
    });
    public BindingCommand turn = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if ((remoteState.get() != null && remoteState.get() == 0) || (remoteState.get() != null && remoteState.get() == 1)) {
                updateRemoteInfo(1, 0, "", "", 0);

            } else if (remoteState.get() != null && remoteState.get() == 2) {
                UIUtils.showToast("正在行程中");
            }


        }
    });

    public BindingCommand lockDrvier = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.ucLockDrvier.call();
        }
    });


    public BindingCommand<Integer> dayChooseClick = new BindingCommand<Integer>(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer position) {

            for (int i = 0; i < dayChooseItemViewModels.size(); i++) {
                DayChooseItemViewModel drivingDayChoose = dayChooseItemViewModels.get(i);

                if (i == position) {
                    drivingDayChoose.rangDrivingDayChoose.get().setSelect(true);
                } else {
                    drivingDayChoose.rangDrivingDayChoose.get().setSelect(false);
                }


            }

            uc.ucSelect.call();


        }
    });

    public void initDayList(int dayType) {
        for (int i = 0; i < dayType; i++) {
            RangDrivingDayChoose drivingDayChoose = new RangDrivingDayChoose();


            if (i == 0) {
                drivingDayChoose.setDay("今天");
                drivingDayChoose.setSelect(true);
            } else if (i == 1) {
                drivingDayChoose.setDay("明天");
                drivingDayChoose.setSelect(false);
            } else if (i == 2) {
                drivingDayChoose.setDay("后天");
                drivingDayChoose.setSelect(false);
            }

            DayChooseItemViewModel dayChooseItemViewModel = new DayChooseItemViewModel(this, drivingDayChoose);
            dayChooseItemViewModels.add(dayChooseItemViewModel);
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
                        lockTips.set(remoteInfo.getLockTips());
                        lockDrvierState.set(remoteInfo.getLockState());
                        startName.set(remoteInfo.getStartSite());
                        endName.set(remoteInfo.getEndSite());
                        remoteState.set(remoteInfo.getRemoteState());
                        dayType.set(remoteInfo.getDay());
                        startTime.set(remoteInfo.getStartTime());
                        endTime.set(remoteInfo.getEndTime());
                        departureTime.set(remoteInfo.getDepartureTime());
                        todayNowTime.set(DateUtils.longToDate(Long.parseLong(remoteInfo.getTodayStartTime()) * 1000));



                        todayDayTime.set(todayNowTime.get().substring(11, 16));
                        todayEndTime.set(new StringBuffer().append(todayNowTime.get().substring(0, 11)).append(" ").append(remoteInfo.getEndTime()).append(":").append("00").toString());
                        stationType.set(remoteInfo.getStationType());

                        initDayList(remoteInfo.getDay());
                        if (remoteInfo.getRemoteState() == 1) {
                            uc.ucButtonStatus.call();
                        }
                        uc.ucImage.call();

//                        try {
//                            LogUtil.d("test", DateUtils.belongCalendar(DateUtils.getHourDate(todayNowTime.get()), DateUtils.getHourDate(todayEndTime.get())) + "");
//
//                            LogUtil.d("test", DateUtils.belongCalendar(DateUtils.getHourDate(todayNowTime.get()), DateUtils.getHourDate(getStartMinnus(todayEndTime.get(), -11)), DateUtils.getHourDate(todayEndTime.get())) + "");
//                            LogUtil.d("test", todayNowTime.get() + "");
//                            LogUtil.d("test", todayDayTime.get() + "");
//                            LogUtil.d("test", todayEndTime.get() + "");
//
//                        } catch (Exception e) {
//
//                        }


                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });


    }


    public void updateRemoteInfo(int opType, int day, String staTime, String eTime, int state) {
        RetrofitFactory.sApiService.updateRemoteInfo(new UpdateRemoteInfoBodyEntity(opType, day, staTime, eTime, state))
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
                        lockTips.set(remoteInfo.getLockTips());
                        lockDrvierState.set(remoteInfo.getLockState());
                        startName.set(remoteInfo.getStartSite());
                        endName.set(remoteInfo.getEndSite());
                        remoteState.set(remoteInfo.getRemoteState());
                        dayType.set(remoteInfo.getDay());
                        startTime.set(remoteInfo.getStartTime());
                        endTime.set(remoteInfo.getEndTime());
                        if (opType == 2) {
                            departureTime.set(remoteInfo.getDepartureTime());
                        }

                        uc.ucButtonStatus.call();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }


    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.type) {
            case MessageEvent.MSG_SELECT_RANGGE_DRIVING_TIME_CODE:

//                RangeDrivingSelectTimeEntity rangeDrivingSelectTimeEntity = (RangeDrivingSelectTimeEntity) event.src;
//                if (rangeDrivingSelectTimeEntity.getSelectType() == 1) {
//                    endSelectTime.set(rangeDrivingSelectTimeEntity.getTime());
//                } else {
//                    startSelectTime.set(rangeDrivingSelectTimeEntity.getTime());
//
//                }

                break;
            case MessageEvent.MSG_SELECT_RANGGE_DRIVING_CONFIRM_CODE:

                SelectRangeDrivingConFirmEntity entity = (SelectRangeDrivingConFirmEntity) event.src;
                startSelectTime.set(entity.getStartTime());
                endSelectTime.set(entity.getEndTime());

                if (startSelectTime.get() != null && endSelectTime.get() != null) {
                    StringBuilder builder = new StringBuilder();

                    builder.append(TextUtils.isEmpty(selectDay.get()) ? "今日" : selectDay.get()).append(startSelectTime.get().substring(0, 2)).append("时")
                            .append(startSelectTime.get().substring(3, 5)).append("分").append("~").append(endSelectTime.get().substring(0, 2)).append("时")
                            .append(endSelectTime.get().substring(3, 5)).append("分");
                    departureTime.set(builder.toString());
                }
                uc.ucButtonStatus.call();

                break;

            case MessageEvent.MSG_CODE_TIME_EXPIRED:
                dayChooseItemViewModels.clear();
                getRemoteInfo();
                break;




            case MessageEvent.MSG_SELECT_RANGGE_DRIVING_DAY_CONFIRM_CODE:
                RangDrivingDayChoose rangDrivingDayChoose = (RangDrivingDayChoose) event.src;
                selectDay.set(rangDrivingDayChoose.getDay());

                for (int i = 0; i < dayChooseItemViewModels.size(); i++) {
                    DayChooseItemViewModel dayChooseItemViewModel = dayChooseItemViewModels.get(i);
                    if (dayChooseItemViewModels.size() > 1) {
                        if (dayChooseItemViewModel.rangDrivingDayChoose.get().getDay().equals(rangDrivingDayChoose.getDay())) {
                            dayChooseItemViewModel.rangDrivingDayChoose.get().setSelect(true);
                        } else {
                            dayChooseItemViewModel.rangDrivingDayChoose.get().setSelect(false);
                        }
                    }
                }

                if (startSelectTime.get() != null && endSelectTime.get() != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(TextUtils.isEmpty(selectDay.get()) ? "今日" : selectDay.get()).append(startSelectTime.get().substring(0, 2)).append("时")
                            .append(startSelectTime.get().substring(3, 5)).append("分").append("~").append(endSelectTime.get().substring(0, 2)).append("时")
                            .append(endSelectTime.get().substring(3, 5)).append("分");
                    departureTime.set(builder.toString());
                }
                uc.ucSelect.call();

                break;

            case MessageEvent.MSG_DRIVER_LOCK_CODE:

                break;

        }
    }


    public static String getStartMinnus(String date, int minunus) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MINUTE, minunus);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public boolean betweenStartTimeAndendTime() {
        return DateUtils.belongCalendar(DateUtils.getHourDate(todayNowTime.get()), DateUtils.getHourDate(getStartMinnus(todayEndTime.get(), -11)), DateUtils.getHourDate(todayEndTime.get()));
    }



}

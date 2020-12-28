package com.ymx.driver.ui.longrange.driving;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.lifecycle.Observer;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.databinding.ActivityLongRangDrivingBinding;
import com.ymx.driver.entity.app.RangDrivingDayChoose;
import com.ymx.driver.ui.longrange.driving.frament.RangeDrivingSelectTime;
import com.ymx.driver.ui.mine.Frament.TimePickDialogFragment;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.SwipeButton;
import com.ymx.driver.viewmodel.longrangdriving.LongRangeDrivingViewModel;




public class LongRangeDrivingActivity extends BaseActivity<ActivityLongRangDrivingBinding, LongRangeDrivingViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, LongRangeDrivingActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_long_rang_driving;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnSubmit.addOnSwipeCallback(new SwipeButton.Swipe() {
            @Override
            public void onButtonPress() {

            }

            @Override
            public void onSwipeCancel() {

            }

            @Override
            public void onSwipeConfirm() {
                int orderType = 0;
                if (viewModel.dayChooseItemViewModels != null && !viewModel.dayChooseItemViewModels.isEmpty()) {
                    for (int i = 0; i < viewModel.dayChooseItemViewModels.size(); i++) {
                        RangDrivingDayChoose rangDrivingDayChoose = viewModel.dayChooseItemViewModels.get(i).rangDrivingDayChoose.get();
                        if (rangDrivingDayChoose.isSelect()) {
                            orderType = dayType(rangDrivingDayChoose.getDay());
                            break;
                        }

                    }
                    if (viewModel.remoteState.get() == 0) {
                        viewModel.updateRemoteInfo(2, orderType, viewModel.startSelectTime.get(), viewModel.endSelectTime.get(), 1);
                    }


                }
            }
        });


    }

    @Override
    public void initData() {
        viewModel.getRemoteInfo();

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucSelect.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                binding.recycer.getAdapter().notifyDataSetChanged();
            }
        });


        viewModel.uc.ucImage.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.stationType.get() == 1) {
                    binding.turnImage.setImageDrawable(getDrawable(R.drawable.icon_putong));

                } else if (viewModel.stationType.get() == 2) {
                    binding.turnImage.setImageDrawable(getDrawable(R.drawable.icon_gaosu));
                }
            }
        });

        viewModel.uc.ucRangeDrivingSelectTimeDialog.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.startTime != null && !TextUtils.isEmpty(viewModel.startTime.get()) && viewModel.endTime != null && !TextUtils.isEmpty(viewModel.startTime.get())) {

                    Bundle bundle = new Bundle();

                    RangDrivingDayChoose rangDrivingDayChoose = null;
                    for (int i = 0; i < viewModel.dayChooseItemViewModels.size(); i++) {
                        rangDrivingDayChoose = viewModel.dayChooseItemViewModels.get(i).rangDrivingDayChoose.get();
                        if (rangDrivingDayChoose.isSelect()) {

                            break;
                        }

                    }
                    if (dayType(rangDrivingDayChoose.getDay()) == 1) {
                        if (DateUtils.belongCalendar(DateUtils.getHourDate(viewModel.todayNowTime.get()), DateUtils.getHourDate(viewModel.todayEndTime.get()))) {
                            UIUtils.showToast("当前时间不在运营时间范围内，运营时间为" + viewModel.startTime.get() + "-" + viewModel.endTime.get());
                            return;
                        }
                        if (viewModel.betweenStartTimeAndendTime()) {
                            UIUtils.showToast("当前时间不可选");
                            return;
                        } else {
                            bundle.putString(TimePickDialogFragment.SELECT_START_TIME,
                                    viewModel.todayDayTime.get());
                        }


                    } else {
                        bundle.putString(TimePickDialogFragment.SELECT_START_TIME,
                                viewModel.startTime.get());
                    }


                    bundle.putString(TimePickDialogFragment.SELECT_END_TIME,
                            viewModel.endTime.get());
                    RangeDrivingSelectTime.newInstance(bundle).show(getSupportFragmentManager(),
                            RangeDrivingSelectTime.class.getSimpleName());

                } else {

                    UIUtils.showToast(getString(R.string.net_errer));
                }


            }
        });

        viewModel.uc.ucButtonStatus.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (viewModel.remoteState != null) {
                    if (viewModel.remoteState.get() == 0) {
                        if (viewModel.startSelectTime != null && viewModel.startSelectTime.get() != null && viewModel.endSelectTime != null && viewModel.endSelectTime.get() != null) {
                            binding.btnSubmit.setVisibility(View.VISIBLE);
                            binding.btRl.setVisibility(View.GONE);
                        }

                        binding.btnSubmit.setText("滑动出车");
                    } else {

                        binding.btnSubmit.setVisibility(View.GONE);
                        binding.btRl.setVisibility(View.VISIBLE);

                    }

                }

            }
        });
        viewModel.uc.ucLockDrvier.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.lockDrvierState != null && viewModel.lockDrvierState.get() == 1) {
                   viewModel. updateRemoteInfo(5, 0, "", "", 0);
                } else if (viewModel.lockDrvierState != null && viewModel.lockDrvierState.get() == 0) {
                    new DefaultStyleDialog(activity)
                            .setBody("您确定要锁定吗？锁定后系统将不再为您匹配新的乘客")
                            .setNegativeText("取消")
                            .setPositiveText("确定锁定")
                            .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                @Override
                                public void negative(Dialog dialog) {
                                    dialog.dismiss();

                                }

                                @Override
                                public void positive(Dialog dialog) {
                                    dialog.dismiss();
                                    viewModel. updateRemoteInfo(4, 0, "", "", 0);
                                }
                            }).show();
                }

            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

    public int dayType(String day) {
        if (day.equals("今天")) {
            return 1;
        } else if (day.equals("明天")) {
            return 2;
        } else if (day.equals("后天")) {
            return 3;
        }
        return -1;
    }
}
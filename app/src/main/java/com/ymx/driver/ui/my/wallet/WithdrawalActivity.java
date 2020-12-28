package com.ymx.driver.ui.my.wallet;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.AcitivyWithdrawalBinding;
import com.ymx.driver.dialog.ConfirmWithdrawalDialog;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.CashierInputFilter;
import com.ymx.driver.viewmodel.mywallet.WithdrawalViewModel;

import org.greenrobot.eventbus.EventBus;

public class WithdrawalActivity extends BaseActivity<AcitivyWithdrawalBinding, WithdrawalViewModel> {


    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, WithdrawalActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.acitivy_withdrawal;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        InputFilter[] filters = {new CashierInputFilter()};
        binding.cashWithdrawalAmount.setFilters(filters);
    }

    @Override
    public void initData() {
        viewModel.getWithdrawInfo();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucAllMoney.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                StringBuffer sb = new StringBuffer();
                sb.append("本次可提现");
                sb.append(viewModel.balance.get());
                sb.append("元");
                viewModel.moneyLimit.set(sb.toString());


                binding.cashWithdrawalAmount.setText(String.valueOf(viewModel.balance.get()));
            }
        });

        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucConfirmWithdrawalSuccess.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
                WithdrawalListActivity.start(activity);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_WITHDRAWAL_DATA_CODE));

            }
        });
        viewModel.uc.ucGo.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                WithdrawalListActivity.start(activity);
            }
        });


        viewModel.uc.ucConfirmWithdrawal.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                if (viewModel.withdrawalState.get() != null && viewModel.withdrawalState.get()) {
                    String money = binding.cashWithdrawalAmount.getText().toString();
                    if (TextUtils.isEmpty(money)) {
                        UIUtils.showToast(getString(R.string.my_withdrawal_hiht));
                        return;
                    }

                    if (Double.parseDouble(money) > viewModel.balance.get()) {
                        UIUtils.showToast(getString(R.string.my_withdrawal_limit));
                        return;
                    }

                    new ConfirmWithdrawalDialog(activity, money, viewModel.withdrawFee.get())
                            .setNegativeText("取消")
                            .setPositiveText("确认")
                            .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                                @Override
                                public void negative(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void positive(Dialog dialog) {
                                    dialog.dismiss();
                                    viewModel.confirmWithdrawal(money);
                                }
                            }).show();

                } else {

                }


            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}

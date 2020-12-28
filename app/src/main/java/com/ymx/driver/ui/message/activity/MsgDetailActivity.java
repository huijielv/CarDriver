package com.ymx.driver.ui.message.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.ActivityMsgDetailBinding;
import com.ymx.driver.viewmodel.message.MsgDetailViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xuweihua
 * 2020/6/14
 */
public class MsgDetailActivity extends BaseActivity<ActivityMsgDetailBinding, MsgDetailViewModel> {
    public static final String MSG_ID = "msg_id";
    public static final String MSG_TITLE = "msg_title";

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, MsgDetailActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_enter_left, R.anim.slide_exit_right);
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_msg_detail;
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

    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            int msgId = getIntent().getIntExtra(MSG_ID, -1);
            String msgTitle = getIntent().getStringExtra(MSG_TITLE);
            viewModel.msgId.set(msgId);
            viewModel.msgTitle.set(msgTitle);

            viewModel.getMsgDetailList();
            viewModel.setMsgRead(msgId);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getMsgDetailList();
            }
        });

        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
    }

    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_MYMSG_REFRESH_DATA_CODE));
    }
}

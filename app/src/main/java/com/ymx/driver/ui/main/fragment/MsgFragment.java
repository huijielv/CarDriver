package com.ymx.driver.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FragmentNoticeBinding;
import com.ymx.driver.entity.app.MsgEntity;
import com.ymx.driver.ui.message.activity.MsgDetailActivity;
import com.ymx.driver.viewmodel.main.MsgViewModel;

import androidx.lifecycle.Observer;

/**
 * Created by xuweihua
 * 2020/5/4
 */
public class MsgFragment extends BaseFragment<FragmentNoticeBinding, MsgViewModel> {

    private String city;

    private String TAG = this.getClass().getSimpleName();

    public static MsgFragment newInstance() {
        return newInstance(null);
    }

    public static MsgFragment newInstance(Bundle bundle) {
        MsgFragment fragment = new MsgFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_notice;
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
        Log.i(TAG,"initView()");
    }

    @Override
    public void initData() {
        //viewModel.getMsgList();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucToMsgDetail.observe(this, new Observer<MsgEntity>() {
            @Override
            public void onChanged(MsgEntity entity) {
                if (entity == null) {
                    return;
                }
                MsgDetailActivity.start(activity, new Intent()
                        .putExtra(MsgDetailActivity.MSG_ID, entity.getId())
                        .putExtra(MsgDetailActivity.MSG_TITLE, entity.getDescribe()));
            }
        });
    }
}

package com.ymx.driver.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.databinding.DialogFragmentSystemNoticeBinding;
import com.ymx.driver.entity.SystemNoticeEntity;
import com.ymx.driver.ui.mine.activity.WebviewActivity;
import com.ymx.driver.viewmodel.main.SystemNoticeItemViewModel;
import com.ymx.driver.viewmodel.main.SystemNoticeViewModel;

import java.util.List;

public class SystemNoticeDialogFragment extends BaseDialogFragment<DialogFragmentSystemNoticeBinding, SystemNoticeViewModel> {
    public static final String SYSTEM_NOTICE_LIST = "system_notice_list";

    public static SystemNoticeDialogFragment newInstance() {
        return newInstance(null);
    }

    public static SystemNoticeDialogFragment newInstance(Bundle bundle) {
        SystemNoticeDialogFragment fragment = new SystemNoticeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.dialog_fragment_system_notice;
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
        binding.indicator.setViewPager(binding.viewPager);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            List<SystemNoticeEntity> list = (List<SystemNoticeEntity>) getArguments().getSerializable(SYSTEM_NOTICE_LIST);
            if (list == null || list.size() <= 0) {
                return;
            }
            for (SystemNoticeEntity entity : list) {
                viewModel.items.add(new SystemNoticeItemViewModel(viewModel, entity));
            }
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucDismiss.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });

        viewModel.uc.ucToSystemNoticeDetail.observe(this, new Observer<SystemNoticeEntity>() {
            @Override
            public void onChanged(SystemNoticeEntity entity) {
                if (entity == null || entity.getSkipType() == 0
                        || TextUtils.isEmpty(entity.getHrefUrl())) {
                    return;
                }
                WebviewActivity.start(activity, new Intent()
                        .putExtra(WebviewActivity.TITLE, entity.getTitle())
                        .putExtra(WebviewActivity.URL, entity.getHrefUrl()));
            }
        });
    }
}


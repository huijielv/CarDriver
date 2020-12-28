package com.ymx.driver.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityAboutBinding;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.ui.main.fragment.UpdateDialogFragment;
import com.ymx.driver.util.keyboardutil.KeyBoardEventBus;
import com.ymx.driver.viewmodel.mine.AboutViewModel;

/**
 * Created by wuwei
 * 2020/4/24
 * 佛祖保佑       永无BUG
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding, AboutViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, AboutActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_about;
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

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.ucUserTerms.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                WebviewActivity.start(activity, new Intent()
                        .putExtra(WebviewActivity.URL, " http://scapp.xysc16.com:5550/agreement/index.html?from=driver"));
            }
        });

        viewModel.uc.ucVersion.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                viewModel.update(getPackageName());
            }
        });

        viewModel.uc.ucUpdate.observe(this, new Observer<UpdateEntity>() {
            @Override
            public void onChanged(UpdateEntity updateEntity) {
                if (updateEntity.getUpgradeMode() == 0) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(UpdateDialogFragment.UPDATE_ENTITY, updateEntity);
                UpdateDialogFragment.newInstance(bundle).show(getSupportFragmentManager(),
                        UpdateDialogFragment.class.getSimpleName());
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
        KeyBoardEventBus.getDefault().unregister(activity);
    }
}

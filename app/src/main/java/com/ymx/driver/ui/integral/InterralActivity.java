package com.ymx.driver.ui.integral;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityIntergralBinding;
import com.ymx.driver.viewmodel.driverinterral.BaseInterralViewModel;


public class InterralActivity extends BaseActivity<ActivityIntergralBinding, BaseInterralViewModel> {

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, InterralActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_enter_left, R.anim.slide_exit_right);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onStart() {
        super.onStart();

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(statusBarDarkFont())
                .statusBarColor(R.color.rx_fff9c28f)
                .init();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_intergral;
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

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {
        viewModel.uc.close.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                Fragment mMainNavFragment = getSupportFragmentManager().findFragmentById(R.id.host_fragment);
                Fragment fragment = mMainNavFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                NavController nav = Navigation.findNavController(activity, R.id.host_fragment);
                if (fragment instanceof InterralFragment) {
                    AppManager.getAppManager().finishActivity(activity);

                } else {

                    ImmersionBar.with(activity)
                            .fitsSystemWindows(true)
                            .statusBarDarkFont(true)
                            .statusBarColor(R.color.rx_fff9c28f)
                            .init();
                    nav.navigateUp();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {


    }

    @Override
    protected void doSthIsExit() {

        viewModel.uc.close.call();
    }


}

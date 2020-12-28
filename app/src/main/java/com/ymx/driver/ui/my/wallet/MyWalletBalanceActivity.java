package com.ymx.driver.ui.my.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.databinding.ActivityMyWalletBalanceBinding;
import com.ymx.driver.ui.my.wallet.Frament.AllBanlanceFrament;
import com.ymx.driver.ui.my.wallet.Frament.ExpenditureBanlanceFrament;
import com.ymx.driver.ui.my.wallet.Frament.WalletBalanceFrament;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mywallet.MyWalletBalanceViewModel;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MyWalletBalanceActivity extends BaseActivity<ActivityMyWalletBalanceBinding, MyWalletBalanceViewModel> {

    public static final String MY_WALLET_BANLANCE = "banlance";

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_my_wallet_balance;
    }

    private int[] mainIndicatorTitleIds = {R.string.my_wallet_balance_all, R.string.my_wallet_balance_income, R.string.my_wallet_balance_expenditure};
    private List<Fragment> mainFragmentLists = new ArrayList<>();


    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    public static void start(Activity activity) {
        start(activity, null);
    }

    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, MyWalletBalanceActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }


    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mainFragmentLists.add(new AllBanlanceFrament());
        mainFragmentLists.add(new WalletBalanceFrament());
        mainFragmentLists.add(new ExpenditureBanlanceFrament());
        initMainViewPager();
        initMagicIndicator();
        binding.walletBalanceViewpager.setCurrentItem(0);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MY_WALLET_BANLANCE)) {
            viewModel.balacne.set(intent.getStringExtra(MY_WALLET_BANLANCE));
        }
    }

    @Override
    public void initViewObservable() {

        viewModel.uc.back.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsExit();
            }
        });

        viewModel.uc.goWithdrawal.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                WithdrawalActivity.start(activity);
            }
        });

    }


    private void initMainViewPager() {
        binding.walletBalanceViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mainFragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return mainFragmentLists.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == ((Fragment) obj).getView();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }

            @Override
            public long getItemId(int position) {
                return mainFragmentLists.get(position).hashCode();
            }


        });
    }

    private void initMagicIndicator() {
        binding.walletBalanceIndicator.setBackgroundColor(UIUtils.getColor(R.color.white));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mainIndicatorTitleIds == null ? 0 : mainIndicatorTitleIds.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mainIndicatorTitleIds[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setNormalColor(UIUtils.getColor(R.color.rx_ff333333));
                simplePagerTitleView.setSelectedColor(UIUtils.getColor(R.color.color_ffff8888));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        binding.walletBalanceViewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtils.dip2px(0));
                return indicator;
            }
        });
        binding.walletBalanceIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.walletBalanceIndicator, binding.walletBalanceViewpager);
    }


    @Override
    protected void doSthIsExit() {
        AppManager.getAppManager().finishActivity(activity);

    }
}

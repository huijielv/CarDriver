package com.ymx.driver.ui.trip.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.ymx.driver.R;
import com.ymx.driver.base.AppManager;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.ActivityTripOrderBinding;
import com.ymx.driver.ui.chartercar.CharterOrderDayFinishActivity;
import com.ymx.driver.ui.chartercar.CharterOrderDetailsActivity;
import com.ymx.driver.ui.chartercar.CharterOrderFinishOrderDetailsActivity;
import com.ymx.driver.ui.chartercar.CharterOrderWaitingActivity;
import com.ymx.driver.ui.longrange.driving.LongRangDrivingFinishDetails;
import com.ymx.driver.ui.longrange.driving.LongRangeDrivingDetails;
import com.ymx.driver.ui.longrange.driving.LongRangeFerryFinishDetails;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderFinishDetailsActivity;
import com.ymx.driver.ui.travel.activity.CarPoolDetailsActivity;
import com.ymx.driver.ui.travel.activity.GrabOrderActivity;
import com.ymx.driver.ui.travel.activity.PhoneOrderPayActivity;
import com.ymx.driver.ui.travel.activity.TravelActivity;
import com.ymx.driver.ui.travel.activity.TravelOrderDetailsActivity;
import com.ymx.driver.ui.travel.activity.TravelOrderDetailsFinshActivity;
import com.ymx.driver.ui.trip.activity.frament.FinishTripOrderFrament;
import com.ymx.driver.ui.trip.activity.frament.NoFinishTripOrderFrament;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.triporderlist.TripOrderListViewModel;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TripOrderListActivity extends BaseActivity<ActivityTripOrderBinding, TripOrderListViewModel> {
    private static final int[] mainIndicatorTitleIds = {R.string.not_finish_trip_order, R.string.finish_trip_order};
    private List<Fragment> mainFragmentLists = new ArrayList<>();
    private int selectIndex = 0;

    public static void start(Activity activity) {
        start(activity, null);
    }


    public static void start(Activity activity, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(activity, TripOrderListActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);

    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_trip_order;
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
        mainFragmentLists.add(new NoFinishTripOrderFrament());
        mainFragmentLists.add(new FinishTripOrderFrament());
        initMainViewPager();
        initMagicIndicator();
        binding.tripOrderViewpager.setCurrentItem(0);
    }

    private void initMainViewPager() {
        binding.tripOrderViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {


                return mainFragmentLists.get(position);
            }

            @Override
            public int getCount() {
                return mainFragmentLists.size();
            }
        });

        binding.tripOrderViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initMagicIndicator() {
        binding.tripOrderIndicator.setBackgroundColor(UIUtils.getColor(R.color.white));
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
                simplePagerTitleView.setNormalColor(UIUtils.getColor(R.color.rx_333333));
                simplePagerTitleView.setSelectedColor(UIUtils.getColor(R.color.rx_ff8008));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.tripOrderViewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setLineHeight(UIUtils.dip2px(2));
                indicator.setColors(UIUtils.getColor(R.color.rx_ff8008));
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtils.dip2px(38));
                return indicator;
            }
        });
        binding.tripOrderIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.tripOrderIndicator, binding.tripOrderViewpager);
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

        viewModel.uc.goTo.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                int driverState = viewModel.tripOrderListItemField.get().getDriverState();
                int channel = viewModel.tripOrderListItemField.get().getChannel();
                String orderId = viewModel.tripOrderListItemField.get().getOrderNo();
                int businessType = viewModel.tripOrderListItemField.get().getBusinessType();
                int categoryType = viewModel.tripOrderListItemField.get().getCategoryType();


                if (selectIndex == 0) {
                    if (businessType == 6 || businessType == 7 || businessType == 8) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                        LongRangeDrivingDetails.start(activity, intent);
                    } else if (businessType == 9) {
                        if (driverState == TripOrderListViewModel.DRIVER_STATE_ROADING || driverState == TripOrderListViewModel.DRIVER_STATE_TO_PASSENGERS) {
                            Intent intent = new Intent();
                            intent.putExtra(CharterOrderDetailsActivity.ORDERI_ID, orderId);
                            CharterOrderDetailsActivity.start(activity, intent);
                        } else if (driverState == 5) {
                            Intent intent = new Intent();
                            intent.putExtra(CharterOrderDetailsActivity.ORDERI_ID, orderId);
                            CharterOrderWaitingActivity.start(activity, intent);
                        } else if (driverState == 6) {
                            Intent intent = new Intent();
                            intent.putExtra(CharterOrderDetailsActivity.ORDERI_ID, orderId);
                            CharterOrderDayFinishActivity.start(activity, intent);
                        }
                    } else if (businessType == 11) {
                        Intent intent = new Intent();
                        intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, orderId);
                        TransferStationTripOrderDetailsActivity.start(activity, intent);
                    } else {

                        if (categoryType == 2) {
                            Intent intent = new Intent();
                            intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                            CarPoolDetailsActivity.start(activity, intent);
                        } else {
                            if (driverState == TripOrderListViewModel.DRIVER_ORDER_N0_CONFIRN) {
                                Intent intent = new Intent();
                                intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                                GrabOrderActivity.start(activity, intent);

                            } else if (driverState == TripOrderListViewModel.DRIVER_STATE_TO_START || driverState == TripOrderListViewModel.DRIVER_STATE_READY_TO_GO || driverState == TripOrderListViewModel.DRIVER_STATE_ROADING || driverState == TripOrderListViewModel.DRIVER_STATE_TO_PASSENGERS) {
                                Intent intent = new Intent();
                                intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                                intent.putExtra(TravelActivity.CATEGORY_TYPE, String.valueOf(categoryType));
                                TravelActivity.start(activity, intent);
                            } else if (driverState == TripOrderListViewModel.DRIVER_STATE_CONFIRM_COST || driverState == TripOrderListViewModel.DRIVER_STATE_TO_PAY) {

                                if (channel == 5) {
                                    if (driverState == TripOrderListViewModel.DRIVER_STATE_CONFIRM_COST) {
                                        Intent intent = new Intent();
                                        intent.putExtra(TravelOrderDetailsActivity.ORDERI_ID, orderId);
                                        intent.putExtra(TravelActivity.CATEGORY_TYPE, categoryType);
                                        intent.putExtra(TravelOrderDetailsActivity.ACTION_TYPE, driverState);
                                        TravelOrderDetailsActivity.start(activity, intent);
                                    } else if (driverState == TripOrderListViewModel.DRIVER_STATE_TO_PAY) {
                                        Intent intent = new Intent();
                                        intent.putExtra(PhoneOrderPayActivity.TYPE, 1);
                                        intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                                        PhoneOrderPayActivity.start(activity, intent);
                                    }

                                } else  {
                                    Intent intent = new Intent();
                                    intent.putExtra(TravelOrderDetailsActivity.ORDERI_ID, orderId);
                                    intent.putExtra(TravelActivity.CATEGORY_TYPE, String.valueOf(categoryType));
                                    intent.putExtra(TravelOrderDetailsActivity.ACTION_TYPE, driverState);
                                    TravelOrderDetailsActivity.start(activity, intent);
                                }


                            }
                        }


                    }

                } else if (selectIndex == 1) {
                    //完成订单的状态界面 跳转

                    if (businessType == 6 || businessType == 7) {
                        Intent intent = new Intent();
                        intent.putExtra(TravelActivity.ORDERI_ID, orderId);
                        LongRangDrivingFinishDetails.start(activity, intent);
                    } else if (businessType == 8) {
                        Intent intent = new Intent();
                        intent.putExtra(LongRangeFerryFinishDetails.ORDERI_ID, orderId);
                        LongRangeFerryFinishDetails.start(activity, intent);
                    } else if (businessType == 9) {

                        Intent intent = new Intent();
                        intent.putExtra(CharterOrderDetailsActivity.ORDERI_ID, orderId);
                        CharterOrderFinishOrderDetailsActivity.start(activity, intent);
                    } else if (businessType == 11) {
                        Intent intent = new Intent();
                        intent.putExtra(TransferStationTripOrderFinishDetailsActivity.ORDERI_ID, orderId);
                        TransferStationTripOrderFinishDetailsActivity.start(activity, intent);
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(TravelOrderDetailsFinshActivity.ORDERI_ID, orderId);
                        intent.putExtra(TravelOrderDetailsFinshActivity.ACTION_TYPE, driverState);
                        TravelOrderDetailsFinshActivity.start(activity, intent);
                    }


                }


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
        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_HOME_FRAGMENT_DATA_CODE));
    }
}

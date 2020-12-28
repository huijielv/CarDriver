package com.ymx.driver.ui.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.lifecycle.Observer;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.base.YmxCache;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.FragmentMineBinding;
import com.ymx.driver.entity.app.FindCarpoolingSiteResultEntity;
import com.ymx.driver.entity.app.GetCharteredInfoEntity;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.ui.chartercar.CharterDrivingActivity;
import com.ymx.driver.ui.chartercar.CharterOrderDayFinishActivity;
import com.ymx.driver.ui.chartercar.CharterOrderDetailsActivity;
import com.ymx.driver.ui.launch.dialog.PermissionDeniedDialog;
import com.ymx.driver.ui.longrange.driving.LongRangeDrivingActivity;
import com.ymx.driver.ui.longrange.driving.LongRangeFerryDrvingDetails;
import com.ymx.driver.ui.transportsite.TransferStationTripOrderDetailsActivity;
import com.ymx.driver.ui.transportsite.TransportSiteUpdateCarStateActivity;
import com.ymx.driver.viewmodel.main.MineViewModel;
import com.ymx.driver.zxinglibrary.android.CaptureActivity;

/**
 * Created by xuweihua
 * 2020/5/4
 */
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> {

    private PermissionDeniedDialog permissionDeniedDialog;

    public static MineFragment newInstance() {
        return newInstance(null);
    }

    public static MineFragment newInstance(Bundle bundle) {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_mine;
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
        viewModel.uc.ucItemClick.observe(this, new Observer<Class<?>>() {
            @Override
            public void onChanged(Class<?> clazz) {
                Intent intent = new Intent();
                intent.setClass(activity, clazz);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_enter_left, R.anim.slide_exit_right);
            }
        });
        viewModel.uc.ucGo.observe(this, new Observer<RemoteInfoEntity>() {
            @Override
            public void onChanged(RemoteInfoEntity remoteInfoEntity) {

                if (remoteInfoEntity.getDriverType() == 2) {
                    LongRangeDrivingActivity.start(activity);
                } else if (remoteInfoEntity.getDriverType() == 3) {
                    LongRangeFerryDrvingDetails.start(activity);
                }

            }
        });

        viewModel.uc.ucGoScan.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_CAMERA, true,
                        Manifest.permission.CAMERA);
            }
        });
        viewModel.uc.ucGoCharter.observe(this, new Observer<GetCharteredInfoEntity>() {
            @Override
            public void onChanged(GetCharteredInfoEntity getCharteredInfoEntity) {
                if (getCharteredInfoEntity.getCarState() == 3) {
                    Intent intent = new Intent();
                    intent.putExtra(CharterOrderDetailsActivity.ORDERI_ID, getCharteredInfoEntity.getOrderNo());
                    CharterOrderDayFinishActivity.start(activity, intent);
                } else {
                    CharterDrivingActivity.start(activity);
                }

            }
        });
        viewModel.uc.ucGoTransferStationSite.observe(this, new Observer<FindCarpoolingSiteResultEntity>() {
            @Override
            public void onChanged(FindCarpoolingSiteResultEntity findCarpoolingSiteResultEntity) {
                if (!TextUtils.isEmpty(findCarpoolingSiteResultEntity.getOrderNo())) {
                    Intent intent = new Intent();
                    intent.putExtra(TransferStationTripOrderDetailsActivity.ORDER_NO, findCarpoolingSiteResultEntity.getOrderNo());
                    TransferStationTripOrderDetailsActivity.start(activity, intent);
                } else {
                    TransportSiteUpdateCarStateActivity.start(activity);
                }

            }
        });

    }


    @Override
    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_CAMERA) {
//            ScanActivity.start(activity);

            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void showPermissionDialog(int requestCode) {
        super.showPermissionDialog(requestCode);

        dismissDialog();
        permissionDeniedDialog = new PermissionDeniedDialog(activity)
                .setOnDialogListener(new PermissionDeniedDialog.Listener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            startActivity(intent);
                        } catch (Exception e) {

                        }

                    }
                });
        permissionDeniedDialog.show();
    }


    private void dismissDialog() {
        if (!isDetached()) {
            if (permissionDeniedDialog != null) {
                permissionDeniedDialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.driverIntegral.set(YmxCache.getDriverIntegral());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }
}

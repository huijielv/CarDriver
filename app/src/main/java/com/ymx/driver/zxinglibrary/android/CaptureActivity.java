package com.ymx.driver.zxinglibrary.android;

import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.Observer;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseActivity;
import com.ymx.driver.base.YmxApp;
import com.ymx.driver.config.QRCodeConfig;
import com.ymx.driver.databinding.ActivityCaptureBinding;
import com.ymx.driver.entity.BaseQRCodeEntity;
import com.ymx.driver.entity.app.OrderCirculationNoticeEntity;
import com.ymx.driver.ui.longrange.driving.OrderCirculationDialogActivity;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mine.ScanViewModel;
import com.ymx.driver.zxinglibrary.ZXingUtils;
import com.ymx.driver.zxinglibrary.bean.ZxingConfig;
import com.ymx.driver.zxinglibrary.camera.CameraManager;
import com.ymx.driver.zxinglibrary.common.Constant;
import com.ymx.driver.zxinglibrary.view.ViewfinderView;


import java.io.IOException;


public class CaptureActivity extends BaseActivity<ActivityCaptureBinding, ScanViewModel> implements SurfaceHolder.Callback, View.OnClickListener {


    private static final String TAG = CaptureActivity.class.getSimpleName();
    public ZxingConfig config;
    private SurfaceView previewView;
    private ViewfinderView viewfinderView;
    private boolean flag = true;
    private TextView flashLightTv;
    private LinearLayoutCompat flashLightLayout;
    private LinearLayoutCompat albumLayout;
    private LinearLayoutCompat bottomLayout;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private SurfaceHolder surfaceHolder;


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*先获取配置信息*/
        try {
            config = (ZxingConfig) getIntent().getExtras().get(Constant.INTENT_ZXING_CONFIG);
        } catch (Exception e) {


        }

        if (config == null) {
            config = new ZxingConfig();
        }


        initView();

        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(config.isPlayBeep());
        beepManager.setVibrate(config.isShake());


    }


    private void initView() {
        previewView = findViewById(R.id.preview_view);
        previewView.setOnClickListener(this);
        viewfinderView = findViewById(R.id.viewfinder_view);
        viewfinderView.setZxingConfig(config);
        flashLightTv = findViewById(R.id.flashLightTv);
        flashLightLayout = findViewById(R.id.flashLightLayout);
        flashLightLayout.setOnClickListener(this);
        albumLayout = findViewById(R.id.albumLayout);
        albumLayout.setOnClickListener(this);
        bottomLayout = findViewById(R.id.bottomLayout);

        /*有闪光灯就显示手电筒按钮  否则不显示*/
        if (isSupportCameraLedFlash(getPackageManager())) {
            flashLightLayout.setVisibility(View.VISIBLE);
        } else {
            flashLightLayout.setVisibility(View.GONE);
        }

    }


    /**
     * @param pm
     * @return 是否有闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param flashState 切换闪光灯图片
     */
    public void switchFlashImg(int flashState) {

        if (flashState == Constant.FLASH_OPEN) {

            flashLightTv.setText(R.string.close_flash);
        } else {

            flashLightTv.setText(R.string.open_flash);
        }

    }

    /**
     * @param rawResult 返回的扫描结果
     */
    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        finishSuccess(rawResult.getText());
    }


    private void switchVisibility(View view, boolean b) {
        if (b) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication(), config);

        viewfinderView.setCameraManager(cameraManager);
        handler = null;

        surfaceHolder = previewView.getHolder();
        if (hasSurface) {

            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);

        } catch (RuntimeException e) {


        }
    }


    @Override
    protected void onPause() {


        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();

        if (!hasSurface) {

            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        viewfinderView.stopAnimator();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.flashLightLayout) {
            /*切换闪光灯*/
            cameraManager.switchFlashLight(handler);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK) {
            showLoading();
            final Uri uri = data.getData();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmapChoose = ZXingUtils.decodeUriAsBitmap(activity, uri);
                    if (bitmapChoose != null) {
                        final String decodeQRCodeFromBitmap = ZXingUtils.syncDecodeQRCode(bitmapChoose);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(decodeQRCodeFromBitmap)) {
                                    finishFailed(getString(R.string.qrcode_link_not_exit_or_expired));
                                } else {
                                    finishSuccess(decodeQRCodeFromBitmap);
                                }
                            }
                        });
                    } else {
                        finishFailed(getString(R.string.qrcode_link_not_exit_or_expired));
                    }
                }
            }).start();


        }
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_capture;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


    private void finishSuccess(String result) {
        if (flag) {
            flag = false;
            try {
                if (!TextUtils.isEmpty(result)) {
                    String data = new String(Base64.decode(result, Base64.NO_WRAP));

                    BaseQRCodeEntity baseQRCodeEntity = new Gson().fromJson(data, BaseQRCodeEntity.class);
                    if (TextUtils.equals(QRCodeConfig.QRCODE,
                            baseQRCodeEntity.getCode())) {
                        BaseQRCodeEntity<OrderCirculationNoticeEntity> orderCirculationNoticeEntitye = BaseQRCodeEntity.fromJson(data, OrderCirculationNoticeEntity.class);
                        viewModel.orderCirculation.set(orderCirculationNoticeEntitye.getData());
                        if (!TextUtils.isEmpty(orderCirculationNoticeEntitye.getData().getOrderNo())) {

                            viewModel.scanCode(orderCirculationNoticeEntitye.getData().getOrderNo());
                        } else {
                            finishFailed(getString(R.string.qrcode_link_not_exit_or_expired));
                        }
                    }
                } else {
                    finishFailed(getString(R.string.qrcode_link_not_exit_or_expired));
                }
            } catch (Exception e) {
                finishFailed(getString(R.string.qrcode_link_not_exit_or_expired));
            }
        }
    }

    private void finishFailed(String errorMsg) {
        UIUtils.showToast(errorMsg);
        dismissLoading();
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
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    public void initViewObservable() {

        viewModel.uc.ucBack.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish();
            }
        });

        viewModel.uc.ucOpenAlbum.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, Constant.REQUEST_IMAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        viewModel.uc.ucSuccess.observe(this, new Observer<OrderCirculationNoticeEntity>() {
            @Override
            public void onChanged(OrderCirculationNoticeEntity orderCirculationNoticeEntity) {


                Intent intentNotice = new Intent();
                intentNotice.putExtra(OrderCirculationDialogActivity.NOTICE, orderCirculationNoticeEntity);
                OrderCirculationDialogActivity.start(YmxApp.getInstance(), intentNotice);
                finish();


            }
        });

        viewModel.uc.ucFaile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                finish();
                UIUtils.showToast(message);
            }
        });
    }
}

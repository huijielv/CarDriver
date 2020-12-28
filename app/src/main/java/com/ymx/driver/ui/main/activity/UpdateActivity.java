package com.ymx.driver.ui.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.ymx.driver.R;
import com.ymx.driver.base.DefaultStyleDialog;
import com.ymx.driver.config.PermissionConfig;
import com.ymx.driver.databinding.DialogFragmentUpdateBinding;
import com.ymx.driver.entity.app.UpdateEntity;
import com.ymx.driver.util.DeviceUtil;
import com.ymx.driver.util.DownloadUtil;
import com.ymx.driver.util.FileUtil;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.viewmodel.mine.UpdateViewModel;

import java.io.File;

import io.reactivex.functions.Consumer;
import okhttp3.HttpUrl;

public class UpdateActivity extends RxFragmentActivity {

    private DialogFragmentUpdateBinding binding;
    private UpdateViewModel viewModel;
    public static final String UPDATE_ENTITY = "update_entity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateEntity updateEntity = (UpdateEntity) getIntent().getSerializableExtra(UPDATE_ENTITY);
        viewModel = ViewModelProviders.of(this).get(UpdateViewModel.class);
        viewModel.entity.set(updateEntity);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_fragment_update, null, false);
        binding.setViewModel(viewModel);
        setContentView(binding.getRoot());
        initViewObservable();
    }

    public void initViewObservable() {
        viewModel.uc.ucNext.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                finish();
            }
        });

        viewModel.uc.ucUpdate.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                requestPermission(PermissionConfig.PC_UPDATE, true,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
    }


    protected void permissionGranted(int requestCode) {
        if (requestCode == PermissionConfig.PC_UPDATE) {
            if (viewModel.entity.get() == null
                    || TextUtils.isEmpty(viewModel.entity.get().getFileUrl())) {
                return;
            }
            File file = FileUtil.createFile(UIUtils.getContext(),
                    FileUtil.TYPE_DOWNLOAD, DownloadUtil.getNameFromUrl(viewModel.entity.get().getFileUrl()), ".apk");
            if (file.exists() && TextUtils.equals(viewModel.entity.get().getFileMd5(),
                    DeviceUtil.getMD5OfFile(file))) {
                installApk(file);
            } else {
                download(viewModel.entity.get().getFileUrl());
            }
        }
    }


    protected void showPermissionDialog(int requestCode) {

        new DefaultStyleDialog(this)
                .setTitle(getString(R.string.external_storage_permission_denied_title))
                .setBody(getString(R.string.external_storage_permission_denied_body))
                .setNegativeText(getString(R.string.external_storage_permission_denied_nega))
                .setPositiveText(getString(R.string.external_storage_permission_denied_posi))
                .setOnDialogListener(new DefaultStyleDialog.DialogListener() {
                    @Override
                    public void negative(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void positive(Dialog dialog) {
                        dialog.dismiss();
                        startAppSettings();
                    }
                }).show();
    }

    private void download(String url) {
        if (TextUtils.isEmpty(url) || HttpUrl.parse(url) == null) {
            UIUtils.showToast(getString(R.string.update_url_error));
            return;
        }
        DownloadUtil.get().download(url, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }
                        finish();
                        installApk(file);
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.positiveText.setEnabled(false);
                        binding.progress.setProgress(progress);
                    }
                });
            }

            @Override
            public void onDownloadFailed(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }
                        finish();
                    }
                });
            }
        });
    }

    public void installApk(File file) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplication().getPackageName() + ".fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }


    public void startAppSettings() {
        if (isDestroyed()) {
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    public void requestPermission(final int requestCode, final boolean showDialog, String... permissions) {
        try {
            RxPermissions rxPermissions = new RxPermissions(UpdateActivity.this);
            rxPermissions
                    .request(permissions)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                permissionGranted(requestCode);
                            } else {
                                if (showDialog) {
                                    showPermissionDialog(requestCode);
                                } else {
                                    permissionDenied(requestCode);
                                }
                            }
                            permissionGrantedOrDenineCanDo(requestCode);
                        }
                    });
        } catch (Exception e) {

        }
    }


    /**
     * 权限申请失败执行
     *
     * @param requestCode
     */
    protected void permissionDenied(int requestCode) {

    }

    /**
     * 权限申请成功或者失败都要执行
     */
    protected void permissionGrantedOrDenineCanDo(int requestCode) {

    }

}

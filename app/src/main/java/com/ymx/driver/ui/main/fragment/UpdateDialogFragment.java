package com.ymx.driver.ui.main.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
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

import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import okhttp3.HttpUrl;

/**
 * Created by wuwei
 * 2020/4/22
 * 佛祖保佑       永无BUG
 */
public class UpdateDialogFragment extends BaseDialogFragment<DialogFragmentUpdateBinding, UpdateViewModel> {
    public static final String UPDATE_ENTITY = "update_entity";

    public static UpdateDialogFragment newInstance() {
        return newInstance(null);
    }

    public static UpdateDialogFragment newInstance(Bundle bundle) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.dialog_fragment_update;
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
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setCancelable(false);
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            UpdateEntity updateEntity = (UpdateEntity) getArguments().getSerializable(UPDATE_ENTITY);
            viewModel.entity.set(updateEntity);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucNext.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
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

    @Override
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

    @Override
    protected void showPermissionDialog(int requestCode) {
        super.showPermissionDialog(requestCode);
        new DefaultStyleDialog(activity)
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
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        dismiss();
                        installApk(file);
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.positiveText.setEnabled(false);
                        binding.progress.setProgress(progress);
                    }
                });
            }

            @Override
            public void onDownloadFailed(String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        dismiss();
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
                Uri contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}

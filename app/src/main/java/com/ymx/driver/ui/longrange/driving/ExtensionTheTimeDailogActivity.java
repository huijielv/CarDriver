package com.ymx.driver.ui.longrange.driving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.ActivityExtensionTheTimeBinding;
import com.ymx.driver.entity.app.RemoteInfoEntity;
import com.ymx.driver.entity.app.UpdateRemoteInfoBodyEntity;
import com.ymx.driver.http.RetrofitFactory;
import com.ymx.driver.http.TFunc;
import com.ymx.driver.http.TObserver;

import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ExtensionTheTimeDailogActivity extends Activity {
    private ActivityExtensionTheTimeBinding binding;
    private LoadingDialog loadingDialog;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ExtensionTheTimeDailogActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.activity_extension_the_time, null, false);
        setContentView(binding.getRoot());
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRemoteInfo(3, 0, "", "", 0);
            }
        });

    }


    public void updateRemoteInfo(int opType, int day, String staTime, String eTime, int state) {
        RetrofitFactory.sApiService.updateRemoteInfo(new UpdateRemoteInfoBodyEntity(opType, day, staTime, eTime, state))
                .map(new TFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<RemoteInfoEntity>() {
                    @Override
                    protected void onRequestStart() {
                        loadingDialog.show();
                    }

                    @Override
                    protected void onRequestEnd() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    protected void onSuccees(RemoteInfoEntity remoteInfo) {

                        UIUtils.showToast("延时成功");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_CODE_TIME_EXPIRED));
                        finish();

                    }

                    @Override
                    protected void onFailure(String message) {
                        UIUtils.showToast(message);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}

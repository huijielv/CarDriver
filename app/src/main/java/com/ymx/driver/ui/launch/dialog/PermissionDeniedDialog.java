package com.ymx.driver.ui.launch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.databinding.DialogPermissionDeniedBinding;
import com.ymx.driver.util.UIUtils;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public class PermissionDeniedDialog extends Dialog {
    private DialogPermissionDeniedBinding binding;
    private Listener listener;

    public PermissionDeniedDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setBackgroundColor(UIUtils.getColor(R.color.rx_transparent));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_permission_denied, null, false);
        setContentView(binding.getRoot());

        binding.setDialog(this);

        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = binding.getRoot().getHeight();
                window.setAttributes(lp);
            }
        });
    }

    public BindingCommand negative = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.negative(PermissionDeniedDialog.this);
            }
        }
    });

    public BindingCommand positive = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.positive(PermissionDeniedDialog.this);
            }
        }
    });

    public PermissionDeniedDialog setOnDialogListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public interface Listener {
        void negative(Dialog dialog);

        void positive(Dialog dialog);
    }
}

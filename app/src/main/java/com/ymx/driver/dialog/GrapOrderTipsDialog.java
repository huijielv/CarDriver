package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;


import com.ymx.driver.databinding.GrapOrderDialogHihtBinding;
import com.ymx.driver.util.UIUtils;

public class GrapOrderTipsDialog extends Dialog {

    public GrapOrderDialogHihtBinding binding;
    private String textContent;


    public GrapOrderTipsDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

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
                R.layout.grap_order_dialog_hiht, null, false);
        setContentView(binding.getRoot());

        binding.setDialog(this);
        if (!TextUtils.isEmpty(textContent)) {
            binding.tvInfoDialogMsg.setText(textContent);
        }


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

    public GrapOrderTipsDialog setContxtText(String textContent) {
        this.textContent = textContent;
        return this;
    }

}

package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import com.ymx.driver.R;
import com.ymx.driver.databinding.DialogSafetyTipsBinding;
import com.ymx.driver.util.UIUtils;

public class SafetyTipsDialog extends Dialog {
    private DialogSafetyTipsBinding safetyTipsBinding;

    public SafetyTipsDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
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
        safetyTipsBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_safety_tips, null, false);
        setContentView(safetyTipsBinding.getRoot());
        safetyTipsBinding.positiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}

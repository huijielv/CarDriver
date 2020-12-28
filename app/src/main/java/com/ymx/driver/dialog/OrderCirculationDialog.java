package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.ymx.driver.R;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.DialogOrderCirculationBinding;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.VerificationCodeView;

import org.greenrobot.eventbus.EventBus;

public class OrderCirculationDialog extends Dialog implements VerificationCodeView.OnCodeFinishListener {

    public DialogOrderCirculationBinding binding;
    private String title;
    private String body;
    private String negativeText;
    private String positiveText;
    private OrderCirculationDialog.DialogListener listener;

    public OrderCirculationDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_order_circulation, null, false);
        setContentView(binding.getRoot());

        binding.setDialog(this);

        binding.title.setText(title);
        binding.body.setText(body);
        binding.verificationcodeview.setOnCodeFinishListener(this);
        binding.negativeText.setText(negativeText);
        binding.positiveText.setText(positiveText);

        if (TextUtils.isEmpty(title)) {
            binding.title.setVisibility(View.GONE);
        } else {
            binding.title.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(negativeText)) {
            binding.negativeText.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
        } else {
            binding.negativeText.setVisibility(View.VISIBLE);
            binding.line.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(positiveText)) {
            binding.positiveText.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
        } else {
            binding.positiveText.setVisibility(View.VISIBLE);
            binding.line.setVisibility(View.VISIBLE);
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

    public BindingCommand negative = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.negative(OrderCirculationDialog.this);
            }
        }
    });

    public BindingCommand positive = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.positive(OrderCirculationDialog.this);
            }
        }
    });

    public BindingCommand qcode = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.orderqrcode(OrderCirculationDialog.this);
            }
        }
    });



    public OrderCirculationDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public OrderCirculationDialog setBody(String body) {
        this.body = body;
        return this;
    }

    public OrderCirculationDialog setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public OrderCirculationDialog setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public OrderCirculationDialog setOnDialogListener(OrderCirculationDialog.DialogListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onTextChange(View view, String content) {

    }

    @Override
    public void onComplete(View view, String content) {
        if (binding.verificationcodeview == view) {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_DRIVER_ID_CODE, content));

        }
    }


    public interface DialogListener {
        void negative(Dialog dialog);

        void positive(Dialog dialog);

        void orderqrcode(Dialog dialog);
    }
}

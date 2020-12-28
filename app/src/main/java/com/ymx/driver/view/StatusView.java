package com.ymx.driver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.ymx.driver.R;
import com.ymx.driver.databinding.StatusViewBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;

public class StatusView extends FrameLayout {
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_EMPTY = 1;
    public static final int STATUS_ERROR = 2;

    private Context context;
    private StatusViewBinding binding;

    private int emptyImg;
    private String emptyTip;
    private int errorImg;
    private String errorTip;

    public ObservableInt status = new ObservableInt(STATUS_NORMAL);

    public StatusView(@NonNull Context context) {
        this(context, null);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.status_view,
                this, true);
        binding.setStatusView(this);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatusView,
                defStyleAttr, 0);
        emptyImg = typedArray.getResourceId(R.styleable.StatusView_empty_img, 0);
        emptyTip = typedArray.getString(R.styleable.StatusView_empty_tip);
        errorImg = typedArray.getResourceId(R.styleable.StatusView_error_img, 0);
        errorTip = typedArray.getString(R.styleable.StatusView_error_tip);
        status.set(typedArray.getInteger(R.styleable.StatusView_status, STATUS_NORMAL));
        typedArray.recycle();

        if (emptyImg != 0) {
            binding.emptyImg.setImageResource(emptyImg);
        }
        binding.emptyTip.setText(emptyTip);
    }

    public void setStatus(int status) {
        this.status.set(status);
    }
}

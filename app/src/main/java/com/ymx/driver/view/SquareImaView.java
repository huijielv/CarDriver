package com.ymx.driver.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class SquareImaView extends ImageView {
    public SquareImaView(Context context) {
        super(context);
    }

    public SquareImaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

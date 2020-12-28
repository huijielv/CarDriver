package com.ymx.driver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ymx.driver.R;
import com.ymx.driver.util.UIUtils;

import androidx.annotation.Nullable;

/**
 * Created by wuwei
 * 2020/6/5
 * 佛祖保佑       永无BUG
 */
public class PercentCircleView extends View {

    private Paint circlePaint = new Paint();
    private int percent = 0;
    private RectF rectF = null;
    private int wh;
    private int circleWidth = UIUtils.dip2px(2);
    private Rect textRect = new Rect();

    public PercentCircleView(Context context) {
        this(context, null);
    }

    public PercentCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlePaint.setAntiAlias(true);
        circlePaint.getTextBounds(percent + "%", 0, 1, textRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        wh = Math.min(width, height);
        setMeasuredDimension(wh, wh);
        rectF = new RectF(1.0f * circleWidth / 2 + 0.5f, 1.0f * circleWidth / 2 + 0.5f,
                1.0f * wh - circleWidth / 2 - 0.5f, 1.0f * wh - circleWidth / 2 - 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setColor(UIUtils.getColor(R.color.rx_ffffff));
        canvas.drawArc(rectF, 0, 360, false, circlePaint);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(UIUtils.getColor(R.color.rx_1aeb834a));
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), 1.0f * wh / 2, circlePaint);

        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setColor(UIUtils.getColor(R.color.rx_eb834a));
        canvas.drawArc(rectF, -90, 360 * (percent * 1.0f / 100), false, circlePaint);

        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setStrokeWidth(0);
        circlePaint.setTextSize(UIUtils.sp2px(14));
        canvas.drawText(percent == 0 ? getContext().getString(R.string.download) : percent + "%", rectF.centerX(),
                rectF.centerY() + (textRect.bottom - textRect.top), circlePaint);
    }

    public void setProgress(int progress) {
        percent = progress;
        if (percent < 0)
            percent = 0;
        if (percent > 100)
            percent = 100;
        invalidate();
    }

}

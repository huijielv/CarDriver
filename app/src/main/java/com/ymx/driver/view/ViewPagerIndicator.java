package com.ymx.driver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ymx.driver.R;
import com.ymx.driver.util.UIUtils;

public class ViewPagerIndicator  extends View {

    //点的半径
    private int mDotRadius;
    //点与点的间隔
    private int mDotGap;


    private ViewPager mViewPager;
    private Paint mDotPaint;

    private int mPosition;
    private float mPositionOffset;

    //不动点的颜色
    private int mNormalColor;
    //动点颜色
    private int mSelectedColor;
    private boolean mCanSmooth;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mNormalColor = typedArray.getColor(R.styleable.ViewPagerIndicator_normal_color, UIUtils.getColor(R.color.rx_ffffff));
        mSelectedColor = typedArray.getColor(R.styleable.ViewPagerIndicator_select_color, UIUtils.getColor(R.color.rx_ffffff));
        mDotRadius = typedArray.getDimensionPixelSize(R.styleable.ViewPagerIndicator_dot_radius, -1);
        mDotGap = typedArray.getDimensionPixelSize(R.styleable.ViewPagerIndicator_dot_gap, -1);
        mCanSmooth = typedArray.getBoolean(R.styleable.ViewPagerIndicator_can_smooth, false);

        typedArray.recycle();
    }

    /**
     * 测量CirclePageIndicator的高宽，不去使用在布局中的配置的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            int count = mViewPager instanceof AutoBannerView
                    ? ((AutoBannerView) mViewPager).getRealAdapter().getCount()
                    : mViewPager.getAdapter().getCount();
            //宽度 = 点的直径 * 点的个数 + 点与点间隔 * (点的个数 - 1)
            int width = 2 * mDotRadius * count + (count - 1) * mDotGap;
            //高度 = 点的直径
            int height = 2 * mDotRadius;
            setMeasuredDimension(width, height);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (mViewPager instanceof AutoBannerView) {
            ((AutoBannerView) mViewPager).setOnPagerSelectedListener(new AutoBannerView.OnPagerSelectedListener() {
                @Override
                public void onPagerItemSelected(int position) {
                    if (!mCanSmooth) {
                        mPosition = position;
                        invalidate();
                    }
                }
            });
        } else {
            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mCanSmooth) {
                mPosition = position;
                mPositionOffset = positionOffset;
                invalidate();
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (!mCanSmooth) {
                mPosition = position;
                invalidate();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            //点与点之间圆心的距离
            int dotDistance = mDotGap + 2 * mDotRadius;
            int count = mViewPager instanceof AutoBannerView
                    ? ((AutoBannerView) mViewPager).getRealAdapter().getCount()
                    : mViewPager.getAdapter().getCount();
            //循环遍历不动点
            for (int i = 0; i < count; i++) {
                float cx = mDotRadius + i * dotDistance;
                float cy = mDotRadius;
                mDotPaint.setColor(mNormalColor);
                canvas.drawCircle(cx, cy, mDotRadius, mDotPaint);
            }
            //绘制动点
            mDotPaint.setColor(mSelectedColor);
            //计算动点x轴的位置
            float mMoveCx = mDotRadius + dotDistance * mPositionOffset + mPosition * dotDistance;
            float mMoveCy = mDotRadius;
            canvas.drawCircle(mMoveCx, mMoveCy, mDotRadius, mDotPaint);
        }
    }
}

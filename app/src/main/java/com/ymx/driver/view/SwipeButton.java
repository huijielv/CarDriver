package com.ymx.driver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ymx.driver.R;
import com.ymx.driver.util.UIUtils;

public class SwipeButton extends Button {

    private int gradientColor1;
    private int gradientColor2;
    private int gradientColor2Width;
    private int gradientColor3;
    private int afterConfirmationBackground;
    private double threshold;
    @Nullable
    private String buttonPressText;
    @Nullable
    private String actionConfirmText = null;
    private String postActionConfirmText = null;
    private Shader.TileMode MODE;

    private int defaultGradientColor1 = Color.rgb(55, 90, 124);
    private int defaultGradientColor2 = Color.rgb(94, 144, 54);
    private int defaultGradientColor2Width;
    private int defaultGradientColor3 = Color.rgb(244, 45, 134);
    private int defaultAfterConfirmationBackground = Color.rgb(155, 144, 123);
    private float defaultThreshold = 0.7f;
    private int defaultMode = 0;

    private float x1;
    //x coordinate of where user first touches the button
    private String startButtonText;
    //the text on the button at the starting

    //whether the threshold distance beyond which action is considered confirmed is crossed or not


    private Swipe swipeCallback;
    private OnClickListener listener;
    private int defaultCanSwipeWidth;
    private boolean mEnable = true;

    /**
     * Instantiates a new Swipe button.
     *
     * @param context the context
     */
    public SwipeButton(@NonNull Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Swipe button.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public SwipeButton(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Swipe button.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public SwipeButton(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultGradientColor2Width = UIUtils.dip2px(200);
        defaultCanSwipeWidth = UIUtils.dip2px(70);


        final TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SwipeButton,
                defStyleAttr,
                0);
        initByAttributes(attributes);
        attributes.recycle();

    }

    private void initByAttributes(@NonNull TypedArray attributes) {
        gradientColor1 = attributes.getColor(R.styleable.SwipeButton_gradient_color_1, defaultGradientColor1);
        gradientColor2 = attributes.getColor(R.styleable.SwipeButton_gradient_color_2, defaultGradientColor2);
        gradientColor3 = attributes.getColor(R.styleable.SwipeButton_gradient_color_3, defaultGradientColor3);
        gradientColor2Width = (int) attributes.getDimension(R.styleable.SwipeButton_gradient_color_2_width,
                defaultGradientColor2Width);
        afterConfirmationBackground = attributes.getColor(R.styleable.SwipeButton_after_confirmation_background,
                defaultAfterConfirmationBackground);
        threshold = attributes.getFloat(R.styleable.SwipeButton_threshold, defaultThreshold);
        actionConfirmText = attributes.getString(R.styleable.SwipeButton_button_confirm_text);
        postActionConfirmText = attributes.getString(R.styleable.SwipeButton_button_post_confirm_text);
        buttonPressText = attributes.getString(R.styleable.SwipeButton_button_swipe_text);
        setMode(attributes.getInt(R.styleable.SwipeButton_swipe_mode, defaultMode));
    }

    /**
     * On touch event boolean.
     *
     * @param event the event
     * @return the boolean
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (mEnable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    // when user first touches the screen we get x and y coordinate
                    x1 = event.getX();

                    this.startButtonText = this.getText().toString();

                    if (x1 > 0 && x1 < defaultCanSwipeWidth) {
                        this.setText(getButtonPressText());
                    } else {
                        this.setText("从左边缘滑动才有效");
                    }

                swipeCallback.onButtonPress();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    //here we'll capture when the user swipes from left to right and write the logic to create the swiping effect

                    float x2 = event.getX();

                    //if left to right sweep event on screen
                    if (getX1() < x2 && x1 > 0 && x1 < defaultCanSwipeWidth) {
                        this.setBackgroundDrawable(null);

                        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());

                        double actionConfirmFraction = getThreshold();

                        Shader shader = new LinearGradient(
                                x2,
                                0,
                                x2 - getGradientColor2Width(),
                                0,
                                new int[]{getGradientColor3(), getGradientColor2(), getGradientColor1()},
                                new float[]{0, 0.5f, 1},
                                MODE
                        );

                        mDrawable.getPaint().setShader(shader);
                        this.setBackgroundDrawable(mDrawable);


                        if (x2 > (this.getWidth() * actionConfirmFraction)) {//提示释放点击
                            this.setText(getActionConfirmText());
                        } else {
                            this.setText(getButtonPressText());
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    //when the user releases touch then revert back the text
                    float x2 = event.getX();
                    int buttonColor = getAfterConfirmationBackground();
                    //if you choose to not set the confirmation text, it will set to the original button text;

                    this.setBackgroundDrawable(null);
                    this.setBackgroundColor(buttonColor);


                    if (x1 > 0 && x1 < defaultCanSwipeWidth && x2 > (this.getWidth() * this.getThreshold())) {//执行
                    swipeCallback.onSwipeConfirm();
                        listener.onClick(SwipeButton.this);
                        this.setText(getStartButtonText());
                    } else {//恢复原始状态
                        this.setText(getStartButtonText());
                    }


                    break;
                }
            }
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mEnable = enabled;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.listener = l;
    }

    public boolean confirmClick() {
        boolean result = false;
        if (listener != null) {
            listener.onClick(this);
            result = true;
        }
        return result;
    }

    public void setStartButtonText(String s) {
        this.startButtonText = s;
    }

    public void setButtonPressText(String s) {
        this.buttonPressText = s;
    }

    public void setActionConfirmText(String s) {
        this.actionConfirmText = s;
    }

    @Nullable
    private String getButtonPressText() {
        return buttonPressText;
    }


    private float getX1() {
        return x1;
    }

    private double getThreshold() {
        return threshold;
    }

    private int getGradientColor2Width() {
        return gradientColor2Width;
    }

    private int getGradientColor3() {
        return gradientColor3;
    }

    private int getGradientColor2() {
        return gradientColor2;
    }

    private int getGradientColor1() {
        return gradientColor1;
    }

    @Nullable
    private String getActionConfirmText() {
        return actionConfirmText;
    }

    private String getStartButtonText() {
        return startButtonText;
    }

    private int getAfterConfirmationBackground() {
        return afterConfirmationBackground;
    }

    public String getPostActionConfirmText() {
        return postActionConfirmText;
    }

    /**
     * Add on swipe callback.
     *
     * @param swipeCallback the swipe callback. This can be a concrete class implementing Swipe interface or an anonymous class
     */
    public void addOnSwipeCallback(Swipe swipeCallback) {
        this.swipeCallback = swipeCallback;
    }

    public void setMode(int mode) {
        switch (mode) {
            case 0:
                MODE = Shader.TileMode.CLAMP;
                break;
            case 1:
                MODE = Shader.TileMode.MIRROR;
                break;
            case 2:
                MODE = Shader.TileMode.REPEAT;
                break;
        }
    }

    /**
     * The interface Swipe. This interface is used to attach callbacks to the various events that occur while swiping the button
     */
    public interface Swipe {
        /**
         * On button press.
         */
        void onButtonPress();

        /**
         * On swipe cancel.
         */
        void onSwipeCancel();

        /**
         * On swipe confirm.
         */
        void onSwipeConfirm();
    }
}


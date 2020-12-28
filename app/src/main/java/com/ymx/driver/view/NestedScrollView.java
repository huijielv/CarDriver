package com.ymx.driver.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NestedScrollView extends ScrollView {
    private  int mLastXIntercept;
    private  int mLastYintercept;
    public NestedScrollView(Context context) {
        super(context);
    }

    public NestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;                                 //注解1
//                if (!scrisFinished()){                       //注解2
//                    mScroller.abortAnimation();
//                    intercepted = true;
//                }
                break;
            case MotionEvent.ACTION_MOVE:                 //注解3
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYintercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)){     //在这里的if中加入是否拦截的判断
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        mLastXIntercept = x;
        mLastYintercept = y;

        return intercepted;

    }
}

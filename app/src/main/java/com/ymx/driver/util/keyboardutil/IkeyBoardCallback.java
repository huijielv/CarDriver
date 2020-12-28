package com.ymx.driver.util.keyboardutil;

/**
 * 用于接收键盘事件的回调
 *
 * @author Simon
 */
public interface IkeyBoardCallback {
    /**
     * 当键盘显示时回调
     */
    void onKeyBoardShow(int keyboardHeight);

    /**
     * 当键盘隐藏时回调
     */
    void onKeyBoardHidden();
}

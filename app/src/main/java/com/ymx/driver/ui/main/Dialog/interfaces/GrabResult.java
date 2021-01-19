package com.ymx.driver.ui.main.Dialog.interfaces;

public interface GrabResult<T> {
    public void onRequestStart();

    public void onRequestEnd();

    public void onSuccees(T t);

    public void onSuccees();

    public void onFailure(String message);
}

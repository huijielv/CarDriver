package com.ymx.driver.keepalive;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.Nullable;




public class KeepAliveActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KeepAliveManager.getInstance().setKeepAliveActivity(this);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = 1;
        attributes.height = 1;
        window.setAttributes(attributes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepAliveManager.getInstance().destroy();
    }
}

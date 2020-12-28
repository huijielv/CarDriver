package com.ymx.driver.push;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.message.UmengNotifyClickActivity;
import com.ymx.driver.R;
import com.ymx.driver.ui.main.activity.MainActivity;
import com.ymx.driver.util.LogUtil;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

public class MipushActivity extends UmengNotifyClickActivity {
    private static final String TAG = "MipushActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        if (intent != null) {
            try {
                String msgBody = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
                LogUtil.i(TAG, "parseIntent: " + msgBody);
                if (!TextUtils.isEmpty(msgBody)) {
                    JSONObject obj = new JSONObject(msgBody);
                    JSONObject jsonObject = obj.getJSONObject("body");
                    String custom = jsonObject.optString("custom");
                    if (!TextUtils.isEmpty(custom)) {
                        MainActivity.start(this, new Intent()
                                .putExtra(MainActivity.EXTRA_DATA, custom));
                        finish();
                    } else {
                        MainActivity.start(this);
                        finish();
                    }
                } else {
                    MainActivity.start(this);
                    finish();
                }
            } catch (Exception e) {
                MainActivity.start(this);
                finish();
                e.printStackTrace();
            }
        }
    }
}

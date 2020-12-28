package com.ymx.driver.wxapi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ymx.driver.R;
import com.ymx.driver.config.AppConfig;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity  extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WEXIN_KEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0://支付成功
                    UIUtils.showToast(UIUtils.getString(R.string.pay_success));
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_QUERY_PAY_RESULT));
                    break;
                case -1://错误，可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
                    UIUtils.showToast(UIUtils.getString(R.string.pay_fail));
                    break;
                case -2://用户取消，无需处理。发生场景：用户不支付了，点击取消，返回APP。
                    UIUtils.showToast(UIUtils.getString(R.string.pay_fail));
                    break;

            }
            finish();//这里需要关闭该页面
        }
    }
}

